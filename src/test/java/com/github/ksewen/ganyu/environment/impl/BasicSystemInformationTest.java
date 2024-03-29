package com.github.ksewen.ganyu.environment.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.ganyu.configuration.SystemInformationAutoConfiguration;
import com.github.ksewen.ganyu.environment.SystemInformation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ksewen
 * @date 16.05.2023 12:00
 */
@SpringBootTest(
    classes = {SystemInformationAutoConfiguration.class},
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    properties = {"spring.profiles.active=test", "spring.application.name=ganyu"})
class BasicSystemInformationTest {

  @Autowired private SystemInformation systemInformation;

  @Test
  void getEnvironment() {
    assertThat(this.systemInformation.getEnvironment()).isEqualTo("test");
  }

  @Test
  void getApplicationName() {
    assertThat(this.systemInformation.getApplicationName()).isEqualTo("ganyu");
  }
}
