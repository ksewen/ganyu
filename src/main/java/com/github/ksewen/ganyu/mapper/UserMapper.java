package com.github.ksewen.ganyu.mapper;

import com.github.ksewen.ganyu.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author ksewen
 * @date 11.05.2023 08:27
 */
@Repository
public interface UserMapper extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

  Optional<User> findByUsername(String username);
}
