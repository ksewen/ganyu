package com.github.ksewen.ganyu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.github.ksewen.ganyu.generate.domain.Role;

@Mapper
public interface RoleExtMapper {

    @Select({"SELECT r.id, r.name, r.create_time, r.create_by, r.update_time, r.update_by, r.ist_delete",
            " FROM role as r",
            " INNER JOIN role_user as ru on r.id = ru.roleId",
            " WHERE ru.userId =#{userId}"})
    List<Role> selectRolesByUserId(@Param("userId") String userId);
}
