package com.github.ksewen.ganyu.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author ksewen
 * @date 03.06.2023 13:33
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PlanToBuyModifyRequest {

    @NotNull(message = "{plan.to.buy.id.null}")
    private Long id;

    private String brand;

    private String name;

    private String description;

    private String imageUrl;

    private List<String> businessType;

}
