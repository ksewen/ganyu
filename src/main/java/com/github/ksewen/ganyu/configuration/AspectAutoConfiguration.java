package com.github.ksewen.ganyu.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.ksewen.ganyu.aspect.LogTraceAspect;

/**
 * @author ksewen
 * @date 13.05.2023 10:49
 */
@Configuration
public class AspectAutoConfiguration {

    @Bean
    public LogTraceAspect logTraceAspect() {
        return new LogTraceAspect();
    }

}
