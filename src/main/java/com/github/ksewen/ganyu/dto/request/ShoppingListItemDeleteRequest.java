package com.github.ksewen.ganyu.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.*;

/**
 * @author ksewen
 * @date 07.06.2023 23:02
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ShoppingListItemDeleteRequest {

  @NotEmpty(message = "{shopping.list.items.empty}")
  private List<
          @Valid @NotNull(message = "{shopping.list.item.id.null}")
          @Min(message = "{shopping.list.item.id.minimal}", value = 1) Long>
      ids;
}
