package com.github.ksewen.ganyu.dto.request;

import lombok.*;

/**
 * @author ksewen
 * @date 15.05.2023 22:36
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserEditRequest {

    private Long id;

    private String username;

    private String email;

    private String password;

    private String nickname;

    private String mobile;

    private String avatarUrl;

}
