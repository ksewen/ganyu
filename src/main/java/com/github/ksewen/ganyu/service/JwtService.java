package com.github.ksewen.ganyu.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author ksewen
 * @date 10.05.2023 12:26
 */
public interface JwtService {

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(UserDetails userDetails);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    boolean validateToken(String token, UserDetails userDetails);

}
