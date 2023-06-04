package com.github.ksewen.ganyu.model;

import lombok.*;

/**
 * @author ksewen
 * @date 01.06.2023 22:12
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanToBuySearchModel {

    private String brand;

    private Long userId;

    private Long shareFrom;

    private Boolean assigned;

    private String name;

    private String businessType;

}
