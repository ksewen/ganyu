package com.github.ksewen.ganyu.service.impl;

import java.util.Date;

import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.constant.ErrorMessageConstants;
import com.github.ksewen.ganyu.constant.MailConstants;
import com.github.ksewen.ganyu.domain.CaptchaType;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.domain.UserCaptcha;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.helper.CaptchaHelpers;
import com.github.ksewen.ganyu.mapper.CaptchaTypeMapper;
import com.github.ksewen.ganyu.mapper.UserCaptchaMapper;
import com.github.ksewen.ganyu.service.CaptchaService;
import com.github.ksewen.ganyu.service.MailService;
import com.github.ksewen.ganyu.service.UserService;

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

    private final UserService userService;

    private final MailService mailService;

    private final int EXPIRE_TIME = 600;

    @Override
    @Transactional
    public void apply(Long userId, Long typeId) {
        User user = this.userService.findById(userId).orElseThrow(
                () -> new CommonException(ResultCode.NOT_FOUND, ErrorMessageConstants.USER_NOT_FOUND_ERROR_MESSAGE));
        CaptchaType type = this.captchaTypeMapper.findById(typeId)
                .orElseThrow(() -> new CommonException(ResultCode.NOT_FOUND, "can not find the captcha type"));
        UserCaptcha record = UserCaptcha.builder().userId(userId).captchaTypeId(type.getId())
                .code(this.captchaHelpers.generateSimple())
                .expiration(new Date(new Date().getTime() + this.EXPIRE_TIME)).build();
        this.userCaptchaMapper.deleteByUserIdAndCaptchaTypeId(userId, type.getId());
        this.userCaptchaMapper.saveAndFlush(record);
        String text = MessageFormatter.basicArrayFormat(MailConstants.CAPTCHA_TEXT_TEMPLATE,
                new Object[] { record.getCode(), type.getName(), this.EXPIRE_TIME });
        this.mailService.sendSimple(user.getEmail(), MailConstants.CAPTCHA_SUBJECT, text);
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
