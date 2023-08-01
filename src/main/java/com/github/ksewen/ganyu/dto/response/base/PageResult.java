package com.github.ksewen.ganyu.dto.response.base;

import com.github.ksewen.ganyu.enums.ResultCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author lixinrui
 * @date 2020/3/104:38 下午
 */
@Setter
@Getter
@ToString(callSuper = true)
public class PageResult<T> extends Result<T> {

  private Integer index;
  private Integer count;
  private Long total;

  public PageResult() {
    super();
  }

  public PageResult(ResultCode resultCode) {
    super(resultCode);
  }

  public PageResult(ResultCode errorCode, String message) {
    super(errorCode, message);
  }

  public PageResult(Integer index, Integer count, Long total) {
    super();
    this.index = index;
    this.count = count;
    this.total = total;
  }

  public PageResult(ResultCode errorCode, Integer index, Integer count, Long total) {
    super(errorCode);
    this.index = index;
    this.count = count;
    this.total = total;
  }

  public PageResult(T data, Integer index, Integer count, Long total) {
    super(data);
    this.index = index;
    this.count = count;
    this.total = total;
  }

  public PageResult(
      ResultCode errorCode, String message, Integer index, Integer count, Long total) {
    super(errorCode, message);
    this.index = index;
    this.count = count;
    this.total = total;
  }

  public PageResult(
      ResultCode errorCode, String message, T data, Integer index, Integer count, Long total) {
    super(errorCode, message, data);
    this.index = index;
    this.count = count;
    this.total = total;
  }

  public static PageResult success() {
    return new PageResult();
  }

  public static PageResult success(String message) {
    return new PageResult(ResultCode.SUCCESS, message);
  }

  public static PageResult success(Integer page, Integer pageSize, Long total) {
    return new PageResult(page, pageSize, total);
  }

  public static <T> PageResult<T> success(T data, Integer page, Integer pageSize, Long total) {
    return new PageResult(data, page, pageSize, total);
  }

  public static <T> PageResult<T> success(
      String message, Integer page, Integer pageSize, Long total) {
    return new PageResult(ResultCode.SUCCESS, message, page, pageSize, total);
  }

  public static <T> PageResult<T> success(
      String message, T data, Integer page, Integer pageSize, Long total) {
    return new PageResult(ResultCode.SUCCESS, message, data, page, pageSize, total);
  }

  public static PageResult operationFailed() {
    return new PageResult(ResultCode.OPERATION_FAILED);
  }

  public static PageResult operationFailed(String message) {
    return new PageResult(ResultCode.OPERATION_FAILED, message);
  }

  public static <T> PageResult<T> operationFailed(
      T data, Integer page, Integer pageSize, Long total) {
    return new PageResult(
        ResultCode.OPERATION_FAILED,
        ResultCode.OPERATION_FAILED.getMessage(),
        data,
        page,
        pageSize,
        total);
  }

  public static <T> PageResult<T> operationFailed(
      String message, Integer page, Integer pageSize, Long total) {
    return new PageResult(ResultCode.OPERATION_FAILED, message, page, pageSize, total);
  }

  public static <T> PageResult<T> operationFailed(
      String message, T data, Integer page, Integer pageSize, Long total) {
    return new PageResult(ResultCode.OPERATION_FAILED, message, data, page, pageSize, total);
  }

  public static PageResult systemError() {
    return new PageResult(ResultCode.SYSTEM_ERROR);
  }

  public static PageResult systemError(String message) {
    return new PageResult(ResultCode.SYSTEM_ERROR, message);
  }

  public static <T> PageResult<T> systemError(T data, Integer page, Integer pageSize, Long total) {
    return new PageResult(
        ResultCode.SYSTEM_ERROR, ResultCode.SYSTEM_ERROR.getMessage(), data, page, pageSize, total);
  }

  public static <T> PageResult<T> systemError(
      String message, Integer page, Integer pageSize, Long total) {
    return new PageResult(ResultCode.SYSTEM_ERROR, message, page, pageSize, total);
  }

  public static <T> PageResult<T> systemError(
      String message, T data, Integer page, Integer pageSize, Long total) {
    return new PageResult(ResultCode.SYSTEM_ERROR, message, data, page, pageSize, total);
  }

  public static PageResult paramInvalid() {
    return new PageResult(ResultCode.PARAM_INVALID);
  }

  public static PageResult paramInvalid(String message) {
    return new PageResult(ResultCode.PARAM_INVALID, message);
  }

  public static <T> PageResult<T> paramInvalid(T data, Integer page, Integer pageSize, Long total) {
    return new PageResult(
        ResultCode.PARAM_INVALID,
        ResultCode.PARAM_INVALID.getMessage(),
        data,
        page,
        pageSize,
        total);
  }

  public static <T> PageResult<T> paramInvalid(
      String message, Integer page, Integer pageSize, Long total) {
    return new PageResult(ResultCode.PARAM_INVALID, message, page, pageSize, total);
  }

  public static <T> PageResult<T> paramInvalid(
      String message, T data, Integer page, Integer pageSize, Long total) {
    return new PageResult(ResultCode.PARAM_INVALID, message, data, page, pageSize, total);
  }
}
