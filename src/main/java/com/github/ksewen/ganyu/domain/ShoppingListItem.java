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
 * @date 07.06.2023 11:52
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "shopping_list_item")
@EntityListeners(value = AuditingEntityListener.class)
@SQLDelete(sql = "update shopping_list_item set deleted = true where id = ?")
@Where(clause = "deleted = false")
public class ShoppingListItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Column(nullable = false, columnDefinition = "BIGINT(20)")
  private Long shoppingListId;

  @Column(nullable = false, columnDefinition = "BIGINT(20)")
  private Long planToBuyId;

  @Column(nullable = false, columnDefinition = "TINYINT(1) default 0")
  @Builder.Default
  private Boolean bought = Boolean.FALSE;

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
