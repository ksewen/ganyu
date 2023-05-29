package com.github.ksewen.ganyu.dto.request;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author ksewen
 * @date 23.05.2023 09:17
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaptchaApplyRequest {

    @NotNull(message = "{captcha.type.id.null}")
    @Range(min = 1)
    private Long typeId;

}
