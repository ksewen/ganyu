package com.github.ksewen.ganyu.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.github.ksewen.ganyu.constant.ParameterConstants;
import com.github.ksewen.ganyu.domain.PlanToBuy;
import com.github.ksewen.ganyu.dto.request.PlanToBuyInsertRequest;
import com.github.ksewen.ganyu.dto.request.PlanToBuyModifyRequest;
import com.github.ksewen.ganyu.dto.response.PlanToBuyResponse;
import com.github.ksewen.ganyu.dto.response.base.PageResult;
import com.github.ksewen.ganyu.dto.response.base.Result;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.helper.BusinessHelpers;
import com.github.ksewen.ganyu.model.PlanToBuyInsertModel;
import com.github.ksewen.ganyu.model.PlanToBuyModifyModel;
import com.github.ksewen.ganyu.model.PlanToBuySearchModel;
import com.github.ksewen.ganyu.security.Authentication;
import com.github.ksewen.ganyu.service.PlanToBuyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 31.05.2023 13:50
 */
@RestController
@RequestMapping("/plan-to-buy")
@SecurityRequirement(name = "jwt-auth")
@RequiredArgsConstructor
public class PlanToBuyController implements LoggingController {

    private final PlanToBuyService planToBuyService;

    private final BeanMapperHelpers beanMapperHelpers;

    private final Authentication authentication;

    private final BusinessHelpers businessHelpers;

    private final String NAME = "plan to buy";

    @Operation(summary = "add something to buy")
    @PostMapping("/add")
    public Result<PlanToBuyResponse> add(@Valid @RequestBody PlanToBuyInsertRequest request) {
        PlanToBuyInsertModel model = this.beanMapperHelpers.createAndCopyProperties(request,
                PlanToBuyInsertModel.class);
        model.setUserId(this.authentication.getUserId());
        PlanToBuy save = this.planToBuyService.save(model);
        PlanToBuyResponse response = this.beanMapperHelpers.createAndCopyProperties(save, PlanToBuyResponse.class);
        if (StringUtils.hasLength(save.getBusinessType())) {
            response.setBusinessType(this.businessHelpers.stringCommaSeparatedToList(save.getBusinessType()));
        }
        return Result.success(response);
    }

    @Operation(summary = "modify something to buy")
    @PostMapping("/modify")
    public Result<PlanToBuyResponse> modify(@Valid @RequestBody PlanToBuyModifyRequest request) {
        PlanToBuyModifyModel model = this.beanMapperHelpers.createAndCopyProperties(request,
                PlanToBuyModifyModel.class);
        PlanToBuy modify = this.planToBuyService.modify(model, this.authentication.getUserId());
        PlanToBuyResponse response = this.beanMapperHelpers.createAndCopyProperties(modify, PlanToBuyResponse.class);
        if (StringUtils.hasLength(modify.getBusinessType())) {
            response.setBusinessType(this.businessHelpers.stringCommaSeparatedToList(modify.getBusinessType()));
        }
        return Result.success(response);
    }

    @Operation(summary = "delete something to buy")
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestParam(required = false) @NotNull(message = "{plan.to.buy.id.null}") Long id) {
        this.planToBuyService.delete(id, this.authentication.getUserId());
        return Result.success(Boolean.TRUE);
    }

    @Operation(summary = "list all the things want to buy")
    @GetMapping("/list")
    public PageResult<List<PlanToBuyResponse>> list(@RequestParam(required = false) String name,
                                                    @RequestParam(required = false) String brand,
                                                    @RequestParam(required = false) Long shareFrom,
                                                    @RequestParam(required = false) Boolean assigned,
                                                    @RequestParam(required = false) String businessType,
                                                    @RequestParam(required = false, defaultValue = ParameterConstants.DEFAULT_INDEX_VALUE) Integer index,
                                                    @RequestParam(required = false, defaultValue = ParameterConstants.DEFAULT_COUNT_VALUE) Integer count) {
        PlanToBuySearchModel model = PlanToBuySearchModel.builder().userId(this.authentication.getUserId()).name(name)
                .brand(brand).shareFrom(shareFrom).assigned(assigned).businessType(businessType).build();
        Page<PlanToBuy> page = this.planToBuyService.findAByConditions(model, index, count);
        return PageResult.success(page.getContent().stream().map(x -> {
            PlanToBuyResponse item = PlanToBuyResponse.builder().id(x.getId()).userId(x.getUserId()).brand(x.getBrand())
                    .shareFrom(x.getShareFrom()).assigned(x.getAssigned()).name(x.getName())
                    .description(x.getDescription()).imageUrl(x.getImageUrl()).build();
            if (StringUtils.hasLength(x.getBusinessType())) {
                item.setBusinessType(this.businessHelpers.stringCommaSeparatedToList(x.getBusinessType()));
            }
            return item;
        }).collect(Collectors.toList()), page.getPageable().getPageNumber(), page.getPageable().getPageSize(),
                page.getTotalElements());
    }

    @Override
    public String name() {
        return this.NAME;
    }
}
