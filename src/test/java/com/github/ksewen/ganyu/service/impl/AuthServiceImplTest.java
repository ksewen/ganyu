package com.github.ksewen.ganyu.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.model.UserRegisterModel;
import com.github.ksewen.ganyu.service.*;

/**
 * @author ksewen
 * @date 17.05.2023 19:13
 */
@SpringBootTest(classes = { AuthServiceImpl.class, BCryptPasswordEncoder.class })
class AuthServiceImplTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void register() {
        final String userRoleName = "USER";
        final List<Role> roles = Arrays.asList(Role.builder().id(1L).name(userRoleName).build());
        final String name = "ksewen";
        final String email = "ksewen77@gmail.com";
        final String password = "123456";
        final String mobile = "5050107777";
        final String avatarUrl = "http://www.ganyu.com";
        final String encodePassword = "encodePassword";
        when(this.roleService.findByNames(userRoleName)).thenReturn(roles);
        when(this.userService.add(argThat(userRegisterModel -> name.equals(userRegisterModel.getUsername())
                && name.equals(userRegisterModel.getNickname()) && email.equals(userRegisterModel.getEmail())
                && mobile.equals(userRegisterModel.getMobile()) && avatarUrl.equals(userRegisterModel.getAvatarUrl())
                && StringUtils.hasLength(userRegisterModel.getPassword())),
                argThat(roles1 -> !CollectionUtils.isEmpty(roles1) && roles1.size() == 1
                        && userRoleName.equals(roles1.get(0).getName()))))
                                .thenAnswer((Answer<User>) invocationOnMock -> {
                                    User mockUser = User.builder().id(1L).build();
                                    for (Object argument : invocationOnMock.getArguments()) {
                                        if (argument instanceof UserRegisterModel) {
                                            UserRegisterModel model = (UserRegisterModel) argument;
                                            mockUser.setUsername(model.getUsername());
                                            mockUser.setNickname(model.getNickname());
                                            mockUser.setEmail(model.getEmail());
                                            mockUser.setMobile(model.getMobile());
                                            mockUser.setAvatarUrl(model.getAvatarUrl());
                                            mockUser.setPassword(this.passwordEncoder.encode(model.getPassword()));
                                        }
                                    }
                                    return mockUser;
                                });
        when(this.passwordEncoder.encode(any(String.class))).thenReturn(encodePassword);

        UserRegisterModel registerModel = UserRegisterModel.builder().username(name).email(email).password(password)
                .mobile(mobile).avatarUrl(avatarUrl).build();
        User actual = this.authService.register(registerModel, userRoleName);
        assertThat(actual).matches(u -> Objects.equals(u.getUsername(), name));
        assertThat(actual).matches(u -> Objects.equals(u.getNickname(), name));
        assertThat(actual).matches(u -> Objects.equals(u.getEmail(), email));
        assertThat(actual).matches(u -> Objects.equals(u.getMobile(), mobile));
        assertThat(actual).matches(u -> Objects.equals(u.getAvatarUrl(), avatarUrl));
        assertThat(actual).matches(u -> Objects.equals(u.getPassword(), encodePassword));
    }
}
