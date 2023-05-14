package com.github.ksewen.ganyu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ksewen.ganyu.dto.auth.LoginRequest;
import com.github.ksewen.ganyu.dto.auth.RegisterRequest;
import com.github.ksewen.ganyu.dto.base.Result;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.model.UserRegisterModel;
import com.github.ksewen.ganyu.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

/**
 * @author ksewen
 * @date 10.05.2023 12:24
 */
@RestController
@RequestMapping("/auth")
public class AuthController implements LoggingController {

    private final String NAME = "authentication";

    @Autowired
    private AuthService authService;

    @Autowired
    private BeanMapperHelpers beanMapperHelpers;

    @Operation(summary = "注册用户")
    @PostMapping("/register")
    public Result<Boolean> register(@Valid @RequestBody RegisterRequest request) {
        UserRegisterModel userRegisterModel = this.beanMapperHelpers
                .createAndCopyProperties(request, UserRegisterModel.class);
        return Result.success(this.authService.register(userRegisterModel));
    }

    @Operation(summary = "登录")
    @PostMapping("/login")
    public Result<Boolean> login(@Valid @RequestBody LoginRequest request) {
        String login = authService.login(request.getEmail(), request.getPassword());
        return Result.success(login);
    }

    @Override
    public String name() {
        return this.NAME;
    }
}
