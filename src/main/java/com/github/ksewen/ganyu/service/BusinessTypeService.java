package com.github.ksewen.ganyu.service;

import com.github.ksewen.ganyu.domain.BusinessType;
import org.springframework.data.domain.Page;

/**
 * @author ksewen
 * @date 31.05.2023 14:56
 */
public interface BusinessTypeService {

  BusinessType save(BusinessType businessType);

  void delete(Long id);

  Page<BusinessType> findAll(int currentIndex, int pageCount);
}
