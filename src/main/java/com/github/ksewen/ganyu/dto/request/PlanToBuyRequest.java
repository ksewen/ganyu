package com.github.ksewen.ganyu.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * @author ksewen
 * @date 31.05.2023 13:53
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PlanToBuyRequest {

    @NotBlank(message = "{plan.to.buy.name.null}")
    private String name;

    private String brand;

    private String description;

    private String imageUrl;

    private List<String> businessType;

}
