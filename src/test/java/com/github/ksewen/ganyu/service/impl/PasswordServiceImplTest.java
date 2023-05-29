package com.github.ksewen.ganyu.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.constant.ErrorMessageConstants;
import com.github.ksewen.ganyu.domain.CaptchaType;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.service.CaptchaService;
import com.github.ksewen.ganyu.service.CaptchaTypeService;
import com.github.ksewen.ganyu.service.PasswordService;
import com.github.ksewen.ganyu.service.UserService;

/**
 * @author ksewen
 * @date 29.05.2023 15:56
 */
@SpringBootTest(classes = PasswordServiceImpl.class)
class PasswordServiceImplTest {

    @Autowired
    private PasswordService passwordService;

    @MockBean
    private UserService userService;

    @MockBean
    private CaptchaService captchaService;

    @MockBean
    private CaptchaTypeService captchaTypeService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private final String name = "ksewen";

    private final String email = "ksewen77@gmail.com";

    private final String password = "encodePassword";

    private final String modifyPassword = "modifyPassword";
    private final String modifyEncodedPassword = "modifyEncodedPassword";

    private final String captcha = "111111";

    @Test
    void modifySuccess() {
        when(this.userService.findById(anyLong())).thenReturn(Optional.of(User.builder().id(1L).username(this.name)
                .nickname(this.name).email(this.email).password(this.password).build()));
        when(this.passwordEncoder.matches(anyString(),
                argThat(encodedPassword -> this.password.equals(encodedPassword)))).thenReturn(Boolean.TRUE);
        when(this.passwordEncoder.encode(modifyPassword)).thenReturn(modifyEncodedPassword);
        when(this.userService.saveAndFlush(
                argThat(user -> this.name.equals(user.getUsername()) && this.name.equals(user.getNickname())
                        && this.email.equals(user.getEmail()) && modifyEncodedPassword.equals(user.getPassword()))))
                                .thenReturn(User.builder().id(1L).username(this.name).nickname(this.name)
                                        .email(this.email).password(modifyEncodedPassword).build());
        this.passwordService.modify(this.password, modifyPassword, 1L);
        verify(this.userService, times(1)).findById(1L);
        verify(this.passwordEncoder, times(1)).matches(this.password, this.password);
        verify(this.userService, times(1)).saveAndFlush(
                argThat(user -> this.name.equals(user.getUsername()) && this.name.equals(user.getNickname())
                        && this.email.equals(user.getEmail()) && modifyEncodedPassword.equals(user.getPassword())));
    }

    @Test
    void modifyWithWrongOldPassword() {
        when(this.userService.findById(anyLong())).thenReturn(Optional.of(User.builder().id(1L).username(this.name)
                .nickname(this.name).email(this.email).password(this.password).build()));
        when(this.passwordEncoder.matches(anyString(),
                argThat(encodedPassword -> this.password.equals(encodedPassword)))).thenReturn(Boolean.FALSE);
        CommonException exception = Assertions.assertThrows(CommonException.class,
                () -> this.passwordService.modify(this.password, modifyPassword, 1L));
        assertThat(exception).matches(e -> ResultCode.ACCESS_DENIED.equals(e.getCode()))
                .matches(e -> "invalid old password".equals(e.getMessage()));
    }

    @Test
    void modifyWithNotExistUser() {
        when(this.userService.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        CommonException exception = Assertions.assertThrows(CommonException.class,
                () -> this.passwordService.modify(this.password, modifyPassword, 1L));
        assertThat(exception).matches(e -> ResultCode.NOT_FOUND.equals(e.getCode()))
                .matches(e -> ErrorMessageConstants.USER_NOT_FOUND_ERROR_MESSAGE.equals(e.getMessage()));
    }

    @Test
    void resetSuccess() {
        when(this.captchaTypeService.findByName(anyString()))
                .thenReturn(Optional.of(CaptchaType.builder().id(1L).build()));
        when(this.captchaService.check(anyLong(), anyString(), eq(1L))).thenReturn(Boolean.TRUE);
        when(this.userService.findById(1L)).thenReturn(Optional.of(User.builder().id(1L).username(this.name)
                .nickname(this.name).email(this.email).password("forgotpassword").build()));
        when(this.passwordEncoder.encode(this.modifyPassword)).thenReturn(this.modifyEncodedPassword);
        this.passwordService.reset(this.captcha, this.modifyPassword, 1L);
        verify(this.userService, times(1)).saveAndFlush(argThat(u -> Objects.equals(1L, u.getId())
                && this.name.equals(u.getUsername()) && this.name.equals(u.getNickname())
                && this.email.equals(u.getEmail()) && this.modifyEncodedPassword.equals(u.getPassword())));
    }

    @Test
    void resetWithInvalidCaptcha() {
        when(this.captchaTypeService.findByName(anyString()))
                .thenReturn(Optional.of(CaptchaType.builder().id(1L).build()));
        when(this.captchaService.check(anyLong(), anyString(), eq(1L))).thenReturn(Boolean.FALSE);
        CommonException exception = Assertions.assertThrows(CommonException.class,
                () -> this.passwordService.reset(this.captcha, this.modifyPassword, 1L));
        assertThat(exception).matches(e -> ResultCode.CAPTCHA_INVALID.equals(e.getCode()))
                .matches(e -> ResultCode.CAPTCHA_INVALID.getMessage().equals(e.getMessage()));
    }

    @Test
    void resetWithNotExistUser() {
        when(this.captchaTypeService.findByName(anyString()))
                .thenReturn(Optional.of(CaptchaType.builder().id(1L).build()));
        when(this.captchaService.check(anyLong(), anyString(), eq(1L))).thenReturn(Boolean.TRUE);
        when(this.userService.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        CommonException exception = Assertions.assertThrows(CommonException.class,
                () -> this.passwordService.reset(this.captcha, this.modifyPassword, 1L));
        assertThat(exception).matches(e -> ResultCode.NOT_FOUND.equals(e.getCode()))
                .matches(e -> ErrorMessageConstants.USER_NOT_FOUND_ERROR_MESSAGE.equals(e.getMessage()));
    }
}
