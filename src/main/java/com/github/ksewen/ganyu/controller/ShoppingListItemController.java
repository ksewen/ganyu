package com.github.ksewen.ganyu.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.github.ksewen.ganyu.domain.ShoppingListItem;
import com.github.ksewen.ganyu.dto.request.ShoppingListItemDeleteRequest;
import com.github.ksewen.ganyu.dto.request.ShoppingListItemInsertRequest;
import com.github.ksewen.ganyu.dto.response.ShoppingListResponseItem;
import com.github.ksewen.ganyu.dto.response.base.Result;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.model.ShoppingListItemQueryModel;
import com.github.ksewen.ganyu.security.Authentication;
import com.github.ksewen.ganyu.service.ShoppingListItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 07.06.2023 16:15
 */
@RestController
@RequestMapping("/shopping-list/item")
@SecurityRequirement(name = "jwt-auth")
@RequiredArgsConstructor
@Validated
public class ShoppingListItemController implements LoggingController {

    private final ShoppingListItemService shoppingListItemService;

    private final Authentication authentication;

    private final BeanMapperHelpers beanMapperHelpers;

    private final String NAME = "shopping list item";

    @Override
    public String name() {
        return this.NAME;
    }

    @Operation(summary = "add shopping list items")
    @PostMapping("/add")
    public Result<List<ShoppingListResponseItem>> add(@Valid @RequestBody ShoppingListItemInsertRequest request) {
        List<ShoppingListItem> items = this.shoppingListItemService.add(request.getItemIds(),
                this.authentication.getUserId(), request.getShoppingListId());
        List<ShoppingListResponseItem> response = items.stream()
                .map(i -> this.beanMapperHelpers.createAndCopyProperties(i, ShoppingListResponseItem.class))
                .collect(Collectors.toList());
        return Result.success(response);
    }

    @Operation(summary = "delete shopping list items")
    @PostMapping("/delete")
    public Result<Boolean> delete(@Valid @RequestBody ShoppingListItemDeleteRequest request) {
        this.shoppingListItemService.delete(request.getIds(), this.authentication.getUserId());
        return Result.success(Boolean.TRUE);
    }

    @Operation(summary = "show the detail of a specific shopping list item")
    @GetMapping("/detail")
    public Result<ShoppingListResponseItem> detail(@RequestParam(required = false) @NotNull(message = "{shopping.list.item.id.null}") Long id) {
        ShoppingListItemQueryModel model = this.shoppingListItemService.findById(id, this.authentication.getUserId());
        ShoppingListResponseItem response = this.beanMapperHelpers.createAndCopyProperties(model,
                ShoppingListResponseItem.class);
        return Result.success(response);
    }

    @Operation(summary = "mark a item of the shopping list has bought")
    @PostMapping("/mark-bought")
    public Result<ShoppingListResponseItem> markBought(@RequestParam(required = false) @NotNull(message = "{plan.to.buy.id.null}") Long id) {
        ShoppingListItemQueryModel record = this.shoppingListItemService.markBought(id,
                this.authentication.getUserId());
        ShoppingListResponseItem response = this.beanMapperHelpers.createAndCopyProperties(record,
                ShoppingListResponseItem.class);
        return Result.success(response);
    }

}
