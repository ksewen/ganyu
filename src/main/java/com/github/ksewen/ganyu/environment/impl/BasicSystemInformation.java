package com.github.ksewen.ganyu.environment.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.util.StringUtils;

import com.github.ksewen.ganyu.environment.SystemInformation;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ksewen
 * @date 12.05.2023 22:37
 */
@Slf4j
public class BasicSystemInformation implements SystemInformation {

    private final String UNSET = "unset";

    private final String SYSTEM_ENV_KEY = "spring.profiles.active";

    private final String SYSTEM_APPLICATION_NAME_ID_KEY = "spring.application.name";

    private String hostName;

    private String hostIp;

    private String environment = this.UNSET;

    private String applicationName = this.UNSET;

    @Override
    public String getHostName() {
        return this.hostName;
    }

    @Override
    public String getHostIp() {
        return this.hostIp;
    }

    @Override
    public String getEnvironment() {
        return this.environment;
    }

    @Override
    public String getApplicationName() {
        return this.applicationName;
    }

    private static BasicSystemInformation newInstance() {
        BasicSystemInformation applicationEnvironment = new BasicSystemInformation();
        applicationEnvironment.initHostInfo();
        applicationEnvironment.initEnvironment();
        applicationEnvironment.initApplicationName();
        return applicationEnvironment;
    }

    private void initHostInfo() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            this.hostName = address.getHostName();
            this.hostIp = address.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("Can not init hostName and hostIp", e);
        }
    }

    private void initEnvironment() {
        String env = System.getProperty(SYSTEM_ENV_KEY);
        if (StringUtils.hasLength(env)) {
            this.environment = env;
        }
    }

    private void initApplicationName() {
        String applicationName = System.getProperty(SYSTEM_APPLICATION_NAME_ID_KEY);
        if (StringUtils.hasLength(applicationName)) {
            this.applicationName = applicationName;
        }
    }

    public final static class SingletonHolder {
        public static final BasicSystemInformation instance = BasicSystemInformation.newInstance();
    }

    public static BasicSystemInformation getInstance() {
        return SingletonHolder.instance;
    }
}
