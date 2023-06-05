package com.github.ksewen.ganyu.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

/**
 * @author ksewen
 * @date 31.05.2023 18:43
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PlanToBuyResponse {

    private Long id;

    private Long userId;

    private String brand;

    private Long shareFrom;

    private Boolean assigned;

    private Boolean bought;

    private String name;

    private String description;

    private String imageUrl;

    private List<String> businessType;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;
}
