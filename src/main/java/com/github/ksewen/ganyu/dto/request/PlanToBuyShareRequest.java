package com.github.ksewen.ganyu.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
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

    private List<Long> targetUserIds;

    private Boolean assigned = Boolean.FALSE;
}
