package com.github.ksewen.ganyu.service;

import com.github.ksewen.ganyu.domain.Role;
import java.util.List;

/**
 * @author ksewen
 * @date 11.05.2023 00:23
 */
public interface RoleService {

  List<Role> findByUserId(Long userId);

  Role findFirstByName(String name);

  List<Role> findByNames(String... name);

  void checkAdministrator(long userId);
}
