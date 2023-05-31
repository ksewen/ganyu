package com.github.ksewen.ganyu.model;

import java.util.List;

import lombok.*;

/**
 * @author ksewen
 * @date 31.05.2023 13:59
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PlanToBuyModel {

    private Long id;

    private String brand;

    private Long userId;

    private Long shareFrom;

    private Boolean assigned;

    private String name;

    private String description;

    private String imageUrl;

    private List<String> businessType;
}
