package com.github.ksewen.ganyu.service;

import java.util.List;
import java.util.Optional;

import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.model.UserModifyModel;
import com.github.ksewen.ganyu.model.UserRegisterModel;

/**
 * @author ksewen
 * @date 10.05.2023 23:39
 */
public interface UserService {

    Optional<User> findByUsername(String username);

    Optional<User> findById(long userId);
    User add(UserRegisterModel userRegisterModel, List<Role> roles);
    User modify(UserModifyModel userModifyModel, long operationUserId);
    User modifyPassword(String exist, String modify, long userId);
}
