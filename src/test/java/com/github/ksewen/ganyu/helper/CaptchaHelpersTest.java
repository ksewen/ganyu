package com.github.ksewen.ganyu.helper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ksewen
 * @date 20.05.2023 23:48
 */
@SpringBootTest(classes = CaptchaHelpers.class)
class CaptchaHelpersTest {

    @Autowired
    private CaptchaHelpers captchaHelpers;

    @Test
    void generateSimple() {
        String actual = this.captchaHelpers.generateSimple();
        assertThat(actual).isNotNull().hasSize(6);
    }
}