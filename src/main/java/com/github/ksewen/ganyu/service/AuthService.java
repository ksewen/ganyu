package com.github.ksewen.ganyu.service;

import com.github.ksewen.ganyu.model.UserRegisterModel;

/**
 * @author ksewen
 * @date 10.05.2023 13:38
 */
public interface AuthService {

    boolean register (UserRegisterModel registerModel);

    String login(String email, String password);

    String refresh(String oldToken);

}
