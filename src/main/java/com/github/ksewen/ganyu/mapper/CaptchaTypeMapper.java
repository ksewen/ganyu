package com.github.ksewen.ganyu.mapper;

import com.github.ksewen.ganyu.domain.CaptchaType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ksewen
 * @date 23.05.2023 09:44
 */
@Repository
public interface CaptchaTypeMapper extends JpaRepository<CaptchaType, Long> {

    Optional<CaptchaType> findByName(String name);
}
