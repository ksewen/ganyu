package com.github.ksewen.ganyu.configuration;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.dto.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ksewen
 * @date 10.05.2023 23:06
 */
@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(value = { MethodArgumentNotValidException.class, BindException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    @SuppressWarnings({ "rawtypes" })
    public Result handleMethodArgumentNotValidException(Exception validException) {
        BindingResult bindingResult = null;
        if (validException instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) validException).getBindingResult();
        } else if (validException instanceof BindException) {
            bindingResult = ((BindException) validException).getBindingResult();
        }
        if (null == bindingResult || null == bindingResult.getFieldError()) {
            return Result.paramInvalid();
        }
        return Result.paramInvalid(bindingResult.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @SuppressWarnings({ "rawtypes" })
    public Result handleCommonException(Exception exception) {
        log.info("internal server error", exception);
        return Result.systemError();
    }

}
