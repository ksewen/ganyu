package com.github.ksewen.ganyu.service;

import com.github.ksewen.ganyu.generate.domain.Role;

import java.util.List;

/**
 * @author ksewen
 * @date 11.05.2023 00:23
 */
public interface RoleService {

    List<Role> selectByName(String name);

}
