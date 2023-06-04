package com.github.ksewen.ganyu.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.constant.AuthenticationConstants;
import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.enums.ResultCode;
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

    @Override
    public void checkAdministrator(long userId) {
        List<Role> operationRoles = this.findByUserId(userId);
        boolean access = !CollectionUtils.isEmpty(operationRoles) && operationRoles.stream()
                .anyMatch(r -> AuthenticationConstants.ADMIN_ROLE_NAME.equals(r.getName()));
        if (!access) {
            throw new CommonException(ResultCode.ACCESS_DENIED,
                    "only administrator can edit other user information");
        }
    }
}
