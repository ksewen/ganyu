package com.github.ksewen.ganyu.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ksewen.ganyu.environment.SystemInformation;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.helper.MDCHelpers;
import com.github.ksewen.ganyu.helper.UUIDHelpers;

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
}
