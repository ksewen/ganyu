package com.github.ksewen.ganyu.aspect;

import com.github.ksewen.ganyu.dto.BaseRequest;
import com.github.ksewen.ganyu.helper.JacksonHelpers;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ksewen.ganyu.annotation.LoggerNotTrace;
import com.github.ksewen.ganyu.environment.SystemInformation;
import com.github.ksewen.ganyu.helper.MDCHelpers;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ksewen
 * @date 12.05.2023 22:11
 */
@Slf4j
@Aspect
public class LogTraceAspect {

    @Autowired
    private MDCHelpers mdcHelpers;

    @Autowired
    private SystemInformation systemInformation;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JacksonHelpers jacksonHelpers;

    @Pointcut("this(com.github.ksewen.ganyu.controller.LoggingController) "
            + "&& !@annotation(com.github.ksewen.ganyu.annotation.LoggerNotTrace))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void doBefore(JoinPoint joinPoint) {
        boolean state = this.mdcHelpers.alreadyFinish();
        if (!state) {
            this.mdcHelpers.init();
        }
        log.info("{} start: {}, with arguments: {}", this.systemInformation.getApplicationName(),
                request.getRequestURI(), this.jacksonHelpers.toJsonNode(this.getParameter(joinPoint)));
    }

    @AfterThrowing(value = "pointCut()", throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {
        log.info("{} throw a exception: {}, with arguments: {}", this.systemInformation.getApplicationName(),
                request.getRequestURI(), this.jacksonHelpers.toJsonNode(this.getParameter(joinPoint)), exception);
        this.mdcHelpers.close();
    }

    @AfterReturning(value = "pointCut()", returning = "result")
    public void doAfterReturningAdvice(JoinPoint joinPoint, Object result) {
        log.info("{} finish: {}, with arguments: {}", request.getRequestURI(),
                this.jacksonHelpers.toJsonNode(this.getParameter(joinPoint)));
        this.mdcHelpers.close();
    }

    @After("pointCut()")
    public void doAfter(JoinPoint joinpoint) {

    }

    private String getParameter(JoinPoint joinpoint) {
        Object[] args = joinpoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BaseRequest) {
                return this.jacksonHelpers.toJsonString(arg);
            }
        }
        return "no argument";
    }
}
