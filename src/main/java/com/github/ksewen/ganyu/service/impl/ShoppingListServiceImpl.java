package com.github.ksewen.ganyu.service.impl;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.constant.ErrorMessageConstants;
import com.github.ksewen.ganyu.domain.ShoppingList;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.helper.SpecificationHelpers;
import com.github.ksewen.ganyu.mapper.ShoppingListMapper;
import com.github.ksewen.ganyu.model.ShoppingListInsertModel;
import com.github.ksewen.ganyu.model.ShoppingListItemQueryModel;
import com.github.ksewen.ganyu.model.ShoppingListSearchModel;
import com.github.ksewen.ganyu.service.RoleService;
import com.github.ksewen.ganyu.service.ShoppingListItemService;
import com.github.ksewen.ganyu.service.ShoppingListService;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author ksewen
 * @date 07.06.2023 12:32
 */
@Service
@RequiredArgsConstructor
public class ShoppingListServiceImpl implements ShoppingListService {

  private final ShoppingListMapper shoppingListMapper;

  private final ShoppingListItemService shoppingListItemService;

  private final RoleService roleService;
  private final SpecificationHelpers specificationHelpers;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ShoppingList save(ShoppingListInsertModel model) {
    ShoppingList insert =
        ShoppingList.builder()
            .name(model.getName())
            .userId(model.getUserId())
            .description(model.getDescription())
            .build();
    ShoppingList result = this.shoppingListMapper.saveAndFlush(insert);
    if (!CollectionUtils.isEmpty(model.getItemIds())) {
      this.shoppingListItemService.add(model.getItemIds(), model.getUserId(), insert.getId());
    }
    return result;
  }

  @Override
  public ShoppingList findById(long id, long operationUserId) {
    ShoppingList record =
        this.shoppingListMapper
            .findById(id)
            .orElseThrow(() -> new CommonException(ResultCode.NOT_FOUND));
    if (operationUserId != record.getUserId()) {
      this.roleService.checkAdministrator(operationUserId);
    }
    return record;
  }

  @Override
  public ShoppingList markFinished(long id, long operationUserId) {
    ShoppingList record =
        this.shoppingListMapper
            .findById(id)
            .orElseThrow(() -> new CommonException(ResultCode.NOT_FOUND));
    if (operationUserId != record.getUserId()) {
      throw new CommonException(
          ResultCode.ACCESS_DENIED, ErrorMessageConstants.SHARE_RECORD_OF_OTHER_USER_ERROR_MESSAGE);
    }
    record.setFinished(Boolean.TRUE);
    return this.shoppingListMapper.saveAndFlush(record);
  }

  @Override
  public Page<ShoppingList> findAllByConditions(
      ShoppingListSearchModel model, int index, int count) {
    return this.shoppingListMapper.findAll(
        (Specification<ShoppingList>)
            (root, query, criteriaBuilder) -> {
              List<Predicate> list = new ArrayList<>();
              if (model.getUserId() != null) {
                list.add(
                    criteriaBuilder.equal(root.get("userId").as(Long.class), model.getUserId()));
              }
              if (StringUtils.hasLength(model.getName())) {
                list.add(
                    criteriaBuilder.like(
                        root.get("name").as(String.class),
                        this.specificationHelpers.generateFullFuzzyKeyword(model.getName())));
              }
              if (model.getFinished() != null) {
                list.add(
                    criteriaBuilder.equal(
                        root.get("finished").as(Boolean.class), model.getFinished()));
              }

              this.specificationHelpers.buildTimeRangeCondition(list, model, root, criteriaBuilder);

              Predicate[] array = new Predicate[list.size()];
              Predicate[] predicates = list.toArray(array);
              return criteriaBuilder.and(predicates);
            },
        PageRequest.of(index, count, Sort.by("createTime").descending()));
  }

  @Override
  public Page<ShoppingListItemQueryModel> findAllItemsByShoppingListId(
      long shoppingListId, long operationUserId, int index, int count) {
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
    return this.shoppingListItemService.findAllByShoppingListId(shoppingListId, index, count);
  }
}
