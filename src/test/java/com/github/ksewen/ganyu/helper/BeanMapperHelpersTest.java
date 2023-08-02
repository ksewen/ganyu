package com.github.ksewen.ganyu.helper;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.enums.ResultCode;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ksewen
 * @date 01.08.2023 18:10
 */
@SpringBootTest(classes = BeanMapperHelpers.class)
class BeanMapperHelpersTest {

  @Autowired BeanMapperHelpers beanMapperHelpers;

  private String DEFAULT_NAME = "createAndCopyProperties";

  private String DEFAULT_INFO = "DEFAULT";
  private SourceObject source = SourceObject.builder().name(this.DEFAULT_NAME).build();

  @Test
  void createAndCopyProperties() {
    TargetObject result =
        this.beanMapperHelpers.createAndCopyProperties(this.source, TargetObject.class);
    assertThat(result).matches(t -> this.DEFAULT_NAME.equals(t.name));
  }

  @Test
  void createAndCopyPropertiesWithTargetWithoutConstructor() {
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class,
            () ->
                this.beanMapperHelpers.createAndCopyProperties(
                    this.source, TargetObjectWithOutDefaultConstructor.class));
    assertThat(exception).matches(e -> ResultCode.COPY_PROPERTIES_ERROR.equals(e.getCode()));
  }

  @Test
  void createAndCopyNotNullProperties() {
    TargetObjectWithDefaultInfo result =
        this.beanMapperHelpers.createAndCopyNotNullProperties(
            this.source, TargetObjectWithDefaultInfo.class);
    assertThat(result)
        .matches(t -> this.DEFAULT_NAME.equals(t.name))
        .matches(t -> this.DEFAULT_INFO.equals(t.info));
  }

  @Test
  void getNullPropertyNames() {}

  @Getter
  @Setter
  @ToString
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  static class SourceObject {
    private String name;
    private String info;
  }

  @Getter
  @Setter
  @ToString
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  static class TargetObject {
    private String name;
  }

  @Getter
  @Setter
  @ToString
  @Builder
  @AllArgsConstructor
  static class TargetObjectWithOutDefaultConstructor {
    private String name;
  }

  @Getter
  @Setter
  @ToString
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  static class TargetObjectWithDefaultInfo {
    private String name;
    private String info = "DEFAULT";
  }
}
