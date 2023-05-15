package com.github.ksewen.ganyu.service;

import java.util.Optional;

import com.github.ksewen.ganyu.domain.Token;
import com.github.ksewen.ganyu.dto.response.JwtTokenResponse;

/**
 * @author ksewen
 * @date 14.05.2023 14:58
 */
public interface TokenService {

    void removeAllUserTokens(Long userId);

    Token save(Long userId, String token);

    JwtTokenResponse refresh(String refreshToken);

    Optional<Token> findByToken(String token);

}
