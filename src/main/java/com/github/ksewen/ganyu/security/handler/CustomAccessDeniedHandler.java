package com.github.ksewen.ganyu.security.handler;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.github.ksewen.ganyu.dto.response.base.Result;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.helper.JacksonHelpers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 15.05.2023 17:25
 */
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final JacksonHelpers jacksonHelpers;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(403);
        response.getWriter().write(this.jacksonHelpers.toJsonString(Result.builder()
                .code(ResultCode.ACCESS_DENIED.getCode()).message(ResultCode.ACCESS_DENIED.getMessage()).build()));
    }

}
