package com.github.ksewen.ganyu.dto.response;

import lombok.*;

/**
 * @author ksewen
 * @date 31.05.2023 15:02
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BusinessTypeInfoResponse {

  private Long id;

  private String name;
}
