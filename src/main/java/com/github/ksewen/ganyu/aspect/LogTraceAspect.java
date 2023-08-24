package com.github.ksewen.ganyu.aspect;

import com.github.ksewen.ganyu.environment.SystemInformation;
import com.github.ksewen.ganyu.helper.JacksonHelpers;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ksewen
 * @date 12.05.2023 22:11
 */
@Slf4j
@Aspect
public class LogTraceAspect {

  @Autowired private SystemInformation systemInformation;

  @Autowired private HttpServletRequest request;

  @Autowired private JacksonHelpers jacksonHelpers;

  @Pointcut(
      "this(com.github.ksewen.ganyu.controller.LoggingController) "
          + "&& !@annotation(com.github.ksewen.ganyu.annotation.LoggerNotTrace))")
  public void pointCut() {}

  @Before("pointCut()")
  public void doBefore(JoinPoint joinPoint) {
    log.info(
        "{} start: {}, with arguments: {}",
        this.systemInformation.getApplicationName(),
        request.getRequestURI(),
        this.jacksonHelpers.toJsonNode(joinPoint.getArgs()));
  }

  @AfterThrowing(value = "pointCut()", throwing = "exception")
  public void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {
    log.info(
        "{} throw a exception: {}, with arguments: {}",
        this.systemInformation.getApplicationName(),
        request.getRequestURI(),
        this.jacksonHelpers.toJsonNode(joinPoint.getArgs()),
        exception);
  }

  @AfterReturning(value = "pointCut()", returning = "result")
  public void doAfterReturningAdvice(JoinPoint joinPoint, Object result) {
    log.info(
        "{} finish: {}, result: {}",
        this.systemInformation.getApplicationName(),
        request.getRequestURI(),
        this.jacksonHelpers.toJsonNode(result));
  }

  @After("pointCut()")
  public void doAfter(JoinPoint joinpoint) {}
}
