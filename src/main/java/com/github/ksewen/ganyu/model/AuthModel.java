package com.github.ksewen.ganyu.model;

import com.github.ksewen.ganyu.domain.Role;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;

/**
 * @author ksewen
 * @date 11.05.2023 22:22
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AuthModel {

    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String password;

    private String mobile;

    private String avatarUrl;

    private List<Role> roles;
}
