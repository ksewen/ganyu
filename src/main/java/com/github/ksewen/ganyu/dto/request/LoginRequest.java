package com.github.ksewen.ganyu.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author ksewen
 * @date 10.05.2023 22:29
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class LoginRequest {

    @NotBlank(message = "{auth.username.null}")
    private String username;;

    @NotBlank(message = "{auth.password.null}")
    private String password;

}
