package com.github.ksewen.ganyu.service.impl;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.generate.domain.Role;
import com.github.ksewen.ganyu.model.UserRegisterModel;
import com.github.ksewen.ganyu.service.AuthService;
import com.github.ksewen.ganyu.service.RoleService;
import com.github.ksewen.ganyu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

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
        List<Role> records = this.roleService.selectByName(this.USER_ROLE_NAME);
        if (CollectionUtils.isEmpty(records)) {
            throw new CommonException(ResultCode.NOT_FOUND);
        }
        if (records.size() > 1) {
            throw new CommonException(ResultCode.DUPLICATE_RECORD);
        }
        registerModel.setPassword(this.passwordEncoder.encode(registerModel.getPassword()));
        List<Integer> roles = new ArrayList<>();
        roles.add(records.get(0).getId());
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
