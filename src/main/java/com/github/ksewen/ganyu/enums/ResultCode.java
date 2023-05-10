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
    ALREADY_EXIST(40001, "the record ist already exist"),
    DUPLICATE_RECORD(40003,"find duplicate records"),
    NOT_FOUND(40004, "can not find the record"),
    SYSTEM_ERROR(50000, "system error"),
    COPY_PROPERTIES_ERROR(50001, "copy properties failed"),
    OPERATION_FAILED(50001, "operation failed");

    private int code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
