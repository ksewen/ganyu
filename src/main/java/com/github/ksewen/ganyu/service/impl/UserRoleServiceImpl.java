package com.github.ksewen.ganyu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.ksewen.ganyu.domain.UserRole;
import com.github.ksewen.ganyu.mapper.UserRoleMapper;
import com.github.ksewen.ganyu.service.UserRoleService;

import java.util.List;

/**
 * @author ksewen
 * @date 11.05.2023 00:20
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<UserRole> findByUserId(Long userId) {
        return this.userRoleMapper.findByUserId(userId);
    }

    @Override
    public UserRole saveAndFlush(UserRole userRole) {
        return this.userRoleMapper.saveAndFlush(userRole);
    }

    @Override
    public List<UserRole> saveAllAndFlush(List<UserRole> userRoles) {
        return this.userRoleMapper.saveAllAndFlush(userRoles);
    }
}
