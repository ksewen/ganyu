package com.github.ksewen.ganyu.service.impl;

import org.springframework.stereotype.Service;

import com.github.ksewen.ganyu.configuration.constant.AuthenticationConstants;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.model.UserRegisterModel;
import com.github.ksewen.ganyu.service.AdminService;
import com.github.ksewen.ganyu.service.AuthService;

import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 15.05.2023 16:27
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AuthService authService;

    @Override
    public User add(UserRegisterModel userRegisterModel) {
        return this.authService.register(userRegisterModel, AuthenticationConstants.ADMIN_ROLE_NAME);
    }
}
