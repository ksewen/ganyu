package com.github.ksewen.ganyu.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.generate.domain.RoleUser;
import com.github.ksewen.ganyu.generate.domain.User;
import com.github.ksewen.ganyu.generate.domain.UserExample;
import com.github.ksewen.ganyu.generate.mapper.UserMapper;
import com.github.ksewen.ganyu.model.UserRegisterModel;
import com.github.ksewen.ganyu.service.RoleUserService;
import com.github.ksewen.ganyu.service.UserService;
import org.springframework.util.StringUtils;

/**
 * @author ksewen
 * @date 10.05.2023 23:54
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleUserService roleUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser(UserRegisterModel userRegisterModel, List<Integer> roles) {
        UserExample example = new UserExample();
        example.or().andEmailEqualTo(userRegisterModel.getEmail());
        example.or().andUsernameEqualTo(userRegisterModel.getUsername());
        if (StringUtils.hasLength(userRegisterModel.getMobile())) {
            example.or().andMobileEqualTo(userRegisterModel.getMobile());
        }
        List<User> users = this.userMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(users)) {
            throw new CommonException(ResultCode.ALREADY_EXIST);
        }
        Date now = new Date();
        User user = new User();
        user.setUsername(userRegisterModel.getUsername());
        user.setEmail(userRegisterModel.getEmail());
        user.setNickname(userRegisterModel.getNickname());
        user.setPassword(userRegisterModel.getPassword());
        user.setMobile(userRegisterModel.getMobile());
        user.setAvatarUrl(userRegisterModel.getAvatarUrl());
        user.setCreateTime(now);
        user.setCreateBy(user.getUsername());
        user.setUpdateTime(now);
        user.setUpdateBy(user.getUsername());
        this.userMapper.insert(user);

        //FIXME: insert operation in a loop
        for (Integer role : roles) {
            RoleUser roleUser = new RoleUser();
            roleUser.setUserId(user.getId());
            roleUser.setRoleId(role);
            roleUser.setCreateTime(now);
            roleUser.setCreateBy(user.getUsername());
            roleUser.setUpdateTime(now);
            roleUser.setUpdateBy(user.getUsername());
            this.roleUserService.insert(roleUser);
        }
        return true;
    }

}
