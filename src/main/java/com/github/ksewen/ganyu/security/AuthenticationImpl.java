package com.github.ksewen.ganyu.security;

import com.github.ksewen.ganyu.model.JwtUserModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationImpl implements Authentication {

  private JwtUserModel getUserDetails() {
    return (JwtUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  @Override
  public Long getUserId() {
    return getUserDetails().getId();
  }

  @Override
  public String getUsername() {
    return getUserDetails().getUsername();
  }
}
