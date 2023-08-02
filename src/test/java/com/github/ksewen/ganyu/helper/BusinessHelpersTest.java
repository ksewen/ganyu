package com.github.ksewen.ganyu.helper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ksewen
 * @date 01.08.2023 18:36
 */
@SpringBootTest(classes = BusinessHelpers.class)
class BusinessHelpersTest {

  @Autowired BusinessHelpers businessHelpers;

  @Test
  void listToStringCommaSeparated() {
    String result = this.businessHelpers.listToStringCommaSeparated(Arrays.asList("a", "b", "c"));
    assertThat(result).matches(s -> "a,b,c".equals(s));
  }

  @Test
  void stringCommaSeparatedToList() {
    List<String> result = this.businessHelpers.stringCommaSeparatedToList("a,b,c");
    assertThat(result)
        .matches(s -> s.size() == 3)
        .matches(s -> "a".equals(s.get(0)))
        .matches(s -> "b".equals(s.get(1)))
        .matches(s -> "c".equals(s.get(2)));
  }
}
