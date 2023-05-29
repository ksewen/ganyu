package com.github.ksewen.ganyu.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.ksewen.ganyu.configuration.exception.InvalidParamException;
import com.github.ksewen.ganyu.domain.Token;
import com.github.ksewen.ganyu.dto.response.JwtTokenResponse;
import com.github.ksewen.ganyu.mapper.TokenMapper;
import com.github.ksewen.ganyu.model.JwtUserModel;
import com.github.ksewen.ganyu.service.JwtService;
import com.github.ksewen.ganyu.service.TokenService;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 14.05.2023 15:02
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final UserDetailsService userDetailsService;

    private final TokenMapper tokenMapper;

    private final JwtService jwtService;

    @Override
    public void removeAllUserTokens(Long userId) {
        List<Token> validUserTokens = this.tokenMapper
                .findAll((Specification<Token>) (root, query, criteriaBuilder) -> {
                    List<Predicate> list = new ArrayList<>();
                    list.add(criteriaBuilder.equal(root.get("userId").as(Long.class), userId));
                    Predicate[] array = new Predicate[list.size()];
                    Predicate predicate = criteriaBuilder.and(list.toArray(array));

                    List<Predicate> listOr = new ArrayList<>();
                    listOr.add(criteriaBuilder.equal(root.get("revoked").as(Boolean.class), false));
                    listOr.add(criteriaBuilder.equal(root.get("expired").as(Boolean.class), false));
                    Predicate[] arrayOr = new Predicate[listOr.size()];
                    Predicate predicateOr = criteriaBuilder.or(listOr.toArray(arrayOr));

                    return query.where(predicate, predicateOr).getRestriction();
                });
        if (!validUserTokens.isEmpty()) {
            this.tokenMapper.deleteAll(validUserTokens);
        }
    }

    @Override
    public Token save(Long userId, String token) {
        Token save = Token.builder().userId(userId).token(token).build();
        return this.tokenMapper.save(save);
    }

    @Override
    public JwtTokenResponse refresh(String refreshToken) {
        String username = this.jwtService.extractUsername(refreshToken);
        if (StringUtils.hasLength(username)) {
            final JwtUserModel user = (JwtUserModel) this.userDetailsService.loadUserByUsername(username);
            if (this.jwtService.validateToken(refreshToken, user)) {
                String accessToken = this.jwtService.generateToken(user);
                this.removeAllUserTokens(user.getId());
                Token save = this.save(user.getId(), accessToken);
                return JwtTokenResponse.builder().id(save.getId()).token(accessToken).refreshToken(refreshToken).build();
            }
        }
        throw new InvalidParamException("invalid refresh_token");
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return this.tokenMapper.findByToken(token);
    }
}
