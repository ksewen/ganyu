package com.github.ksewen.ganyu.model;

import lombok.*;

/**
 * @author ksewen
 * @date 10.05.2023 13:38
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserRegisterModel {

    private String username;

    private String nickname;

    private String email;

    private String password;

    private String mobile;

    private String avatarUrl;

}
