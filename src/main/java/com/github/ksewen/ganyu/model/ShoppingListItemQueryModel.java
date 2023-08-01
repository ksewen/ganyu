package com.github.ksewen.ganyu.model;

import java.time.LocalDateTime;
import lombok.*;

/**
 * @author ksewen
 * @date 07.06.2023 12:30
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ShoppingListItemQueryModel {

    private Long id;

    private Long shoppingListId;

    private Long planToBuyId;

    private String name;

    private String brand;

    private String description;

    private String imageUrl;

    private Boolean bought;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;

}
