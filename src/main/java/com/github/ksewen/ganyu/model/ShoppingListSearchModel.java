package com.github.ksewen.ganyu.model;

import com.github.ksewen.ganyu.model.base.DataSearchModel;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author ksewen
 * @date 11.06.2023 17:36
 */
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ShoppingListSearchModel extends DataSearchModel {

  private Long userId;

  private String name;

  private Boolean finished;
}
