package com.github.ksewen.ganyu.helper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ksewen
 * @date 02.08.2023 01:46
 */
@SpringBootTest(classes = UUIDHelpers.class)
class UUIDHelpersTest {

  @Autowired private UUIDHelpers uuidHelpers;

  @Test
  void getShortUUID() {
    String result = this.uuidHelpers.getShortUUID();
    assertThat(result).hasSize(32);
  }

  @Test
  void testGetShortUUID() {
    String id = java.util.UUID.randomUUID().toString();
    String result = this.uuidHelpers.getShortUUID(id);
    assertThat(result).hasSize(32);
  }

  @Test
  void getRandomUUID() {
    String result = this.uuidHelpers.getRandomUUID("seed");
    assertThat(result).isNotEmpty().hasSize(36);
  }

  @Test
  void testGetRandomUUID() {
    String result = this.uuidHelpers.getRandomUUID();
    assertThat(result).isNotEmpty().hasSize(36);
  }

  @Test
  void isValidUUID() {
    String id = "1a20aea1-c987-4bd3-a2a1-c19ae741e210";
    String shortId = "8e58ebcf6e45474295782af90264bfaa";
    boolean valid = this.uuidHelpers.isValidUUID(id) && this.uuidHelpers.isValidUUID(shortId);
    assertThat(valid).isTrue();
  }
}
