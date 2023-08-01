package com.github.ksewen.ganyu.configuration.exception;

import com.github.ksewen.ganyu.enums.ResultCode;

/**
 * @author ksewen
 * @date 14.05.2023 21:25
 */
public class InvalidParamException extends CommonException {

  public InvalidParamException() {
    super(ResultCode.PARAM_INVALID);
  }

  public InvalidParamException(String message) {
    super(ResultCode.PARAM_INVALID, message);
  }
}
