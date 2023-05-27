package com.github.ksewen.ganyu.helper;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author ksewen
 * @date 20.05.2023 23:46
 */
public class CaptchaHelpers {

    public String generateSimple() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int captchaNum = random.nextInt(1000000);
        String captcha = String.format("%06d", captchaNum);
        return captcha;
    }

}
