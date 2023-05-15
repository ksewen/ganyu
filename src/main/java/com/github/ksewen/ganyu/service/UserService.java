package com.github.ksewen.ganyu.service;

import java.util.List;
import java.util.Optional;

import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.model.UserEditModel;
import com.github.ksewen.ganyu.model.UserRegisterModel;

/**
 * @author ksewen
 * @date 10.05.2023 23:39
 */
public interface UserService {

    Optional<User> findByUsername(String username);
    User add(UserRegisterModel userRegisterModel, List<Role> roles);

    User edit(UserEditModel userEditModel, long operationUserId);

}
