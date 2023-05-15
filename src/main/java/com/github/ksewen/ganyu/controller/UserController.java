package com.github.ksewen.ganyu.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.dto.request.UserEditRequest;
import com.github.ksewen.ganyu.dto.response.UserInfoResponse;
import com.github.ksewen.ganyu.dto.response.base.Result;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.model.UserEditModel;
import com.github.ksewen.ganyu.security.Authentication;
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

    private final Authentication authentication;

    private final String NAME = "user";

    @Operation(summary = "edit user information")
    @PostMapping("/edit")
    public Result<UserInfoResponse> edit(@Valid @RequestBody UserEditRequest request) {
        UserEditModel userEditModel = this.beanMapperHelpers.createAndCopyProperties(request,
                UserEditModel.class);
        userEditModel.setId(this.authentication.getUserId());
        User user = this.userService.edit(userEditModel, this.authentication.getUserId());
        return Result.success(this.beanMapperHelpers.createAndCopyProperties(user, UserInfoResponse.class));
    }

    @Override
    public String name() {
        return this.NAME;
    }
}
