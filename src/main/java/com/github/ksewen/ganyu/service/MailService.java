package com.github.ksewen.ganyu.service;

/**
 * @author ksewen
 * @date 20.05.2023 23:15
 */
public interface MailService {

  void sendSimple(String to, String subject, String text);
}
