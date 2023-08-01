package com.github.ksewen.ganyu.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.github.ksewen.ganyu.configuration.exception.InvalidParamException;
import com.github.ksewen.ganyu.domain.Token;
import com.github.ksewen.ganyu.dto.response.JwtTokenResponse;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.mapper.TokenMapper;
import com.github.ksewen.ganyu.model.JwtTokenModel;
import com.github.ksewen.ganyu.model.JwtUserModel;
import com.github.ksewen.ganyu.service.JwtService;
import com.github.ksewen.ganyu.service.TokenService;
import java.time.LocalDateTime;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author ksewen
 * @date 18.05.2023 10:53
 */
@SpringBootTest(classes = TokenServiceImpl.class)
class TokenServiceImplTest {

  @Autowired private TokenService tokenService;
  @MockBean private UserDetailsService userDetailsService;

  @MockBean private TokenMapper tokenMapper;

  private final String username = "ksewen";

  @MockBean private JwtService jwtService;

  @Test
  void refreshSuccess() {
    LocalDateTime localDateTime = LocalDateTime.now();
    final String refreshToken = "valid_token";
    final JwtTokenModel tokenModel =
        JwtTokenModel.builder().token("accessToken").expireAt(localDateTime).build();
    when(this.jwtService.extractUsername(refreshToken)).thenReturn(this.username);
    when(this.userDetailsService.loadUserByUsername(this.username))
        .thenReturn(
            JwtUserModel.builder().id(1L).username(this.username).nickname(this.username).build());
    when(this.jwtService.validateToken(anyString(), any(UserDetails.class))).thenReturn(true);
    when(this.jwtService.generateToken(any(UserDetails.class))).thenReturn(tokenModel);
    when(this.tokenMapper.save(
            argThat(
                token ->
                    token.getUserId().equals(1L)
                        && token.getToken().equals(tokenModel.getToken()))))
        .thenReturn(Token.builder().id(1L).token(tokenModel.getToken()).userId(1L).build());
    JwtTokenResponse actual = this.tokenService.refresh(refreshToken);
    assertThat(actual)
        .matches(t -> Objects.equals(1L, t.getId()))
        .matches(t -> t.getToken().equals(tokenModel.getToken()))
        .matches(t -> t.getRefreshToken().equals(refreshToken))
        .matches(t -> this.username.equals(t.getUsername()));
  }

  @Test
  void refreshWithInvalidToken() {
    final String refreshToken = "invalid_token";
    when(this.jwtService.extractUsername(anyString())).thenReturn(null);
    InvalidParamException exception =
        Assertions.assertThrows(
            InvalidParamException.class, () -> this.tokenService.refresh(refreshToken));
    assertThat(exception)
        .matches(e -> ResultCode.PARAM_INVALID.equals(e.getCode()))
        .matches(e -> "invalid refresh_token".equals(e.getMessage()));
  }
}
