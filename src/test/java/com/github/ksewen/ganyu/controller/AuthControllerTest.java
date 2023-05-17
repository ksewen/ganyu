package com.github.ksewen.ganyu.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.StringUtils;

import com.github.ksewen.ganyu.GanyuApplicationTests;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.dto.request.UserRegisterRequest;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.helper.JacksonHelpers;
import com.github.ksewen.ganyu.model.UserRegisterModel;
import com.github.ksewen.ganyu.service.AuthService;
import com.github.ksewen.ganyu.service.TokenService;

/**
 * @author ksewen
 * @date 16.05.2023 23:50
 */
//FIXME: remove mock service and create integration test
@SpringBootTest(classes = { GanyuApplicationTests.class, JacksonAutoConfiguration.class, JacksonHelpers.class,
        BeanMapperHelpers.class, AuthController.class })
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonHelpers jacksonHelpers;

    @MockBean
    private AuthService authService;

    @MockBean
    private TokenService tokenService;

    @Test
    void register() throws Exception {
        when(this.authService.register(any(UserRegisterModel.class), anyString())).thenAnswer((Answer<User>) invocationOnMock -> {
            User mockUser = User.builder().id(1L).build();
            for (Object argument : invocationOnMock.getArguments()) {
                if (argument instanceof UserRegisterModel) {
                    UserRegisterModel model = (UserRegisterModel) argument;
                    mockUser.setUsername(model.getUsername());
                    mockUser.setNickname(
                            StringUtils.hasLength(model.getNickname()) ? model.getNickname() : model.getUsername());
                    mockUser.setEmail(model.getEmail());
                    mockUser.setMobile(model.getMobile());
                    mockUser.setAvatarUrl(model.getAvatarUrl());
                    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    mockUser.setPassword(passwordEncoder.encode(model.getPassword()));
                }
            }
            return mockUser;
        });

        UserRegisterRequest registerRequest = UserRegisterRequest.builder().username("ksewen")
                .email("ksewen77@gmail.com").password("123456").build();
        this.mockMvc
                .perform(post("/auth/register").content(this.jacksonHelpers.toJsonString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(20000)).andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.id").value(1)).andExpect(jsonPath("$.data.username").value("ksewen"))
                .andExpect(jsonPath("$.data.nickname").value("ksewen"))
                .andExpect(jsonPath("$.data.email").value("ksewen77@gmail.com"))
                .andExpect(jsonPath("$.data.mobile").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.avatarUrl").value(IsNull.nullValue()));
    }

}
