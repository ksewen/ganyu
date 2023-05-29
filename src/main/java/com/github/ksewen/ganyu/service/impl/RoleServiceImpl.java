package com.github.ksewen.ganyu.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.mapper.RoleMapper;
import com.github.ksewen.ganyu.service.RoleService;

import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 11.05.2023 00:23
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    public List<Role> findByUserId(Long userId) {
        return this.roleMapper.findByUserId(userId);
    }

    public Role findFirstByName(String name) {
        return this.roleMapper.findFirstByName(name);
    }

    public List<Role> findByNames(String... name) {
        return this.roleMapper.findByNameIn(Arrays.asList(name));
    }
}
