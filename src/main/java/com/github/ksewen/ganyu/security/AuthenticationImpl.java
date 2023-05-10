package com.github.ksewen.ganyu.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.github.ksewen.ganyu.model.JwtUserModel;

@Component
public class AuthenticationImpl implements Authentication {

    private JwtUserModel getUserDetails(){
        return (JwtUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public Integer getUserId() {
        return getUserDetails().getId();
    }

    @Override
    public String getUsername() {
        return getUserDetails().getUsername();
    }
}
