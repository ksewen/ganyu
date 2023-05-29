package com.github.ksewen.ganyu.service;

import java.util.Optional;

import com.github.ksewen.ganyu.domain.CaptchaType;

/**
 * @author ksewen
 * @date 29.05.2023 15:41
 */
public interface CaptchaTypeService {

    Optional<CaptchaType> findById(Long id);

    Optional<CaptchaType> findByName(String name);
}
