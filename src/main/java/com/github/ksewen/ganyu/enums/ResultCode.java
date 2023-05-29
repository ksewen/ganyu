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
    CAPTCHA_INVALID(40004, "invalid captcha"),
    UNAUTHORIZED(41000, "invalid username or password"),
    ACCESS_DENIED(43000, "permission denied"),
    NOT_FOUND(44000, "can not find the record"),
    SYSTEM_ERROR(50000, "system error"),
    COPY_PROPERTIES_ERROR(50001, "copy properties failed"),
    SERIALIZATION_OR_DESERIALIZATION_ERROR(50002, "serialization or deserialization failed"),
    OPERATION_FAILED(50001, "operation failed");

    private int code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
