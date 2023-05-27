package com.github.ksewen.ganyu.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.domain.CaptchaType;
import com.github.ksewen.ganyu.domain.UserCaptcha;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.helper.CaptchaHelpers;
import com.github.ksewen.ganyu.mapper.CaptchaTypeMapper;
import com.github.ksewen.ganyu.mapper.UserCaptchaMapper;
import com.github.ksewen.ganyu.service.CaptchaService;

import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 20.05.2023 22:35
 */
@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    private final CaptchaHelpers captchaHelpers;

    private final CaptchaTypeMapper captchaTypeMapper;

    private final UserCaptchaMapper userCaptchaMapper;

    private final int EXPIRE_TIME = 600;

    @Override
    @Transactional
    public void apply(Long userId, Long typeId) {
        CaptchaType type = this.captchaTypeMapper.findById(typeId)
                .orElseThrow(() -> new CommonException(ResultCode.NOT_FOUND, "can not find the captcha type"));
        UserCaptcha record = UserCaptcha.builder().userId(userId).captchaTypeId(type.getId())
                .code(this.captchaHelpers.generateSimple())
                .expiration(new Date(new Date().getTime() + this.EXPIRE_TIME)).build();
        this.userCaptchaMapper.saveAndFlush(record);
        this.userCaptchaMapper.deleteByUserIdAndCaptchaTypeId(userId, type.getId());
    }

    @Override
    public boolean check(Long userId, String code, Long typeId) {
        UserCaptcha userCaptcha = this.userCaptchaMapper
                .findFirstByUserIdAndCaptchaTypeIdOrderByExpirationDesc(userId, typeId).orElseThrow(
                        () -> new CommonException(ResultCode.CAPTCHA_INVALID, "given user not has any valid captcha"));
        if (!userCaptcha.getCode().equals(code)) {
            throw new CommonException(ResultCode.CAPTCHA_INVALID);
        }
        return true;
    }
}
