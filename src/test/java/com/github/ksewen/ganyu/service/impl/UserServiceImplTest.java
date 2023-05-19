package com.github.ksewen.ganyu.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.CollectionUtils;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.mapper.UserMapper;
import com.github.ksewen.ganyu.mapper.specification.UserSpecification;
import com.github.ksewen.ganyu.model.UserRegisterModel;
import com.github.ksewen.ganyu.service.RoleService;
import com.github.ksewen.ganyu.service.UserRoleService;
import com.github.ksewen.ganyu.service.UserService;

/**
 * @author ksewen
 * @date 18.05.2023 10:54
 */
@SpringBootTest(classes = { UserServiceImpl.class, BeanMapperHelpers.class })
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserRoleService userRoleService;

    @MockBean
    private RoleService roleService;

    private final String name = "ksewen";

    private final String email = "ksewen77@gmail.com";

    private final String password = "encodePassword";

    @Test
    void addSuccess() {
        final List<Role> roles = Arrays.asList(Role.builder().id(1L).name("ADMIN").build(),
                Role.builder().id(2L).name("USER").build());
        when(this.userMapper.findOne(any(UserSpecification.class))).thenReturn(Optional.ofNullable(null));
        when(this.userMapper.saveAndFlush(
                argThat(user -> this.name.equals(user.getUsername()) && this.name.equals(user.getNickname())
                        && this.email.equals(user.getEmail()) && password.equals(user.getPassword()))))
                                .thenReturn(User.builder().id(1L).username(this.name).nickname(this.name)
                                        .email(this.email).password(password).build());
        User actual = this.userService.add(generateUser(), roles);
        assertThat(actual).matches(u -> this.name.equals(u.getUsername()))
                .matches(u -> this.name.equals(u.getNickname())).matches(u -> this.email.equals(u.getEmail()))
                .matches(u -> password.equals(u.getPassword()));
        assertThat(actual.getMobile()).isNull();
        assertThat(actual.getAvatarUrl()).isNull();
        verify(this.userRoleService, times(1))
                .saveAllAndFlush(argThat(list -> !CollectionUtils.isEmpty(list) && list.size() == roles.size()
                        && Objects.equals(1L, list.get(0).getUserId()) && Objects.equals(1L, list.get(0).getRoleId())
                        && Objects.equals(1L, list.get(1).getUserId()) && Objects.equals(2L, list.get(1).getRoleId())));
    }

    @Test
    void addWithExistUser() {
        when(this.userMapper.findOne(any(UserSpecification.class))).thenReturn(
                Optional.of(User.builder().id(1L).username(this.name).nickname(this.name).email(this.email).build()));
        CommonException exception = Assertions.assertThrows(CommonException.class, () -> {
            this.userService.add(generateUser(), null);
        });
        assertThat(exception).matches(e -> ResultCode.ALREADY_EXIST.equals(e.getCode()))
                .matches(e -> ResultCode.ALREADY_EXIST.getMessage().equals(e.getMessage()));
    }

    @Test
    void edit() {

    }

    private UserRegisterModel generateUser() {
        return UserRegisterModel.builder().username(this.name).nickname(this.name).email(this.email).password(password)
                .build();
    }
}
