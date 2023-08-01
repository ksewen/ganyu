package com.github.ksewen.ganyu.service.impl;

import com.github.ksewen.ganyu.domain.CaptchaType;
import com.github.ksewen.ganyu.mapper.CaptchaTypeMapper;
import com.github.ksewen.ganyu.service.CaptchaTypeService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author ksewen
 * @date 29.05.2023 15:45
 */
@Service
@RequiredArgsConstructor
public class CaptchaTypeServiceImpl implements CaptchaTypeService {

  private final CaptchaTypeMapper captchaTypeMapper;

  @Override
  public Optional<CaptchaType> findById(Long id) {
    return this.captchaTypeMapper.findById(id);
  }

  @Override
  public Optional<CaptchaType> findByName(String name) {
    return this.captchaTypeMapper.findByName(name);
  }
}
