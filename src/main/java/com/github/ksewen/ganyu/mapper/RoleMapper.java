package com.github.ksewen.ganyu.mapper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.ksewen.ganyu.domain.Role;

/**
 * @author ksewen
 * @date 11.05.2023 08:28
 */
@Repository
public interface RoleMapper extends JpaRepository<Role, Long> {

    @Query("SELECT new Role(r.id, r.name) FROM Role r WHERE r.id in (SELECT ur.roleId FROM UserRole ur WHERE ur.userId = ?1)")
    List<Role> findByUserId(Long userId);

    Role findFirstByName(String name);
}
