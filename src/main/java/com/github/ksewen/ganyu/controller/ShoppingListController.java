package com.github.ksewen.ganyu.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.github.ksewen.ganyu.constant.ParameterConstants;
import com.github.ksewen.ganyu.domain.ShoppingList;
import com.github.ksewen.ganyu.dto.request.ShoppingListInsertRequest;
import com.github.ksewen.ganyu.dto.response.ShoppingListResponse;
import com.github.ksewen.ganyu.dto.response.ShoppingListResponseItem;
import com.github.ksewen.ganyu.dto.response.base.PageResult;
import com.github.ksewen.ganyu.dto.response.base.Result;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.model.ShoppingListInsertModel;
import com.github.ksewen.ganyu.model.ShoppingListItemQueryModel;
import com.github.ksewen.ganyu.model.ShoppingListSearchModel;
import com.github.ksewen.ganyu.security.Authentication;
import com.github.ksewen.ganyu.service.ShoppingListService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 06.06.2023 17:14
 */
@RestController
@RequestMapping("/shopping-list")
@SecurityRequirement(name = "jwt-auth")
@RequiredArgsConstructor
public class ShoppingListController implements LoggingController {

    private final BeanMapperHelpers beanMapperHelpers;

    private final Authentication authentication;

    private final ShoppingListService shoppingListService;

    private final String NAME = "shopping list";

    @Operation(summary = "add a shopping list")
    @PostMapping("/add")
    public Result<ShoppingListResponse> add(@Valid @RequestBody ShoppingListInsertRequest request) {
        ShoppingListInsertModel model = this.beanMapperHelpers.createAndCopyProperties(request,
                ShoppingListInsertModel.class);
        model.setUserId(this.authentication.getUserId());
        ShoppingList save = this.shoppingListService.save(model);
        ShoppingListResponse response = this.beanMapperHelpers.createAndCopyProperties(save,
                ShoppingListResponse.class);
        return Result.success(response);
    }

    @Operation(summary = "list all the shopping list")
    @GetMapping("/list")
    public PageResult<List<ShoppingListResponse>> list(@RequestParam(required = false) String name,
                                                       @RequestParam(required = false) Boolean finished,
                                                       @RequestParam(required = false) LocalDateTime createTimeAfter,
                                                       @RequestParam(required = false) LocalDateTime createTimeBefore,
                                                       @RequestParam(required = false) LocalDateTime modifyTimeAfter,
                                                       @RequestParam(required = false) LocalDateTime modifyTimeBefore,
                                                       @RequestParam(required = false, defaultValue = ParameterConstants.DEFAULT_INDEX_VALUE) Integer index,
                                                       @RequestParam(required = false, defaultValue = ParameterConstants.DEFAULT_COUNT_VALUE) Integer count) {
        ShoppingListSearchModel model = ShoppingListSearchModel.builder().userId(this.authentication.getUserId())
                .name(name).finished(finished).createTimeAfter(createTimeAfter).createTimeBefore(createTimeBefore)
                .modifyTimeAfter(modifyTimeAfter).modifyTimeBefore(modifyTimeBefore).build();
        Page<ShoppingList> page = this.shoppingListService.findAllByConditions(model, index, count);
        return PageResult.success(
                page.getContent().stream()
                        .map(x -> this.beanMapperHelpers.createAndCopyProperties(x, ShoppingListResponse.class))
                        .collect(Collectors.toList()),
                page.getPageable().getPageNumber(), page.getContent().size(), page.getTotalElements());
    }

    @Operation(summary = "show the detail of a specific shopping list")
    @GetMapping("/detail")
    public Result<ShoppingListResponse> detail(@RequestParam(required = false) @NotNull(message = "{shopping.list.id.null}") Long id) {
        ShoppingList record = this.shoppingListService.findById(id, this.authentication.getUserId());
        ShoppingListResponse response = this.beanMapperHelpers.createAndCopyProperties(record,
                ShoppingListResponse.class);
        return Result.success(response);
    }

    @Operation(summary = "list all the shopping list items of the specific shopping list")
    @GetMapping("/items")
    public PageResult<List<ShoppingListResponseItem>> listByShoppingList(@RequestParam(required = false) Long shoppingListId,
                                                                         @RequestParam(required = false, defaultValue = ParameterConstants.DEFAULT_INDEX_VALUE) Integer index,
                                                                         @RequestParam(required = false, defaultValue = ParameterConstants.DEFAULT_COUNT_VALUE) Integer count) {
        Page<ShoppingListItemQueryModel> page = this.shoppingListService.findAllItemsByShoppingListId(shoppingListId,
                this.authentication.getUserId(), index, count);
        return PageResult
                .success(
                        page.getContent().stream()
                                .map(x -> this.beanMapperHelpers.createAndCopyProperties(x,
                                        ShoppingListResponseItem.class))
                                .collect(Collectors.toList()),
                        page.getPageable().getPageNumber(), page.getContent().size(), page.getTotalElements());
    }

    @Operation(summary = "mark a shopping list has finished")
    @PostMapping("/mark-finished")
    public Result<ShoppingListResponse> markFinished(@RequestParam(required = false) @NotNull(message = "{shopping.list.id.null}") Long id) {
        ShoppingList record = this.shoppingListService.markFinished(id, this.authentication.getUserId());
        ShoppingListResponse response = this.beanMapperHelpers.createAndCopyProperties(record,
                ShoppingListResponse.class);
        return Result.success(response);
    }

    @Override
    public String name() {
        return this.NAME;
    }
}
