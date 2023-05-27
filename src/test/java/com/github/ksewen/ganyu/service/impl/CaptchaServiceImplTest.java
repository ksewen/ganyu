package com.github.ksewen.ganyu.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.domain.UserCaptcha;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.helper.CaptchaHelpers;
import com.github.ksewen.ganyu.mapper.CaptchaTypeMapper;
import com.github.ksewen.ganyu.mapper.UserCaptchaMapper;

/**
 * @author ksewen
 * @date 27.05.2023 22:43
 */
@SpringBootTest(classes = CaptchaServiceImpl.class)
class CaptchaServiceImplTest {

    @Autowired
    private CaptchaServiceImpl captchaService;

    @MockBean
    private CaptchaHelpers captchaHelpers;

    @MockBean
    private CaptchaTypeMapper captchaTypeMapper;

    @MockBean
    private UserCaptchaMapper userCaptchaMapper;

    private final String code = "123456";
    private final long userId = 1L;
    private final long captchaTypeId = 1L;

    @Test
    void checkSuccess() {
        when(this.userCaptchaMapper.findFirstByUserIdAndCaptchaTypeIdOrderByExpirationDesc(this.userId,
                this.captchaTypeId))
                        .thenReturn(Optional.of(UserCaptcha.builder().id(1L).userId(this.userId)
                                .captchaTypeId(this.captchaTypeId).code(this.code).build()));
        boolean actual = this.captchaService.check(this.userId, this.code, this.captchaTypeId);
        assertThat(actual).isTrue();
    }

    @Test
    void checkWhenNotHaveCaptcha() {
        when(this.userCaptchaMapper.findFirstByUserIdAndCaptchaTypeIdOrderByExpirationDesc(anyLong(), anyLong()))
                .thenReturn(Optional.ofNullable(null));
        CommonException exception = Assertions.assertThrows(CommonException.class,
                () -> this.captchaService.check(this.userId, this.code, this.captchaTypeId));
        assertThat(exception).matches(e -> ResultCode.CAPTCHA_INVALID.equals(e.getCode()))
                .matches(e -> "given user not has any valid captcha".equals(e.getMessage()));
    }

    @Test
    void checkWithInvalidCaptcha() {
        when(this.userCaptchaMapper.findFirstByUserIdAndCaptchaTypeIdOrderByExpirationDesc(anyLong(), anyLong()))
                .thenReturn(Optional.of(UserCaptcha.builder().id(1L).userId(this.userId)
                        .captchaTypeId(this.captchaTypeId).code("111111").build()));
        CommonException exception = Assertions.assertThrows(CommonException.class,
                () -> this.captchaService.check(userId, this.code, captchaTypeId));
        assertThat(exception).matches(e -> ResultCode.CAPTCHA_INVALID.equals(e.getCode()))
                .matches(e -> ResultCode.CAPTCHA_INVALID.getMessage().equals(e.getMessage()));
    }
}
