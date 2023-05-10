package com.github.ksewen.ganyu.domain;

import com.github.ksewen.ganyu.generate.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class UserDomain {

    private Integer id;

    private String username;

    private String nickname;

    private String email;

    private String password;

    private String mobile;

    private String avatarUrl;

    private Date createTime;

    private Date createBy;

    private Date updateTime;

    private Date updateBy;

    private Boolean istDelete;

    private List<Role> roles;

}