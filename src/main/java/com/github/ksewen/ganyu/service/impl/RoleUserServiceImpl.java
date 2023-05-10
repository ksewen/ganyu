package com.github.ksewen.ganyu.service.impl;

import com.github.ksewen.ganyu.generate.domain.RoleUser;
import com.github.ksewen.ganyu.generate.mapper.RoleUserMapper;
import com.github.ksewen.ganyu.service.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ksewen
 * @date 11.05.2023 00:20
 */
@Service
public class RoleUserServiceImpl implements RoleUserService {

    @Autowired
    private RoleUserMapper roleUserMapper;

    @Override
    public int insert(RoleUser roleUser) {
        return this.roleUserMapper.insert(roleUser);
    }
}
