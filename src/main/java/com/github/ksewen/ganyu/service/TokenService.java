package com.github.ksewen.ganyu.service;

import java.util.Optional;

import com.github.ksewen.ganyu.domain.Token;

/**
 * @author ksewen
 * @date 14.05.2023 14:58
 */
public interface TokenService {

    void removeAllUserTokens(Long userId);

    Token save(Long userId, String token);

    String refresh(String oldToken);

    Optional<Token> findByToken(String token);

}
