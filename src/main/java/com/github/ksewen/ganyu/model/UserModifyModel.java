package com.github.ksewen.ganyu.model;

import lombok.*;

/**
 * @author ksewen
 * @date 15.05.2023 22:32
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserModifyModel {

    private Long id;

    private String nickname;

    private String password;

    private String mobile;

    private String avatarUrl;

}
