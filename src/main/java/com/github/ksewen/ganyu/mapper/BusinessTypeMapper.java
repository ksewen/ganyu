package com.github.ksewen.ganyu.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.ksewen.ganyu.domain.BusinessType;

/**
 * @author ksewen
 * @date 31.05.2023 14:56
 */
@Repository
public interface BusinessTypeMapper extends JpaRepository<BusinessType, Long> {
}
