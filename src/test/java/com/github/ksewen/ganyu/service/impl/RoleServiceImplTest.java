package com.github.ksewen.ganyu.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.constant.AuthenticationConstants;
import com.github.ksewen.ganyu.constant.ErrorMessageConstants;
import com.github.ksewen.ganyu.domain.Role;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.mapper.RoleMapper;
import com.github.ksewen.ganyu.service.RoleService;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author ksewen
 * @date 03.06.2023 13:41
 */
@SpringBootTest(classes = RoleServiceImpl.class)
class RoleServiceImplTest {

  @Autowired private RoleService roleService;

  @MockBean private RoleMapper roleMapper;

  @Test
  void checkSuccess() {}

  @Test
  void checkWithoutAuthorization() {
    when(this.roleService.findByUserId(anyLong()))
        .thenReturn(
            Arrays.asList(
                Role.builder().id(2L).name(AuthenticationConstants.USER_ROLE_NAME).build()));
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class, () -> this.roleService.checkAdministrator(2L));
    assertThat(exception)
        .matches(e -> ResultCode.ACCESS_DENIED.equals(e.getCode()))
        .matches(e -> ErrorMessageConstants.NOT_ADMINISTRATOR_ERROR_MESSAGE.equals(e.getMessage()));
  }
}
