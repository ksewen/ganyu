package com.github.ksewen.ganyu.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.ksewen.ganyu.domain.BusinessType;
import com.github.ksewen.ganyu.mapper.BusinessTypeMapper;
import com.github.ksewen.ganyu.service.BusinessTypeService;

import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 31.05.2023 14:57
 */
@Service
@RequiredArgsConstructor
public class BusinessTypeImpl implements BusinessTypeService {

    private final BusinessTypeMapper businessTypeMapper;

    @Override
    public BusinessType save(BusinessType businessType) {
        return this.businessTypeMapper.saveAndFlush(businessType);
    }

    @Override
    public void delete(Long id) {
        this.businessTypeMapper.deleteById(id);
    }

    @Override
    public Page<BusinessType> findAll(int index, int count) {
        Pageable pageable = PageRequest.of(index, count);
        return this.businessTypeMapper.findAll(pageable);
    }


}
