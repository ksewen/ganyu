package com.github.ksewen.ganyu.service.impl;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.constant.ErrorMessageConstants;
import com.github.ksewen.ganyu.domain.PlanToBuy;
import com.github.ksewen.ganyu.domain.ShoppingList;
import com.github.ksewen.ganyu.domain.ShoppingListItem;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.mapper.ShoppingListItemMapper;
import com.github.ksewen.ganyu.mapper.ShoppingListMapper;
import com.github.ksewen.ganyu.model.ShoppingListItemQueryModel;
import com.github.ksewen.ganyu.service.PlanToBuyService;
import com.github.ksewen.ganyu.service.RoleService;
import com.github.ksewen.ganyu.service.ShoppingListItemService;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author ksewen
 * @date 07.06.2023 16:09
 */
@Service
@RequiredArgsConstructor
public class ShoppingListItemServiceImpl implements ShoppingListItemService {

  private final PlanToBuyService planToBuyService;

  private final ShoppingListItemMapper shoppingListItemMapper;

  private final RoleService roleService;

  private final ShoppingListMapper shoppingListMapper;

  @Override
  public List<ShoppingListItem> add(List<Long> ids, long userId, long shoppingListId) {
    if (CollectionUtils.isEmpty(ids)) {
      throw new CommonException(ResultCode.PARAM_INVALID);
    }
    List<PlanToBuy> list = this.planToBuyService.findByUserIdAndIds(userId, ids);
    if (CollectionUtils.isEmpty(list) || list.size() != ids.size()) {
      throw new CommonException(
          ResultCode.NOT_FOUND, "can not found any records by given ids and user id");
    }
    List<ShoppingListItem> insert =
        list.stream()
            .map(
                x ->
                    ShoppingListItem.builder()
                        .shoppingListId(shoppingListId)
                        .planToBuyId(x.getId())
                        .build())
            .collect(Collectors.toList());
    return this.shoppingListItemMapper.saveAllAndFlush(insert);
  }

  @Override
  public void delete(List<Long> ids, long operationUserId) {
    List<ShoppingListItem> records = this.shoppingListItemMapper.findAllById(ids);
    if (CollectionUtils.isEmpty(records) || records.size() != ids.size()) {
      throw new CommonException(
          ResultCode.NOT_FOUND, ErrorMessageConstants.SHOPPING_LIST_ITEM_NOT_FOUND_ERROR_MESSAGE);
    }
    Set<Long> listIds =
        records.stream()
            .collect(Collectors.toMap(ShoppingListItem::getShoppingListId, s -> s, (s1, s2) -> s1))
            .keySet();
    List<ShoppingList> listRecords = this.shoppingListMapper.findAllById(listIds);
    if (CollectionUtils.isEmpty(listRecords) || listRecords.size() != listIds.size()) {
      throw new CommonException(
          ResultCode.NOT_FOUND, ErrorMessageConstants.SHOPPING_LIST_NOT_FOUND_ERROR_MESSAGE);
    }
    boolean deleteOthers =
        listRecords.stream().anyMatch(s -> !Objects.equals(operationUserId, s.getUserId()));
    if (deleteOthers) {
      this.roleService.checkAdministrator(operationUserId);
    }
    this.shoppingListItemMapper.deleteAllById(ids);
  }

  @Override
  public ShoppingListItemQueryModel findById(long id, long operationUserId) {
    ShoppingListItemQueryModel result =
        this.shoppingListItemMapper
            .findByIdJoinWithPlanToBuy(id)
            .orElseThrow(
                () ->
                    new CommonException(
                        ResultCode.NOT_FOUND,
                        ErrorMessageConstants.SHOPPING_LIST_ITEM_NOT_FOUND_ERROR_MESSAGE));
    long shoppingListId = result.getShoppingListId();
    ShoppingList shoppingList =
        this.shoppingListMapper
            .findById(shoppingListId)
            .orElseThrow(
                () ->
                    new CommonException(
                        ResultCode.NOT_FOUND,
                        ErrorMessageConstants.SHOPPING_LIST_NOT_FOUND_ERROR_MESSAGE));
    if (!Objects.equals(shoppingList.getUserId(), operationUserId)) {
      this.roleService.checkAdministrator(operationUserId);
    }
    return result;
  }

  @Override
  public Page<ShoppingListItemQueryModel> findAllByShoppingListId(
      long shoppingListId, int index, int count) {
    return this.shoppingListItemMapper.findAllByShoppingListIdJoinWithPlanToBuy(
        shoppingListId, PageRequest.of(index, count, Sort.by("createTime").descending()));
  }

  @Override
  public ShoppingListItemQueryModel markBought(long id, long operationUserId) {
    ShoppingListItem record =
        this.shoppingListItemMapper
            .findById(id)
            .orElseThrow(
                () ->
                    new CommonException(
                        ResultCode.NOT_FOUND,
                        ErrorMessageConstants.SHOPPING_LIST_ITEM_NOT_FOUND_ERROR_MESSAGE));
    ShoppingList shoppingList =
        this.shoppingListMapper
            .findById(record.getShoppingListId())
            .orElseThrow(
                () ->
                    new CommonException(
                        ResultCode.NOT_FOUND,
                        ErrorMessageConstants.SHOPPING_LIST_NOT_FOUND_ERROR_MESSAGE));
    if (operationUserId != shoppingList.getUserId()) {
      throw new CommonException(
          ResultCode.ACCESS_DENIED, ErrorMessageConstants.NOT_ADMINISTRATOR_ERROR_MESSAGE);
    }
    record.setBought(Boolean.TRUE);
    this.shoppingListItemMapper.saveAndFlush(record);
    // FIXME: access the database again
    return this.shoppingListItemMapper.findByIdJoinWithPlanToBuy(id).get();
  }
}
