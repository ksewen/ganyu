package com.github.ksewen.ganyu.model;

import com.github.ksewen.ganyu.model.base.DataSearchModel;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author ksewen
 * @date 05.06.2023 18:21
 */
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserSearchModel extends DataSearchModel {

  private String username;

  private String nickname;

  private String email;

  private String mobile;
}
