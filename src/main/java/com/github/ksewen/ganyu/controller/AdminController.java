package com.github.ksewen.ganyu.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.dto.request.UserRegisterRequest;
import com.github.ksewen.ganyu.dto.response.UserInfoResponse;
import com.github.ksewen.ganyu.dto.response.base.Result;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.model.UserRegisterModel;
import com.github.ksewen.ganyu.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 10.05.2023 12:24
 */
@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "jwt-auth")
@RequiredArgsConstructor
public class AdminController implements LoggingController {

    private final String NAME = "administrator";

    private final AdminService adminService;

    private final BeanMapperHelpers beanMapperHelpers;

    @Operation(summary = "modify administrator")
    @PostMapping("/modify")
    public Result<UserInfoResponse> modify(@Valid @RequestBody UserRegisterRequest request) {
        UserRegisterModel userRegisterModel = this.beanMapperHelpers.createAndCopyProperties(request,
                UserRegisterModel.class);
        User user = this.adminService.modify(userRegisterModel);
        return Result.success(this.beanMapperHelpers.createAndCopyProperties(user, UserInfoResponse.class));
    }

    @Override
    public String name() {
        return this.NAME;
    }
}
