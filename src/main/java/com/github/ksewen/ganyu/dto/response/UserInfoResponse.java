package com.github.ksewen.ganyu.dto.response;

import java.time.LocalDateTime;

import lombok.*;

/**
 * @author ksewen
 * @date 14.05.2023 14:40
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserInfoResponse {

    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String mobile;

    private String avatarUrl;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;

}
