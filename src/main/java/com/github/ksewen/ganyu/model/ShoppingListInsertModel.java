package com.github.ksewen.ganyu.model;

import java.util.List;

import lombok.*;

/**
 * @author ksewen
 * @date 07.06.2023 12:23
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ShoppingListInsertModel {

    private String name;

    private Long userId;

    private String description;

    private List<Long> itemIds;

}
