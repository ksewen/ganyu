package com.github.ksewen.ganyu.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.github.ksewen.ganyu.constant.ParameterConstants;
import com.github.ksewen.ganyu.domain.User;
import com.github.ksewen.ganyu.dto.request.UserModifyRequest;
import com.github.ksewen.ganyu.dto.response.UserInfoResponse;
import com.github.ksewen.ganyu.dto.response.base.PageResult;
import com.github.ksewen.ganyu.dto.response.base.Result;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.model.UserModifyModel;
import com.github.ksewen.ganyu.model.UserSearchModel;
import com.github.ksewen.ganyu.security.Authentication;
import com.github.ksewen.ganyu.service.AdminService;
import com.github.ksewen.ganyu.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 10.05.2023 12:24
 */
@RestController
@RequestMapping("/admin/user")
@SecurityRequirement(name = "jwt-auth")
@RequiredArgsConstructor
public class AdminUserController implements LoggingController {

    private final String NAME = "administrator user management";

    private final AdminService adminService;

    private final UserService userService;

    private final Authentication authentication;

    private final BeanMapperHelpers beanMapperHelpers;

    @Operation(summary = "administrator modify another user")
    @PostMapping("/modify")
    public Result<UserInfoResponse> modify(@Valid @RequestBody UserModifyRequest request) {
        UserModifyModel userModifyModel = this.beanMapperHelpers.createAndCopyProperties(request,
                UserModifyModel.class);
        User user = this.adminService.modify(userModifyModel, this.authentication.getUserId());
        return Result.success(this.beanMapperHelpers.createAndCopyProperties(user, UserInfoResponse.class));
    }

    @Operation(summary = "administrator search a list of users")
    @PostMapping("/list")
    public PageResult<List<UserInfoResponse>> list(@RequestParam(required = false) String username,
                                                   @RequestParam(required = false) String nickname,
                                                   @RequestParam(required = false) String email,
                                                   @RequestParam(required = false) String mobile,
                                                   @RequestParam(required = false) LocalDateTime createTimeAfter,
                                                   @RequestParam(required = false) LocalDateTime createTimeBefore,
                                                   @RequestParam(required = false) LocalDateTime modifyTimeAfter,
                                                   @RequestParam(required = false) LocalDateTime modifyTimeBefore,
                                                   @RequestParam(required = false, defaultValue = ParameterConstants.DEFAULT_INDEX_VALUE) Integer index,
                                                   @RequestParam(required = false, defaultValue = ParameterConstants.DEFAULT_COUNT_VALUE) Integer count) {
        UserSearchModel model = UserSearchModel.builder().username(username).nickname(nickname).email(email)
                .mobile(mobile).createTimeAfter(createTimeAfter).createTimeBefore(createTimeBefore)
                .modifyTimeAfter(modifyTimeAfter).modifyTimeBefore(modifyTimeBefore).build();
        Page<User> page = this.userService.findAByConditions(model, index, count);
        return PageResult.success(
                page.getContent().stream()
                        .map(x -> this.beanMapperHelpers.createAndCopyProperties(x, UserInfoResponse.class))
                        .collect(Collectors.toList()),
                page.getPageable().getPageNumber(), page.getContent().size(), page.getTotalElements());
    }

    @Override
    public String name() {
        return this.NAME;
    }
}
