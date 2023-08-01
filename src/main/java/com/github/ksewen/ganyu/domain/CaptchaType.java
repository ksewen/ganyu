package com.github.ksewen.ganyu.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author ksewen
 * @date 23.05.2023 09:36
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "captcha_type")
@EntityListeners(value = AuditingEntityListener.class)
@SQLDelete(sql = "update captcha_type set deleted = true where id = ?")
@Where(clause = "deleted = false")
public class CaptchaType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Column(nullable = false, columnDefinition = "VARCHAR(32)")
  private String name;

  @Column(columnDefinition = "DATETIME")
  @CreationTimestamp
  private LocalDateTime createTime;

  @Column(columnDefinition = "VARCHAR(64)")
  @CreatedBy
  private String createBy;

  @Column(columnDefinition = "DATETIME")
  @UpdateTimestamp
  private LocalDateTime modifyTime;

  @Column(columnDefinition = "VARCHAR(64)")
  @LastModifiedBy
  private String modifyBy;

  @Column(columnDefinition = "TINYINT(1) default 0", nullable = false)
  @Builder.Default
  private Boolean deleted = Boolean.FALSE;
}
