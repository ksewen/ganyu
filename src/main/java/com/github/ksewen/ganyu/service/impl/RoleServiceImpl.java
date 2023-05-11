package com.github.ksewen.ganyu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.mapper.RoleMapper;
import com.github.ksewen.ganyu.service.RoleService;

import java.util.List;

/**
 * @author ksewen
 * @date 11.05.2023 00:23
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> findByUserId(Long userId) {
        return null;
    }

    @Override
    public Role findFirstByName(String name) {
        return this.roleMapper.findFirstByName(name);
    }
}
