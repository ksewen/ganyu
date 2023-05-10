package com.github.ksewen.ganyu.service.impl;

import com.github.ksewen.ganyu.generate.domain.Role;
import com.github.ksewen.ganyu.generate.domain.RoleExample;
import com.github.ksewen.ganyu.generate.mapper.RoleMapper;
import com.github.ksewen.ganyu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    public List<Role> selectByName(String name) {
        RoleExample example = new RoleExample();
        example.createCriteria().andNameEqualTo(name);
        List<Role> roles = this.roleMapper.selectByExample(example);
        return roles;
    }
}
