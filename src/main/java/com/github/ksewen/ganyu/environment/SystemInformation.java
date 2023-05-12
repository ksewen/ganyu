package com.github.ksewen.ganyu.environment;

/**
 * @author ksewen
 * @date 12.05.2023 22:35
 */
public interface SystemInformation {

    String getHostName();

    String getHostIp();

    String getEnvironment();

    String getApplicationName();

}
