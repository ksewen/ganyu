package com.github.ksewen.ganyu.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.dto.auth.JwtTokenResponse;
import com.github.ksewen.ganyu.dto.auth.LoginRequest;
import com.github.ksewen.ganyu.dto.auth.RegisterRequest;
import com.github.ksewen.ganyu.dto.base.Result;
import com.github.ksewen.ganyu.dto.user.UserInfoResponse;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.model.UserRegisterModel;
import com.github.ksewen.ganyu.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 10.05.2023 12:24
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements LoggingController {

    private final String NAME = "authentication";

    private final AuthService authService;

    private final BeanMapperHelpers beanMapperHelpers;

    @Operation(summary = "注册用户")
    @PostMapping("/register")
    public Result<UserInfoResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserRegisterModel userRegisterModel = this.beanMapperHelpers.createAndCopyProperties(request,
                UserRegisterModel.class);
        User user = this.authService.register(userRegisterModel);
        return Result.success(this.beanMapperHelpers.createAndCopyProperties(user, UserInfoResponse.class));
    }

    @Operation(summary = "登录")
    @PostMapping("/login")
    public Result<JwtTokenResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtTokenResponse token = this.authService.login(request.getEmail(), request.getPassword());
        return Result.success(token);
    }

    @Override
    public String name() {
        return this.NAME;
    }
}
