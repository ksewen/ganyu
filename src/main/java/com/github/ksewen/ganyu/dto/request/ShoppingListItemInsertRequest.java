package com.github.ksewen.ganyu.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author ksewen
 * @date 07.06.2023 16:17
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ShoppingListItemInsertRequest {

    @NotNull(message = "{shopping.list.id.null}")
    private Long shoppingListId;

    @NotEmpty(message = "{shopping.list.items.empty}")
    private List<@Valid @NotNull(message = "{shopping.list.item.id.null}") @Min(message = "shopping.list.item.id.minimal", value = 1) Long> itemIds;

}
