package com.github.ksewen.ganyu.model;

import java.time.LocalDateTime;

import lombok.*;

/**
 * @author ksewen
 * @date 23.06.2023 09:37
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtTokenModel {

    private String token;

    private LocalDateTime expireAt;
}
