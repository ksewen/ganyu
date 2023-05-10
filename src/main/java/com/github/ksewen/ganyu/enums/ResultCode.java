package com.github.ksewen.ganyu.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * @author ksewen
 * @date 10.05.2023 12:55
 */

@Getter
@ToString
public enum ResultCode {

    SUCCESS(20000, "success"),

    PARAM_INVALID(40000, "parameter invalid"),
    SYSTEM_ERROR(50000, "system error"),
    OPERATION_FAILED(50001, "operation failed");

    private int code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
