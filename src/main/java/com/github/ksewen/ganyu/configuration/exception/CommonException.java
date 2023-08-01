package com.github.ksewen.ganyu.configuration.exception;

import com.github.ksewen.ganyu.enums.ResultCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ksewen
 * @date 10.05.2023 23:13
 */
@Getter
@Setter
@ToString
public class CommonException extends RuntimeException {

    private ResultCode code;

    public CommonException() {
        super(ResultCode.SYSTEM_ERROR.getMessage());
        this.code = ResultCode.SYSTEM_ERROR;
    }

    public CommonException(ResultCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public CommonException(String message) {
        super(message);
        this.code = ResultCode.SYSTEM_ERROR;
    }

    public CommonException(ResultCode code, String message) {
        super(message);
        this.code = code;
    }

}
