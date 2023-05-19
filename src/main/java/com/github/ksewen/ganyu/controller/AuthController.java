package com.github.ksewen.ganyu.controller;

import org.springframework.web.bind.annotation.*;

import com.github.ksewen.ganyu.configuration.constant.AuthenticationConstants;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.dto.request.LoginRequest;
import com.github.ksewen.ganyu.dto.request.UserRegisterRequest;
import com.github.ksewen.ganyu.dto.response.JwtTokenResponse;
import com.github.ksewen.ganyu.dto.response.UserInfoResponse;
import com.github.ksewen.ganyu.dto.response.base.Result;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.model.UserRegisterModel;
import com.github.ksewen.ganyu.service.AuthService;
import com.github.ksewen.ganyu.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

    private final TokenService tokenService;

    private final BeanMapperHelpers beanMapperHelpers;

    @Operation(summary = "register")
    @PostMapping("/register")
    public Result<UserInfoResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        UserRegisterModel userRegisterModel = this.beanMapperHelpers.createAndCopyProperties(request,
                UserRegisterModel.class);
        User user = this.authService.register(userRegisterModel, AuthenticationConstants.USER_ROLE_NAME);
        return Result.success(this.beanMapperHelpers.createAndCopyProperties(user, UserInfoResponse.class));
    }

    @Operation(summary = "login")
    @PostMapping("/login")
    public Result<JwtTokenResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtTokenResponse token = this.authService.login(request.getUsername(), request.getPassword());
        return Result.success(token);
    }

    @Operation(summary = "token-refresh")
    @PostMapping("/token-refresh")
    public Result<JwtTokenResponse> tokenRefresh(@RequestParam @NotBlank(message = "{auth.refresh.token.null}") String refreshToken) {
        JwtTokenResponse token = this.tokenService.refresh(refreshToken);
        return Result.success(token);
    }

    @Override
    public String name() {
        return this.NAME;
    }
}
