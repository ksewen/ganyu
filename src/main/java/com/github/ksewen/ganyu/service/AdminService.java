package com.github.ksewen.ganyu.service;

import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.model.UserModifyModel;

/**
 * @author ksewen
 * @date 15.05.2023 16:26
 */
public interface AdminService {

  User modify(UserModifyModel userRegisterModel, long operationUserId);
}
