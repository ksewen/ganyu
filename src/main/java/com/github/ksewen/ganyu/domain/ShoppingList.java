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
 * @date 06.06.2023 17:16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "shopping_list")
@EntityListeners(value = AuditingEntityListener.class)
@SQLDelete(sql = "update shopping_list set deleted = true where id = ?")
@Where(clause = "deleted = false")
public class ShoppingList {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Column(nullable = false, columnDefinition = "BIGINT(20)")
  private Long userId;

  @Column(nullable = false, columnDefinition = "VARCHAR(128)")
  private String name;

  @Column(columnDefinition = "VARCHAR(255)")
  private String description;

  @Column(nullable = false, columnDefinition = "TINYINT(1) default 0")
  @Builder.Default
  private Boolean finished = Boolean.FALSE;

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
