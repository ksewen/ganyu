package com.github.ksewen.ganyu.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import com.github.ksewen.ganyu.domain.UserDomain;

@Mapper
public interface UserExtMapper {

    @Select({
            "select",
            " u.id, u.username, u.nickname, u.email, u.password, u.mobile, u.avatar_url, u.create_time, u.create_by, u.update_time, u.update_by, u.ist_delete, r.id, r.name",
            " from user as u",
            " LEFT JOIN role_user as sru on u.id = sru.userId",
            " LEFT JOIN role as r on sru.roleId = r.id",
            "where identity = #{identity,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.VARCHAR, id = true),
            @Result(column = "username", property = "username", jdbcType = JdbcType.VARCHAR),
            @Result(column = "nickname", property = "nickname", jdbcType = JdbcType.VARCHAR),
            @Result(column = "email", property = "email", jdbcType = JdbcType.VARCHAR),
            @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
            @Result(column = "mobile", property = "mobile", jdbcType = JdbcType.VARCHAR),
            @Result(column = "avatar_url", property = "avatarUrl", jdbcType = JdbcType.VARCHAR),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.DATE),
            @Result(column = "create_by", property = "createBy", jdbcType = JdbcType.DATE),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.DATE),
            @Result(column = "update_by", property = "updateBy", jdbcType = JdbcType.DATE),
            @Result(column = "ist_delete", property = "istDelete", jdbcType = JdbcType.TINYINT),
            @Result(property = "roles", column = "id", many = @Many(select = "com.github.ksewen.ganyu.mapper.RoleExtMapper.selectRolesByUserId"))
    })
    UserDomain loginByIdentity(String identity);
}
