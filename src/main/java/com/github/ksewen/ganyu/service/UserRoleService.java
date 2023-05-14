package com.github.ksewen.ganyu.service;

import java.util.List;

import com.github.ksewen.ganyu.domain.UserRole;

/**
 * @author ksewen
 * @date 11.05.2023 00:19
 */
public interface UserRoleService {

    List<UserRole> findByUserId(Long userId);

    UserRole saveAndFlush(UserRole userRole);

    List<UserRole> saveAllAndFlush(List<UserRole> userRoles);

}
