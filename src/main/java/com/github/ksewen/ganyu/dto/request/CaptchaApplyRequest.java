package com.github.ksewen.ganyu.dto.request;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "{captcha.type.id.null}")
    private Long typeId;

}
