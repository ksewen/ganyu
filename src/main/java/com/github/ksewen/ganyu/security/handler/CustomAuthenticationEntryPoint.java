package com.github.ksewen.ganyu.security.handler;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.github.ksewen.ganyu.dto.response.base.Result;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.helper.JacksonHelpers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ksewen
 * @date 15.05.2023 17:30
 */
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final JacksonHelpers jacksonHelpers;

    private final String MESSAGE = "authentication failed";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        log.error(this.MESSAGE, authException);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(403);
        response.getWriter().write(this.jacksonHelpers.toJsonString(Result.builder()
                .code(ResultCode.UNAUTHORIZED.getCode()).message(this.MESSAGE).build()));
    }

}
