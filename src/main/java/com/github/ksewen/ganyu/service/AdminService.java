package com.github.ksewen.ganyu.service;

import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.model.UserRegisterModel;

/**
 * @author ksewen
 * @date 15.05.2023 16:26
 */
public interface AdminService {

    User modify(UserRegisterModel userRegisterModel);

}
