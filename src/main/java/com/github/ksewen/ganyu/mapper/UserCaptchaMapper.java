package com.github.ksewen.ganyu.mapper;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.ksewen.ganyu.domain.UserCaptcha;

/**
 * @author ksewen
 * @date 23.05.2023 09:45
 */
@Repository
public interface UserCaptchaMapper extends JpaRepository<UserCaptcha, Long> {

    List<UserCaptcha> findByUserIdAndCaptchaTypeId(Long userId, Long captchaTypeId);

    Optional<UserCaptcha> findFirstByUserIdAndCaptchaTypeIdOrderByExpirationDesc(Long userId, Long captchaTypeId);

    long deleteByUserIdAndCaptchaTypeId(Long userId, Long captchaTypeId);

}
