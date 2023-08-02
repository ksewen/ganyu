package com.github.ksewen.ganyu.helper;

import com.github.ksewen.ganyu.environment.SystemInformation;
import com.google.common.annotations.VisibleForTesting;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

/**
 * @author ksewen
 * @date 12.05.2023 22:29
 */
@Slf4j
@AllArgsConstructor
public class MDCHelpers implements AutoCloseable {

  @VisibleForTesting public final String REQUEST_ID_KEY = "requestId";

  @VisibleForTesting public final String HOSTNAME = "hostname";

  @VisibleForTesting public final String IP = "ip";

  @VisibleForTesting public final String ENVIRONMENT = "environment";

  @VisibleForTesting public final String APPLICATION_NAME = "applicationName";

  private final SystemInformation systemInformation;
  private final UUIDHelpers uuidHelpers;

  public void init() {
    this.initRequestId();
    this.initHostname();
    this.initIP();
    this.initEnvironment();
    this.initApplicationName();
  }

  public void initRequestId() {
    String requestId = this.getRequestId();
    if (!StringUtils.hasLength(requestId)) {
      this.put(this.REQUEST_ID_KEY, this.uuidHelpers.getShortUUID());
    }
  }

  public String getRequestId() {
    return this.getByKey(this.REQUEST_ID_KEY);
  }

  public void initHostname() {
    String hostname = this.getHostname();
    if (!StringUtils.hasLength(hostname)) {
      this.put(this.HOSTNAME, this.systemInformation.getHostName());
    }
  }

  public String getHostname() {
    return this.getByKey(this.HOSTNAME);
  }

  public void initIP() {
    String ip = this.getIP();
    if (!StringUtils.hasLength(ip)) {
      this.put(this.IP, this.systemInformation.getHostIp());
    }
  }

  public String getIP() {
    return this.getByKey(this.IP);
  }

  public void initEnvironment() {
    String environment = this.getEnvironment();
    if (!StringUtils.hasLength(environment)) {
      this.put(this.ENVIRONMENT, this.systemInformation.getEnvironment());
    }
  }

  public String getEnvironment() {
    return this.getByKey(this.ENVIRONMENT);
  }

  public void initApplicationName() {
    String applicationName = this.getApplicationName();
    if (!StringUtils.hasLength(applicationName)) {
      this.put(this.APPLICATION_NAME, this.systemInformation.getApplicationName());
    }
  }

  public String getApplicationName() {
    return this.getByKey(this.APPLICATION_NAME);
  }

  public boolean alreadyFinish() {
    return StringUtils.hasLength(this.getRequestId())
        && StringUtils.hasLength(this.getHostname())
        && StringUtils.hasLength(this.getIP())
        && StringUtils.hasLength(this.getEnvironment())
        && StringUtils.hasLength(this.getApplicationName());
  }

  public void put(String key, String value) {
    MDC.put(key, value);
  }

  public String getByKey(String key) {
    return MDC.get(key);
  }

  @Override
  public void close() {
    MDC.clear();
  }
}
