package com.github.ksewen.ganyu.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.domain.UserRole;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.mapper.UserMapper;
import com.github.ksewen.ganyu.model.UserRegisterModel;
import com.github.ksewen.ganyu.service.UserRoleService;
import com.github.ksewen.ganyu.service.UserService;

/**
 * @author ksewen
 * @date 10.05.2023 23:54
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public User findFirstByUsername(String username) {
        return this.userMapper.findFirstByUsername(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser(UserRegisterModel userRegisterModel, List<Role> roles) {
        User condition = User.builder()
                .email(userRegisterModel.getEmail()).username(userRegisterModel.getUsername()).build();
        if (StringUtils.hasLength(userRegisterModel.getMobile())) {
            condition.setMobile(userRegisterModel.getMobile());
        }
        Example<User> example = Example.of(condition);
        Optional<User> result = this.userMapper.findOne(example);
        if (result.isPresent()) {
            throw new CommonException(ResultCode.ALREADY_EXIST);
        }
        User user = User.builder()
                .username(userRegisterModel.getUsername())
                .email(userRegisterModel.getEmail())
                .nickname(userRegisterModel.getNickname())
                .password(userRegisterModel.getPassword())
                .mobile(userRegisterModel.getMobile())
                .avatarUrl(userRegisterModel.getAvatarUrl())
                .build();
        this.userMapper.saveAndFlush(user);

        List<UserRole> userRoles = new ArrayList<>();
        for (Role role : roles) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            userRoles.add(userRole);
        }
        this.userRoleService.saveAllAndFlush(userRoles);
        return true;
    }

}
