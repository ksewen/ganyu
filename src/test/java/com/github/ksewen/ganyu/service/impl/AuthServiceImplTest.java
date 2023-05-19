package com.github.ksewen.ganyu.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.domain.Token;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.dto.response.JwtTokenResponse;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.model.JwtUserModel;
import com.github.ksewen.ganyu.model.UserRegisterModel;
import com.github.ksewen.ganyu.service.*;

/**
 * @author ksewen
 * @date 17.05.2023 19:13
 */
// api for testing usage sample
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

    private final String name = "ksewen";
    private final String password = "123456";
    private final String email = "ksewen77@gmail.com";

    @Test
    void registerWhenSuccess() {
        final String userRoleName = "USER";
        final List<Role> roles = Arrays.asList(Role.builder().id(1L).name(userRoleName).build());
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
        assertThat(actual).matches(u -> name.equals(u.getUsername())).matches(u -> name.equals(u.getUsername()))
                .matches(u -> email.equals(u.getEmail())).matches(u -> mobile.equals(u.getMobile()))
                .matches(u -> avatarUrl.equals(u.getAvatarUrl())).matches(u -> encodePassword.equals(u.getPassword()));
    }

    @Test
    void registerWhenInvalidRole() {
        final String name = "ksewen";
        final String password = "123456";
        when(this.roleService.findByNames(any())).thenReturn(null);
        UserRegisterModel registerModel = UserRegisterModel.builder().username(name).email(email).password(password)
                .build();
        CommonException exception = Assertions.assertThrows(CommonException.class,
                () -> this.authService.register(registerModel, "NOT_EXIST"));
        assertThat(exception).matches(e -> ResultCode.NOT_FOUND.equals(e.getCode()))
                .matches(e -> "the given role name invalid".equals(e.getMessage()));
    }

    @Test
    void login() {

        final String accessToken = "accessToken";
        final String refreshToken = "refreshToken";
        when(this.authenticationManager.authenticate(
                argThat(token -> name.equals(token.getPrincipal()) && password.equals(token.getCredentials()))))
                        .thenReturn(new TestingAuthenticationToken(name, password));
        when(this.userDetailsService.loadUserByUsername(name))
                .thenReturn(JwtUserModel.builder().id(1L).username(name).nickname(name).password(password).build());
        ArgumentMatcher<UserDetails> matcher = argument -> name.equals(argument.getUsername())
                && password.equals(argument.getPassword());
        when(this.jwtService.generateToken(argThat(matcher))).thenReturn(accessToken);
        when(this.jwtService.generateRefreshToken(argThat(matcher))).thenReturn(refreshToken);
        when(this.tokenService.save(1L, accessToken))
                .thenReturn(Token.builder().id(1L).userId(1L).token(accessToken).build());
        JwtTokenResponse actual = this.authService.login(name, password);
        assertThat(actual).matches(t -> Objects.equals(1L, t.getId())).matches(t -> accessToken.equals(t.getToken()))
                .matches(t -> refreshToken.equals(t.getRefreshToken()));
        verify(this.tokenService, times(1)).removeAllUserTokens(1L);
    }
}
