package com.github.ksewen.ganyu.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.*;

/**
 * @author ksewen
 * @date 04.06.2023 22:51
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PlanToBuyShareRequest {

  @NotNull(message = "{plan.to.buy.id.null}")
  private Long id;

  @NotEmpty(message = "{plan.to.buy.share.target.users.empty}")
  private List<
          @Valid @NotNull(message = "{plan.to.buy.share.target.user.null}")
          @Min(value = 1, message = "{plan.to.buy.share.target.user.minimal}") Long>
      targetUserIds;

  private Boolean assigned = Boolean.FALSE;
}
