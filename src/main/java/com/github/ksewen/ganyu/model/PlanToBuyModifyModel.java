package com.github.ksewen.ganyu.model;

import java.util.List;

import lombok.*;

/**
 * @author ksewen
 * @date 03.06.2023 13:32
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PlanToBuyModifyModel {

    private long id;

    private String brand;

    private String name;

    private String description;

    private String imageUrl;

    private List<String> businessType;

}
