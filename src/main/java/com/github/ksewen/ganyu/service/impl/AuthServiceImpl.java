package com.github.ksewen.ganyu.service.impl;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.domain.Token;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.dto.response.JwtTokenResponse;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.model.JwtTokenModel;
import com.github.ksewen.ganyu.model.JwtUserModel;
import com.github.ksewen.ganyu.model.UserRegisterModel;
import com.github.ksewen.ganyu.service.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author ksewen
 * @date 10.05.2023 16:07
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserService userService;

  private final RoleService roleService;

  private final JwtService jwtService;

  private final TokenService tokenService;

  private final UserDetailsService userDetailsService;

  private final AuthenticationManager authenticationManager;

  private final PasswordEncoder passwordEncoder;

  @Override
  public User register(UserRegisterModel userRegisterModel, String... roleNames) {
    List<Role> roles = this.roleService.findByNames(roleNames);
    if (CollectionUtils.isEmpty(roles)) {
      throw new CommonException(ResultCode.NOT_FOUND, "the given role name invalid");
    }
    userRegisterModel.setPassword(this.passwordEncoder.encode(userRegisterModel.getPassword()));
    if (!StringUtils.hasLength(userRegisterModel.getNickname())) {
      userRegisterModel.setNickname(userRegisterModel.getUsername());
    }
    return this.userService.add(userRegisterModel, roles);
  }

  @Override
  public JwtTokenResponse login(String username, String password) {
    UsernamePasswordAuthenticationToken upToken =
        new UsernamePasswordAuthenticationToken(username, password);
    final Authentication authentication = this.authenticationManager.authenticate(upToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    final JwtUserModel userDetails =
        (JwtUserModel) this.userDetailsService.loadUserByUsername(username);
    final JwtTokenModel token = this.jwtService.generateToken(userDetails);
    final JwtTokenModel refreshToken = this.jwtService.generateRefreshToken(userDetails);
    this.tokenService.removeAllUserTokens(userDetails.getId());
    Token save = this.tokenService.save(userDetails.getId(), token.getToken());
    return JwtTokenResponse.builder()
        .id(save.getId())
        .userId(userDetails.getId())
        .username(userDetails.getUsername())
        .token(save.getToken())
        .refreshToken(refreshToken.getToken())
        .expireAt(refreshToken.getExpireAt())
        .build();
  }

  @Override
  public void logout(Long userId) {
    this.tokenService.removeAllUserTokens(userId);
  }
}
