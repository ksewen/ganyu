package com.github.ksewen.ganyu.model.base;

import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author ksewen
 * @date 05.06.2023 18:36
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DataSearchModel {

    private LocalDateTime createTimeAfter;

    private LocalDateTime createTimeBefore;

    private LocalDateTime modifyTimeAfter;

    private LocalDateTime modifyTimeBefore;

}
