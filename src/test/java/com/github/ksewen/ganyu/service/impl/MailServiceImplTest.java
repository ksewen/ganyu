package com.github.ksewen.ganyu.service.impl;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author ksewen
 * @date 02.08.2023 13:59
 */
@SpringBootTest(
    classes = MailServiceImpl.class,
    properties = {"spring.mail.username=test"})
class MailServiceImplTest {

  @Autowired private MailServiceImpl mailService;

  @MockBean private JavaMailSender javaMailSender;

  @Test
  void sendSimple() {
    String to = "recipient";
    String subject = "test subject";
    String text = "test text";
    this.mailService.sendSimple(to, subject, text);
    verify(this.javaMailSender, times(1))
        .send(
            (SimpleMailMessage)
                argThat(
                    message -> {
                      boolean f =
                          "test".equals(((SimpleMailMessage) message).getFrom())
                              && to.equals(((SimpleMailMessage) message).getTo()[0])
                              && subject.equals(((SimpleMailMessage) message).getSubject())
                              && text.equals(((SimpleMailMessage) message).getText());
                      return f;
                    }));
  }
}
