package com.github.ksewen.ganyu.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * @author ksewen
 * @date 19.05.2023 20:04
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PasswordModifyRequest {

  @NotBlank(message = "{user.exist.password.null}")
  private String exist;

  @NotBlank(message = "{user.modify.password.null}")
  private String modify;
}
