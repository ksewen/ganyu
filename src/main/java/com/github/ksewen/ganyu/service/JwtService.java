package com.github.ksewen.ganyu.service;

import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import com.github.ksewen.ganyu.model.JwtTokenModel;

import io.jsonwebtoken.Claims;

/**
 * @author ksewen
 * @date 10.05.2023 12:26
 */
public interface JwtService {

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    JwtTokenModel generateToken(UserDetails userDetails);

    JwtTokenModel generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    JwtTokenModel generateRefreshToken(UserDetails userDetails);

    boolean validateToken(String token, UserDetails userDetails);

}
