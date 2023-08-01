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
public class UserModifyRequest {

  private Long id;

  private String nickname;

  private String mobile;

  private String avatarUrl;
}
