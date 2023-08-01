package com.github.ksewen.ganyu.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;

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
