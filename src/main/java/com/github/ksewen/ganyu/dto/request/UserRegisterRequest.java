package com.github.ksewen.ganyu.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author ksewen
 * @date 10.05.2023 16:09
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class UserRegisterRequest extends LoginRequest {

    @NotBlank(message = "{auth.email.null}")
    private String email;

    private String nickname;

    private String mobile;

    private String avatarUrl;

}
