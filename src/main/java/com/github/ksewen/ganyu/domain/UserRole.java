package com.github.ksewen.ganyu.domain;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author ksewen
 * @date 11.05.2023 08:14
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "user_role")
@EntityListeners(value = AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE user_role SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class UserRole {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, columnDefinition = "BIGINT(20)")
    private Long userId;

    @Column(nullable = false, columnDefinition = "BIGINT(20)")
    private Long roleId;

    @Column(columnDefinition = "DATETIME")
    @CreationTimestamp
    private Date createTime;

    @Column(columnDefinition = "VARCHAR(64)")
    @CreatedBy
    private String createBy;

    @Column(columnDefinition = "DATETIME")
    @UpdateTimestamp
    private Date modifyTime;

    @Column(columnDefinition = "VARCHAR(64)")
    @LastModifiedBy
    private String modifyBy;

    @Column(columnDefinition = "TINYINT(1) default 0", nullable = false)
    @Builder.Default
    private Boolean deleted = Boolean.FALSE;

}
