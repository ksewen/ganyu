package com.github.ksewen.ganyu.domain;

import java.time.LocalDateTime;

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
 * @date 11.05.2023 07:27
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "user")
@EntityListeners(value = AuditingEntityListener.class)
@SQLDelete(sql = "update user set deleted = true where id = ?")
@Where(clause = "deleted = false")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(64)")
    private String username;

    @Column(columnDefinition = "VARCHAR(64)")
    private String nickname;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String password;

    @Column(columnDefinition = "VARCHAR(20)")
    private String mobile;

    @Column(columnDefinition = "VARCHAR(255)")
    private String avatarUrl;

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
