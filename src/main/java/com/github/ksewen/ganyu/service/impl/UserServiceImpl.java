package com.github.ksewen.ganyu.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.domain.UserRole;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.mapper.UserMapper;
import com.github.ksewen.ganyu.mapper.specification.UserSpecification;
import com.github.ksewen.ganyu.model.UserRegisterModel;
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

    @Override
    public Optional<User> findByUsername(String username) {
        return this.userMapper.findByUsername(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User createUser(UserRegisterModel userRegisterModel, List<Role> roles) {
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
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            userRoles.add(userRole);
        }
        this.userRoleService.saveAllAndFlush(userRoles);
        return result;
    }

}
