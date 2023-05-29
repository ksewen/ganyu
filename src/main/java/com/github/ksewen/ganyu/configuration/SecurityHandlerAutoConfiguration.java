package com.github.ksewen.ganyu.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.github.ksewen.ganyu.helper.JacksonHelpers;
import com.github.ksewen.ganyu.security.handler.CustomAccessDeniedHandler;
import com.github.ksewen.ganyu.security.handler.CustomAuthenticationEntryPoint;

/**
 * @author ksewen
 * @date 15.05.2023 17:31
 */
@Configuration
public class SecurityHandlerAutoConfiguration {

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint(@Autowired JacksonHelpers jacksonHelpers) {
        return new CustomAuthenticationEntryPoint(jacksonHelpers);
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler(@Autowired JacksonHelpers jacksonHelpers) {
        return new CustomAccessDeniedHandler(jacksonHelpers);
    }
}
