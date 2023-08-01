package com.github.ksewen.ganyu.service;

import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.dto.response.JwtTokenResponse;
import com.github.ksewen.ganyu.model.UserRegisterModel;

/**
 * @author ksewen
 * @date 10.05.2023 13:38
 */
public interface AuthService {

  User register(UserRegisterModel userRegisterModel, String... roleNames);

  JwtTokenResponse login(String username, String password);

  void logout(Long userId);
}
