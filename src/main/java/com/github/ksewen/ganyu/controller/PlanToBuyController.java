package com.github.ksewen.ganyu.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.github.ksewen.ganyu.domain.PlanToBuy;
import com.github.ksewen.ganyu.dto.request.PlanToBuyRequest;
import com.github.ksewen.ganyu.dto.response.PlanToBuyResponse;
import com.github.ksewen.ganyu.dto.response.base.PageResult;
import com.github.ksewen.ganyu.dto.response.base.Result;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.helper.BusinessHelpers;
import com.github.ksewen.ganyu.model.PlanToBuyModel;
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
    public Result<PlanToBuyResponse> add(@Valid @RequestBody PlanToBuyRequest request) {
        PlanToBuyModel model = this.beanMapperHelpers.createAndCopyProperties(request, PlanToBuyModel.class);
        model.setUserId(this.authentication.getUserId());
        PlanToBuy save = this.planToBuyService.save(model);
        PlanToBuyResponse response = this.beanMapperHelpers.createAndCopyProperties(save, PlanToBuyResponse.class);
        if (StringUtils.hasLength(save.getBusinessType())) {
            response.setBusinessType(this.businessHelpers.stringCommaSeparatedToList(save.getBusinessType()));
        }
        return Result.success(response);
    }

    @Operation(summary = "list all the things want to buy")
    @GetMapping("/list")
    public PageResult<List<PlanToBuyResponse>> list(@RequestParam(required = false) @NotNull(message = "{page.index.null}") Integer index,
                                                    @RequestParam(required = false) @NotNull(message = "{page.count.null}") Integer count) {
        Page<PlanToBuy> page = this.planToBuyService.findAllByUserId(this.authentication.getUserId(), index, count);
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
