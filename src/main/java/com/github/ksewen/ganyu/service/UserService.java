package com.github.ksewen.ganyu.service;

import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.model.UserRegisterModel;

import java.util.List;

/**
 * @author ksewen
 * @date 10.05.2023 23:39
 */
public interface UserService {

    User findFirstByUsername(String username);
    boolean createUser(UserRegisterModel userRegisterModel, List<Role> roles);

}
