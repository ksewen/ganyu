package com.github.ksewen.ganyu.service;

/**
 * @author ksewen
 * @date 29.05.2023 15:52
 */
public interface PasswordService {

  void modify(String exist, String modify, long userId);

  void reset(String captcha, String modify, long userId);
}
