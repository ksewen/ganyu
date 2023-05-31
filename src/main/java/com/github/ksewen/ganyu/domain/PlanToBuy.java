package com.github.ksewen.ganyu.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

/**
 * @author ksewen
 * @date 31.05.2023 13:37
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "plan_to_buy")
@EntityListeners(value = AuditingEntityListener.class)
@SQLDelete(sql = "update plan_to_buy set deleted = true where id = ?")
@Where(clause = "deleted = false")
public class PlanToBuy {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, columnDefinition = "BIGINT(20)")
    private Long userId;

    @Column(nullable = false, columnDefinition = "VARCHAR(128) default 'UNSET'")
    private String brand;

    @Column(columnDefinition = "BIGINT(20)")
    private Long shareFrom;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean assigned;

    @Column(nullable = false, columnDefinition = "VARCHAR(128)")
    private String name;

    @Column(columnDefinition = "VARCHAR(255)")
    private String description;

    @Column(columnDefinition = "VARCHAR(255)")
    private String imageUrl;

    @Column(columnDefinition = "VARCHAR(255)")
    private String businessType;

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
