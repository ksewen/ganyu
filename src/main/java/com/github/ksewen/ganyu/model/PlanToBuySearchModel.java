package com.github.ksewen.ganyu.model;

import com.github.ksewen.ganyu.model.base.DataSearchModel;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author ksewen
 * @date 01.06.2023 22:12
 */
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PlanToBuySearchModel extends DataSearchModel {

    private String brand;

    private Long userId;

    private Long shareFrom;

    private Boolean assigned;

    private Boolean bought;

    private String name;

    private String businessType;

}
