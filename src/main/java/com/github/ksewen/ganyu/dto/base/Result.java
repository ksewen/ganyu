package com.github.ksewen.ganyu.dto.base;

import com.github.ksewen.ganyu.enums.ResultCode;
import lombok.*;

/**
 * @author ksewen
 * @date 10.05.2023 12:51
 */
@Getter
@Setter
@Builder
@ToString
public class Result<T> {

    private int code;
    private String message;

    private boolean success;
    private T data;

    public Result() {
        this(ResultCode.SUCCESS, ResultCode.SUCCESS.getMessage());
    }

    public Result(ResultCode resultCode) {
        this(resultCode, resultCode.getMessage());
    }

    public Result(T data) {
        this(ResultCode.SUCCESS, ResultCode.SUCCESS.getMessage(), data);
    }

    public Result(ResultCode resultCode, String message) {
        this(resultCode, message, null);
    }

    public Result(ResultCode resultCode, String message, T data) {
        this.code = resultCode.getCode();
        this.message = message;
        this.data = data;
    }

    public static Result success() {
        return new Result();
    }

    public static Result success(String message) {
        return new Result(ResultCode.SUCCESS, message);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result(ResultCode.SUCCESS, message, data);
    }

    public static Result operationFailed() {
        return new Result(ResultCode.OPERATION_FAILED);
    }

    public static Result operationFailed(String message) {
        return new Result(ResultCode.OPERATION_FAILED, message);
    }

    public static <T> Result<T> operationFailed(T data) {
        return new Result(ResultCode.OPERATION_FAILED, ResultCode.OPERATION_FAILED.getMessage(),
                data);
    }

    public static <T> Result<T> operationFailed(String message, T data) {
        return new Result(ResultCode.OPERATION_FAILED, message, data);
    }

    public static Result systemError() {
        return new Result(ResultCode.SYSTEM_ERROR);
    }

    public static Result systemError(String message) {
        return new Result(ResultCode.SYSTEM_ERROR, message);
    }

    public static <T> Result<T> systemError(T data) {
        return new Result<>(ResultCode.SYSTEM_ERROR, ResultCode.SYSTEM_ERROR.getMessage(), data);
    }

    public static <T> Result<T> systemError(String message, T data) {
        return new Result(ResultCode.SYSTEM_ERROR, message, data);
    }

    public static Result paramInvalid() {
        return new Result(ResultCode.PARAM_INVALID);
    }

    public static Result paramInvalid(String message) {
        return new Result(ResultCode.PARAM_INVALID, message);
    }

    public static <T> Result<T> paramInvalid(T data) {
        return new Result(ResultCode.PARAM_INVALID, ResultCode.PARAM_INVALID.getMessage(), data);
    }

    public static <T> Result<T> paramInvalid(String message, T data) {
        return new Result(ResultCode.PARAM_INVALID, message, data);
    }
}
