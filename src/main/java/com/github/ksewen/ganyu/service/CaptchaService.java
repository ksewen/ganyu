package com.github.ksewen.ganyu.service;

/**
 * @author ksewen
 * @date 20.05.2023 22:34
 */
public interface CaptchaService {

    void apply(Long userId, Long typeId);

    boolean check(Long userId, String code, Long typeId);

}
