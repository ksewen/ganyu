package com.github.ksewen.ganyu.service;

import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.dto.auth.JwtTokenResponse;
import com.github.ksewen.ganyu.model.UserRegisterModel;

/**
 * @author ksewen
 * @date 10.05.2023 13:38
 */
public interface AuthService {

    User register (UserRegisterModel registerModel);

    JwtTokenResponse login(String username, String password);

}
