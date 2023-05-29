package com.github.ksewen.ganyu.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.ksewen.ganyu.domain.UserRole;
import com.github.ksewen.ganyu.mapper.UserRoleMapper;
import com.github.ksewen.ganyu.service.UserRoleService;

import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 11.05.2023 00:20
 */
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleMapper userRoleMapper;

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
