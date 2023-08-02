package com.github.ksewen.ganyu.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.github.ksewen.ganyu.environment.SystemInformation;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author ksewen
 * @date 02.08.2023 11:46
 */
@SpringBootTest(classes = MDCHelpers.class)
class MDCHelpersTest {

  @Autowired private MDCHelpers mdcHelpers;

  @MockBean SystemInformation systemInformation;

  @MockBean UUIDHelpers uuidHelpers;

  private String MOCK_UUID = "mock-short-uuid";

  private String MOCK_HOSTNAME = "mock-hostname";

  private String MOCK_IP = "mock-ip";

  private String MOCK_ENVIRONMENT = "mock-environment";

  private String MOCK_APPLICATION_NAME = "mock-application-name";

  @Test
  void initRequestId() {
    when(this.uuidHelpers.getShortUUID()).thenReturn(this.MOCK_UUID);

    String requestId = this.mdcHelpers.getRequestId();
    assertThat(requestId).isNull();
    this.mdcHelpers.initRequestId();
    String result = MDC.get(this.mdcHelpers.REQUEST_ID_KEY);
    assertThat(result).isEqualTo(this.MOCK_UUID);
  }

  @Test
  void initHostname() {
    when(this.systemInformation.getHostName()).thenReturn(this.MOCK_HOSTNAME);

    String hostname = this.mdcHelpers.getHostname();
    assertThat(hostname).isNull();
    this.mdcHelpers.initHostname();
    String result = MDC.get(this.mdcHelpers.HOSTNAME);
    assertThat(result).isEqualTo(this.MOCK_HOSTNAME);
  }

  @Test
  void initIP() {
    when(this.systemInformation.getHostIp()).thenReturn(this.MOCK_IP);

    String ip = this.mdcHelpers.getIP();
    assertThat(ip).isNull();
    this.mdcHelpers.initIP();
    String result = MDC.get(this.mdcHelpers.IP);
    assertThat(result).isEqualTo(this.MOCK_IP);
  }

  @Test
  void initEnvironment() {
    when(this.systemInformation.getEnvironment()).thenReturn(this.MOCK_ENVIRONMENT);

    String environment = this.mdcHelpers.getEnvironment();
    assertThat(environment).isNull();
    this.mdcHelpers.initEnvironment();
    String result = MDC.get(this.mdcHelpers.ENVIRONMENT);
    assertThat(result).isEqualTo(this.MOCK_ENVIRONMENT);
  }

  @Test
  void initApplicationName() {
    when(this.systemInformation.getApplicationName()).thenReturn(this.MOCK_APPLICATION_NAME);

    String applicationName = this.mdcHelpers.getApplicationName();
    assertThat(applicationName).isNull();
    this.mdcHelpers.initApplicationName();
    String result = MDC.get(this.mdcHelpers.APPLICATION_NAME);
    assertThat(result).isEqualTo(this.MOCK_APPLICATION_NAME);
  }

  @Test
  void put() {
    String key = "key";
    String value = "value";
    this.mdcHelpers.put(key, value);
    String result = MDC.get(key);
    assertThat(result).isEqualTo(value);
  }

  @Test
  void close() {
    MDC.put("test", "value");
    this.mdcHelpers.close();
    String result = MDC.get("test");
    assertThat(result).isNull();
  }
}
