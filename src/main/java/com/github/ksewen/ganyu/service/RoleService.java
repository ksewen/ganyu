package com.github.ksewen.ganyu.service;

import java.util.List;

import com.github.ksewen.ganyu.domain.Role;

/**
 * @author ksewen
 * @date 11.05.2023 00:23
 */
public interface RoleService {

    List<Role> findByUserId(Long userId);

    Role findFirstByName(String name);
    List<Role> findByNames(String... name);

}
