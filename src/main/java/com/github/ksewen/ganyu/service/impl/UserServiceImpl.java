package com.github.ksewen.ganyu.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.github.ksewen.ganyu.configuration.constant.AuthenticationConstants;
import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.domain.UserRole;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.mapper.UserMapper;
import com.github.ksewen.ganyu.mapper.specification.UserSpecification;
import com.github.ksewen.ganyu.model.UserModifyModel;
import com.github.ksewen.ganyu.model.UserRegisterModel;
import com.github.ksewen.ganyu.service.RoleService;
import com.github.ksewen.ganyu.service.UserRoleService;
import com.github.ksewen.ganyu.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 10.05.2023 23:54
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRoleService userRoleService;

    private final RoleService roleService;

    private final BeanMapperHelpers beanMapperHelpers;

    private final PasswordEncoder passwordEncoder;

    private final String USER_NOT_FOUND_ERROR_MESSAGE = "can not found a exist user by given id";

    @Override
    public Optional<User> findByUsername(String username) {
        return this.userMapper.findByUsername(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User add(UserRegisterModel userRegisterModel, List<Role> roles) {
        UserSpecification specification = new UserSpecification(null, userRegisterModel.getUsername(), null,
                userRegisterModel.getEmail(), userRegisterModel.getMobile(), false);
        Optional<User> exist = this.userMapper.findOne(specification);
        if (exist.isPresent()) {
            throw new CommonException(ResultCode.ALREADY_EXIST);
        }
        User user = User.builder().username(userRegisterModel.getUsername()).email(userRegisterModel.getEmail())
                .nickname(userRegisterModel.getNickname()).password(userRegisterModel.getPassword())
                .mobile(userRegisterModel.getMobile()).avatarUrl(userRegisterModel.getAvatarUrl()).build();
        User result = this.userMapper.saveAndFlush(user);

        List<UserRole> userRoles = new ArrayList<>();
        for (Role role : roles) {
            UserRole userRole = new UserRole();
            userRole.setUserId(result.getId());
            userRole.setRoleId(role.getId());
            userRoles.add(userRole);
        }
        this.userRoleService.saveAllAndFlush(userRoles);
        return result;
    }

    @Override
    public User modify(UserModifyModel userModifyModel, long operationUserId) {
        Assert.notNull(userModifyModel.getId(), "the id of the modified user must be not blank");
        if (userModifyModel.getId() != operationUserId) {
            List<Role> operationRoles = this.roleService.findByUserId(operationUserId);
            boolean access = !CollectionUtils.isEmpty(operationRoles) && operationRoles.stream()
                    .anyMatch(r -> AuthenticationConstants.ADMIN_ROLE_NAME.equals(r.getName()));
            if (!access) {
                throw new CommonException(ResultCode.ACCESS_DENIED,
                        "only administrator can edit other user information");
            }
        }
        Optional<User> exist = this.userMapper.findById(userModifyModel.getId());
        User insert = exist
                .orElseThrow(() -> new CommonException(ResultCode.NOT_FOUND, this.USER_NOT_FOUND_ERROR_MESSAGE));
        BeanUtils.copyProperties(userModifyModel, insert, this.beanMapperHelpers.getNullPropertyNames(userModifyModel));
        return this.userMapper.saveAndFlush(insert);
    }

    @Override
    public User modifyPassword(String exist, String modify, long userId) {
        User user = this.userMapper.findById(userId).map(u -> {
            boolean matches = this.passwordEncoder.matches(exist, u.getPassword());
            if (!matches) {
                throw new CommonException(ResultCode.ACCESS_DENIED, "invalid old password");
            }
            return u;
        }).orElseThrow(() -> new CommonException(ResultCode.NOT_FOUND, this.USER_NOT_FOUND_ERROR_MESSAGE));
        user.setPassword(this.passwordEncoder.encode(modify));
        return this.userMapper.saveAndFlush(user);
    }

}
