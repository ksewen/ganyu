package com.github.ksewen.ganyu.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.github.ksewen.ganyu.model.AuthModel;
import com.github.ksewen.ganyu.model.JwtUserModel;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUserModel create(AuthModel user) {
        return JwtUserModel.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .password(user.getPassword())
                .mobile(user.getMobile())
                .avatarUrl(user.getAvatarUrl())
                .authorities(mapToGrantedAuthorities(user.getRoles()
                        .stream().map(r -> r.getName()).collect(Collectors.toList())))
                .build();
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
