package com.github.ksewen.ganyu.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ksewen.ganyu.environment.SystemInformation;
import com.github.ksewen.ganyu.helper.*;

/**
 * @author ksewen
 * @date 12.05.2023 22:32
 */
@Configuration
public class HelperAutoConfiguration {

    @Bean
    public BeanMapperHelpers beanMapperHelpers() {
        return new BeanMapperHelpers();
    }

    @Bean
    public UUIDHelpers uuidHelpers() {
        return new UUIDHelpers();
    }

    @Bean
    public MDCHelpers mdcHelpers(@Autowired SystemInformation systemInformation, @Autowired UUIDHelpers uuidHelpers) {
        return new MDCHelpers(systemInformation, uuidHelpers);
    }

    @Bean
    public JacksonHelpers jacksonHelpers(@Autowired ObjectMapper objectMapper) {
        return new JacksonHelpers(objectMapper);
    }

    @Bean
    public CaptchaHelpers captchaHelpers() {
        return new CaptchaHelpers();
    }
}
