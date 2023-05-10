package com.github.ksewen.ganyu.service;

import com.github.ksewen.ganyu.domain.UserDomain;
import com.github.ksewen.ganyu.model.UserRegisterModel;

import java.util.List;

/**
 * @author ksewen
 * @date 10.05.2023 23:39
 */
public interface UserService {

    boolean createUser(UserRegisterModel userRegisterModel, List<Integer> roles);

}
