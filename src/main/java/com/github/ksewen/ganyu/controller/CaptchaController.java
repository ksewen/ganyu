package com.github.ksewen.ganyu.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.ksewen.ganyu.dto.request.CaptchaApplyRequest;
import com.github.ksewen.ganyu.dto.response.base.Result;
import com.github.ksewen.ganyu.security.Authentication;
import com.github.ksewen.ganyu.service.CaptchaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 21.05.2023 11:16
 */
@RestController
@RequestMapping("/captcha")
@SecurityRequirement(name = "jwt-auth")
@RequiredArgsConstructor
public class CaptchaController implements LoggingController {

    private final CaptchaService captchaService;

    private final Authentication authentication;

    private final String NAME = "captcha";

    @Operation(summary = "apply captcha")
    @PostMapping("/apply")
    public Result<Boolean> apply(@Valid @RequestBody CaptchaApplyRequest request) {
        this.captchaService.apply(this.authentication.getUserId(), request.getTypeId());
        return Result.success(Boolean.TRUE);
    }

    @Override
    public String name() {
        return this.NAME;
    }
}
