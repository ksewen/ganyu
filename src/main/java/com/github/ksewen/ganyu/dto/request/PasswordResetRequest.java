package com.github.ksewen.ganyu.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * @author ksewen
 * @date 29.05.2023 12:28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PasswordResetRequest {

    @NotBlank(message = "{captcha.code.null}")
    private String captcha;

    @NotBlank(message = "{user.modify.password.null}")
    private String modify;

}
