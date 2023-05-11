package com.github.ksewen.ganyu.dto.auth;

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
public class RegisterRequest extends LoginRequest {

    @NotBlank(message = "{auth.username.null}")
    private String username;

    private String nickname;

    private String mobile;

    private String avatarUrl;

}
