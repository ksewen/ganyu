package com.github.ksewen.ganyu.domain;

import com.github.ksewen.ganyu.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author ksewen
 * @date 11.05.2023 08:21
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "token")
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Column(nullable = false, columnDefinition = "BIGINT(20)")
  private Long userId;

  @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
  private String token;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, columnDefinition = "VARCHAR(64)")
  @Builder.Default
  private TokenType tokenType = TokenType.BEARER;

  @Column(nullable = false, columnDefinition = "TINYINT(1)")
  @Builder.Default
  private Boolean revoked = Boolean.FALSE;

  ;

  @Column(nullable = false, columnDefinition = "TINYINT(1)")
  @Builder.Default
  private Boolean expired = Boolean.FALSE;

  ;
}
