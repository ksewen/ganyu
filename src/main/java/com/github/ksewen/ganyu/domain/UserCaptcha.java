package com.github.ksewen.ganyu.domain;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author ksewen
 * @date 23.05.2023 09:27
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "user_captcha")
public class UserCaptcha {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, columnDefinition = "BIGINT(20)")
    private Long userId;

    @Column(nullable = false, columnDefinition = "BIGINT(20)")
    private Long captchaTypeId;

    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    private String code;

    @Column(columnDefinition = "DATETIME")
    @CreationTimestamp
    private Date expiration;

}
