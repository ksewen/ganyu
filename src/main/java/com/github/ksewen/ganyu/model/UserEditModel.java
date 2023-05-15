package com.github.ksewen.ganyu.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author ksewen
 * @date 15.05.2023 22:32
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class UserEditModel extends UserRegisterModel {

    private Long id;

}
