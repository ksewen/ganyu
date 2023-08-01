package com.github.ksewen.ganyu.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.*;

/**
 * @author ksewen
 * @date 07.06.2023 12:14
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ShoppingListInsertRequest {

  @NotBlank(message = "{shopping.list.name.null}")
  private String name;

  private String description;

  private List<
          @Valid @NotNull(message = "{shopping.list.item.id.null}")
          @Min(message = "shopping.list.item.id.minimal", value = 1) Long>
      itemIds;
}
