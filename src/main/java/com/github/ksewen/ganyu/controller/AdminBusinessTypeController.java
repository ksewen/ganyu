package com.github.ksewen.ganyu.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.github.ksewen.ganyu.domain.BusinessType;
import com.github.ksewen.ganyu.dto.request.BusinessTypeAddRequest;
import com.github.ksewen.ganyu.dto.response.BusinessTypeInfoResponse;
import com.github.ksewen.ganyu.dto.response.base.PageResult;
import com.github.ksewen.ganyu.dto.response.base.Result;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.service.BusinessTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 31.05.2023 14:54
 */
@RestController
@RequestMapping("/admin/business-type")
@SecurityRequirement(name = "jwt-auth")
@RequiredArgsConstructor
public class AdminBusinessTypeController implements LoggingController {

    private final BusinessTypeService businessTypeService;

    private final BeanMapperHelpers beanMapperHelpers;

    private final String NAME = "administrator business type management";

    @Operation(summary = "add business type")
    @PostMapping("/add")
    public Result<BusinessTypeInfoResponse> add(@Valid @RequestBody BusinessTypeAddRequest request) {
        BusinessType businessType = this.businessTypeService
                .save(BusinessType.builder().name(request.getName()).build());
        return Result
                .success(this.beanMapperHelpers.createAndCopyProperties(businessType, BusinessTypeInfoResponse.class));
    }

    @Operation(summary = "delete business type")
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestParam @NotNull(message = "{business.type.id.null}") @Min(1) Long id) {
        this.businessTypeService.delete(id);
        return Result.success(Boolean.TRUE);
    }

    @Operation(summary = "list all business type")
    @GetMapping("/list")
    public PageResult<List<BusinessTypeInfoResponse>> list(@RequestParam @NotNull(message = "{page.index.null}") Integer index,
                                                           @RequestParam @NotNull(message = "{page.count.null}") Integer count) {
        Page<BusinessType> page = this.businessTypeService.findAll(index, count);
        return PageResult.success(
                page.getContent().stream().map(x -> BusinessTypeInfoResponse.builder().id(x.getId()).name(x.getName()).build())
                        .collect(Collectors.toList()),
                page.getPageable().getPageNumber(), page.getPageable().getPageSize(), page.getTotalElements());
    }

    @Override
    public String name() {
        return this.NAME;
    }
}
