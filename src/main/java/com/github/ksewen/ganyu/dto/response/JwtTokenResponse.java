package com.github.ksewen.ganyu.dto.response;

import java.time.LocalDateTime;

import lombok.*;

/**
 * @author ksewen
 * @date 14.05.2023 18:16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JwtTokenResponse {

    private Long id;

    private Long userId;

    private String username;

    private String token;

    private String refreshToken;

    private LocalDateTime expireAt;
}
