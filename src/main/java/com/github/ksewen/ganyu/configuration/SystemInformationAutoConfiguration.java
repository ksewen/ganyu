package com.github.ksewen.ganyu.configuration;

import com.github.ksewen.ganyu.environment.SystemInformation;
import com.github.ksewen.ganyu.environment.impl.BasicSystemInformation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ksewen
 * @date 12.05.2023 22:43
 */
@Configuration
public class SystemInformationAutoConfiguration {

    @Bean
    public SystemInformation systemInformation() {
        return BasicSystemInformation.getInstance();
    }
}
