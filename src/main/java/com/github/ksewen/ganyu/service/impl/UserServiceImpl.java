package com.github.ksewen.ganyu.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.constant.ErrorMessageConstants;
import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.domain.UserRole;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.mapper.UserMapper;
import com.github.ksewen.ganyu.model.UserModifyModel;
import com.github.ksewen.ganyu.model.UserRegisterModel;
import com.github.ksewen.ganyu.service.RoleService;
import com.github.ksewen.ganyu.service.UserRoleService;
import com.github.ksewen.ganyu.service.UserService;

import jakarta.persistence.criteria.Predicate;
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

    @Override
    public Optional<User> findByUsername(String username) {
        return this.userMapper.findByUsername(username);
    }

    @Override
    public Optional<User> findById(long userId) {
        return this.userMapper.findById(userId);
    }

    @Override
    public User saveAndFlush(User user) {
        return this.userMapper.saveAndFlush(user);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User add(UserRegisterModel userRegisterModel, List<Role> roles) {
        Optional<User> exist = this.userMapper.findOne((Specification<User>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.hasLength(userRegisterModel.getUsername())) {
                list.add(criteriaBuilder.equal(root.get("username").as(String.class), userRegisterModel.getUsername()));
            }
            if (StringUtils.hasLength(userRegisterModel.getEmail())) {
                list.add(criteriaBuilder.equal(root.get("email").as(String.class), userRegisterModel.getEmail()));
            }
            if (StringUtils.hasLength(userRegisterModel.getMobile())) {
                list.add(criteriaBuilder.equal(root.get("mobile").as(String.class), userRegisterModel.getMobile()));
            }
            Predicate[] array = new Predicate[list.size()];
            Predicate[] predicates = list.toArray(array);
            return criteriaBuilder.or(predicates);
        });
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
        if (userModifyModel.getId() != operationUserId) {
            this.roleService.checkAdministrator(operationUserId);
        }
        Optional<User> exist = this.userMapper.findById(userModifyModel.getId());
        User insert = exist.orElseThrow(
                () -> new CommonException(ResultCode.NOT_FOUND, ErrorMessageConstants.USER_NOT_FOUND_ERROR_MESSAGE));
        BeanUtils.copyProperties(userModifyModel, insert, this.beanMapperHelpers.getNullPropertyNames(userModifyModel));
        return this.userMapper.saveAndFlush(insert);
    }

}
