package com.github.ksewen.ganyu.mapper;

import com.github.ksewen.ganyu.domain.UserRole;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ksewen
 * @date 11.05.2023 08:29
 */
@Repository
public interface UserRoleMapper extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserId(Long userId);
}
