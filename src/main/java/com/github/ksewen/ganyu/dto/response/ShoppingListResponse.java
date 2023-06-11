package com.github.ksewen.ganyu.dto.response;

import java.time.LocalDateTime;

import lombok.*;

/**
 * @author ksewen
 * @date 07.06.2023 12:04
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ShoppingListResponse {

    private Long id;

    private Long userId;

    private String name;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;
}
