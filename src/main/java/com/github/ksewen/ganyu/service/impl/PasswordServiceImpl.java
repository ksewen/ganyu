package com.github.ksewen.ganyu.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.constant.ErrorMessageConstants;
import com.github.ksewen.ganyu.domain.CaptchaType;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.service.CaptchaService;
import com.github.ksewen.ganyu.service.CaptchaTypeService;
import com.github.ksewen.ganyu.service.PasswordService;
import com.github.ksewen.ganyu.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ksewen
 * @date 29.05.2023 15:52
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordServiceImpl implements PasswordService {

    private final UserService userService;

    private final CaptchaService captchaService;

    private final CaptchaTypeService captchaTypeService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void modify(String exist, String modify, long userId) {
        User user = this.userService.findById(userId).map(u -> {
            boolean matches = this.passwordEncoder.matches(exist, u.getPassword());
            if (!matches) {
                throw new CommonException(ResultCode.ACCESS_DENIED, "invalid old password");
            }
            return u;
        }).orElseThrow(
                () -> new CommonException(ResultCode.NOT_FOUND, ErrorMessageConstants.USER_NOT_FOUND_ERROR_MESSAGE));
        user.setPassword(this.passwordEncoder.encode(modify));
        this.userService.saveAndFlush(user);
    }

    @Override
    public void reset(String captcha, String modify, long userId) {
        final String captchaType = "PASSWORD_RESET";
        CaptchaType type = this.captchaTypeService.findByName(captchaType).orElseThrow(() -> {
            log.error(
                    "can not find the captcha type {} in the system, please make sure the system configuration correct!",
                    captchaType);
            return new CommonException(ResultCode.NOT_FOUND, "invalid captcha type name");
        });
        boolean check = this.captchaService.check(userId, captcha, type.getId());
        if (!check) {
            throw new CommonException(ResultCode.CAPTCHA_INVALID);
        }
        User user = this.userService.findById(userId).orElseThrow(
                () -> new CommonException(ResultCode.NOT_FOUND, ErrorMessageConstants.USER_NOT_FOUND_ERROR_MESSAGE));
        user.setPassword(this.passwordEncoder.encode(modify));
        this.userService.saveAndFlush(user);
    }

}
