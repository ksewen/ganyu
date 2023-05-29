package com.github.ksewen.ganyu.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.dto.request.PasswordModifyRequest;
import com.github.ksewen.ganyu.dto.request.PasswordResetRequest;
import com.github.ksewen.ganyu.dto.request.UserModifyRequest;
import com.github.ksewen.ganyu.dto.response.UserInfoResponse;
import com.github.ksewen.ganyu.dto.response.base.Result;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.model.UserModifyModel;
import com.github.ksewen.ganyu.security.Authentication;
import com.github.ksewen.ganyu.service.AuthService;
import com.github.ksewen.ganyu.service.PasswordService;
import com.github.ksewen.ganyu.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 15.05.2023 22:23
 */
@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "jwt-auth")
@RequiredArgsConstructor
public class UserController implements LoggingController {

    private final BeanMapperHelpers beanMapperHelpers;

    private final UserService userService;

    private final PasswordService passwordService;

    private final AuthService authService;

    private final Authentication authentication;

    private final String NAME = "user";

    @Operation(summary = "modify user information")
    @PostMapping("/modify")
    public Result<UserInfoResponse> modify(@Valid @RequestBody UserModifyRequest request) {
        UserModifyModel userModifyModel = this.beanMapperHelpers.createAndCopyProperties(request,
                UserModifyModel.class);
        userModifyModel.setId(this.authentication.getUserId());
        User user = this.userService.modify(userModifyModel, this.authentication.getUserId());
        return Result.success(this.beanMapperHelpers.createAndCopyProperties(user, UserInfoResponse.class));
    }

    @Operation(summary = "modify user password")
    @PostMapping("/password/modify")
    public Result<Boolean> modifyPassword(@Valid @RequestBody PasswordModifyRequest request) {
        this.passwordService.modify(request.getExist(), request.getModify(), this.authentication.getUserId());
        return Result.success(Boolean.TRUE);
    }

    @Operation(summary = "reset user password")
    @PostMapping("/password/reset")
    public Result<Boolean> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        this.passwordService.reset(request.getCaptcha(), request.getModify(), this.authentication.getUserId());
        return Result.success(Boolean.TRUE);
    }

    @Operation(summary = "logout")
    @PostMapping("/logout")
    public Result<Boolean> logout() {
        this.authService.logout(this.authentication.getUserId());
        return Result.success(Boolean.TRUE);
    }

    @Override
    public String name() {
        return this.NAME;
    }
}
