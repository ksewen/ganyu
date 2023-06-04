package com.github.ksewen.ganyu.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * @author ksewen
 * @date 31.05.2023 14:58
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BusinessTypeAddRequest {

    @NotBlank(message = "{business.type.name.null}")
    private String name;

}
