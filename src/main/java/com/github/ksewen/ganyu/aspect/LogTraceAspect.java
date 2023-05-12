package com.github.ksewen.ganyu.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ksewen.ganyu.annotation.LogTrace;
import com.github.ksewen.ganyu.environment.SystemInformation;
import com.github.ksewen.ganyu.helper.MDCHelpers;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ksewen
 * @date 12.05.2023 22:11
 */
@Slf4j
@Aspect
@Component
public class LogTraceAspect {

    @Autowired
    private MDCHelpers mdcHelpers;

    @Autowired
    private SystemInformation systemInformation;

    @Pointcut("@annotation(com.github.ksewen.ganyu.annotation.LogTrace)")
    public void pointCut() {
    }

    @Before("pointCut() && @annotation(logTrace)")
    public void doBefore(JoinPoint joinpoint, LogTrace logTrace) {
        boolean state = this.mdcHelpers.alreadyFinish();
        if (!state) {
            this.mdcHelpers.init();
        }
        log.info("{} start: {}", this.systemInformation.getApplicationName(), logTrace.value());
    }

    @AfterThrowing(value = "pointCut() && @annotation(logTrace)", throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint, LogTrace logTrace, Throwable exception) {
        log.info("{} throw a exception: {}", this.systemInformation.getApplicationName(), logTrace.value(), exception);
        this.mdcHelpers.close();
    }

    @AfterReturning(value = "pointCut() && @annotation(logTrace)", returning = "result")
    public void doAfterReturningAdvice(JoinPoint joinPoint, LogTrace logTrace, Object result) {
        log.info("{} finish: {}", logTrace.value());
        this.mdcHelpers.close();
    }

    @After("pointCut()")
    public void doAfter(JoinPoint joinpoint) {

    }
}
