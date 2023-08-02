package com.github.ksewen.ganyu.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.constant.ErrorMessageConstants;
import com.github.ksewen.ganyu.domain.CaptchaType;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.domain.UserCaptcha;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.helper.CaptchaHelpers;
import com.github.ksewen.ganyu.mapper.UserCaptchaMapper;
import com.github.ksewen.ganyu.service.CaptchaTypeService;
import com.github.ksewen.ganyu.service.MailService;
import com.github.ksewen.ganyu.service.UserService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author ksewen
 * @date 27.05.2023 22:43
 */
@SpringBootTest(classes = CaptchaServiceImpl.class)
class CaptchaServiceImplTest {

  @Autowired private CaptchaServiceImpl captchaService;

  @MockBean private CaptchaHelpers captchaHelpers;

  @MockBean private CaptchaTypeService captchaTypeService;

  @MockBean private UserService userService;

  @MockBean private MailService mailService;

  @MockBean private UserCaptchaMapper userCaptchaMapper;

  private final String name = "ksewen";

  private final String email = "ksewen77@gmail.com";

  private final String code = "123456";
  private final long userId = 1L;
  private final long captchaTypeId = 1L;

  private final String captchaName = "TEST";

  @Test
  void applySuccess() {
    when(this.userService.findById(anyLong()))
        .thenReturn(
            Optional.of(
                User.builder().id(this.userId).username(this.name).email(this.email).build()));
    when(this.captchaTypeService.findById(anyLong()))
        .thenReturn(
            Optional.of(
                CaptchaType.builder().id(this.captchaTypeId).name(this.captchaName).build()));
    when(this.captchaHelpers.generateSimple()).thenReturn(this.code);
    this.captchaService.apply(1L, 1L);
    verify(this.userCaptchaMapper, times(1)).deleteByUserIdAndCaptchaTypeId(1L, 1L);
    verify(this.userCaptchaMapper, times(1))
        .saveAndFlush(
            argThat(
                record ->
                    record.getUserId() == 1L
                        && record.getCaptchaTypeId() == 1L
                        && this.code.equals(record.getCode())));
    verify(this.mailService, times(1))
        .sendSimple(argThat(email -> this.email.equals(email)), anyString(), anyString());
  }

  @Test
  void applyWithNotExistUser() {
    when(this.userService.findById(anyLong())).thenReturn(Optional.ofNullable(null));
    CommonException exception =
        Assertions.assertThrows(CommonException.class, () -> this.captchaService.apply(1L, 1L));
    assertThat(exception)
        .matches(e -> ResultCode.NOT_FOUND.equals(e.getCode()))
        .matches(e -> ErrorMessageConstants.USER_NOT_FOUND_ERROR_MESSAGE.equals(e.getMessage()));
  }

  @Test
  void applyWithInvalidCaptchaType() {
    when(this.userService.findById(anyLong()))
        .thenReturn(
            Optional.of(
                User.builder().id(this.userId).username(this.name).email(this.email).build()));
    when(this.captchaTypeService.findById(anyLong())).thenReturn(Optional.ofNullable(null));
    CommonException exception =
        Assertions.assertThrows(CommonException.class, () -> this.captchaService.apply(1L, 1L));
    assertThat(exception)
        .matches(e -> ResultCode.NOT_FOUND.equals(e.getCode()))
        .matches(
            e -> ErrorMessageConstants.CAPTCHA_TYPE_NOT_FOUND_ERROR_MESSAGE.equals(e.getMessage()));
  }

  @Test
  void checkSuccess() {
    when(this.userCaptchaMapper.findFirstByUserIdAndCaptchaTypeIdOrderByExpirationDesc(
            this.userId, this.captchaTypeId))
        .thenReturn(
            Optional.of(
                UserCaptcha.builder()
                    .id(1L)
                    .userId(this.userId)
                    .captchaTypeId(this.captchaTypeId)
                    .code(this.code)
                    .expiration(LocalDateTime.now().plusSeconds(600))
                    .build()));
    boolean actual = this.captchaService.check(this.userId, this.code, this.captchaTypeId);
    assertThat(actual).isTrue();
  }

  @Test
  void checkWhenNotHaveCaptcha() {
    when(this.userCaptchaMapper.findFirstByUserIdAndCaptchaTypeIdOrderByExpirationDesc(
            anyLong(), anyLong()))
        .thenReturn(Optional.ofNullable(null));
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class,
            () -> this.captchaService.check(this.userId, this.code, this.captchaTypeId));
    assertThat(exception)
        .matches(e -> ResultCode.CAPTCHA_INVALID.equals(e.getCode()))
        .matches(e -> "given user not has any valid captcha".equals(e.getMessage()));
  }

  @Test
  void checkWithExpiredCaptcha() {
    when(this.userCaptchaMapper.findFirstByUserIdAndCaptchaTypeIdOrderByExpirationDesc(
            this.userId, this.captchaTypeId))
        .thenReturn(
            Optional.of(
                UserCaptcha.builder()
                    .id(1L)
                    .userId(this.userId)
                    .captchaTypeId(this.captchaTypeId)
                    .code(this.code)
                    .expiration(LocalDateTime.now().minusSeconds(1))
                    .build()));
    boolean actual = this.captchaService.check(userId, this.code, captchaTypeId);
    assertThat(actual).isFalse();
  }

  @Test
  void checkWithInvalidCaptcha() {
    when(this.userCaptchaMapper.findFirstByUserIdAndCaptchaTypeIdOrderByExpirationDesc(
            anyLong(), anyLong()))
        .thenReturn(
            Optional.of(
                UserCaptcha.builder()
                    .id(1L)
                    .userId(this.userId)
                    .captchaTypeId(this.captchaTypeId)
                    .code("111111")
                    .build()));
    boolean actual = this.captchaService.check(userId, this.code, captchaTypeId);
    assertThat(actual).isFalse();
  }
}
