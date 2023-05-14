package com.github.ksewen.ganyu.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.model.UserRegisterModel;
import com.github.ksewen.ganyu.service.AuthService;
import com.github.ksewen.ganyu.service.RoleService;
import com.github.ksewen.ganyu.service.UserService;

/**
 * @author ksewen
 * @date 10.05.2023 16:07
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String USER_ROLE_NAME = "USER";

    @Override
    public boolean register(UserRegisterModel registerModel) {
        Role exist = this.roleService.findFirstByName(this.USER_ROLE_NAME);
        if (exist == null) {
            throw new CommonException(ResultCode.NOT_FOUND, "user with given information ist already exist");
        }
        registerModel.setPassword(this.passwordEncoder.encode(registerModel.getPassword()));
        if (!StringUtils.hasLength(registerModel.getNickname())) {
            registerModel.setNickname(registerModel.getUsername());
        }
        List<Role> roles = new ArrayList<>();
        roles.add(exist);
        return this.userService.createUser(registerModel, roles);
    }

    @Override
    public String login(String email, String password) {
        return null;
    }

    @Override
    public String refresh(String oldToken) {
        return null;
    }

}
