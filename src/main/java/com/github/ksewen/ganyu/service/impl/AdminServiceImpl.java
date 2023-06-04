package com.github.ksewen.ganyu.service.impl;

import org.springframework.stereotype.Service;

import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.model.UserModifyModel;
import com.github.ksewen.ganyu.service.AdminService;
import com.github.ksewen.ganyu.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 15.05.2023 16:27
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserService userService;

    @Override
    public User modify(UserModifyModel userRegisterModel, long operationUserId) {
        return this.userService.modify(userRegisterModel, operationUserId);
    }
}
