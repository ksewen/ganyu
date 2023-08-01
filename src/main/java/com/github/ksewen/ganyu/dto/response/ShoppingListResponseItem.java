package com.github.ksewen.ganyu.dto.response;

import java.time.LocalDateTime;
import lombok.*;

/**
 * @author ksewen
 * @date 07.06.2023 12:04
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ShoppingListResponseItem {

    private Long id;

    private Long shoppingListId;

    private Long planToBuyId;

    private Boolean bought;

    private String name;

    private String brand;

    private String description;

    private String imageUrl;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;
}
