package com.github.ksewen.ganyu.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author ksewen
 * @date 02.08.2023 14:25
 */
@SpringBootTest(classes = ShoppingListItemServiceImpl.class)
class ShoppingListItemServiceImplTest {

  @Autowired private ShoppingListItemServiceImpl shoppingListItemService;

  @MockBean private PlanToBuyService planToBuyService;

  @MockBean private ShoppingListItemMapper shoppingListItemMapper;

  @MockBean private RoleService roleService;

  @MockBean private ShoppingListMapper shoppingListMapper;

  private long USER_ID = 1L;

  private long SHOPPING_LIST_ID = 7L;

  @Test
  void add() {
    when(this.planToBuyService.findByUserIdAndIds(anyLong(), anyList()))
        .thenReturn(
            Arrays.asList(
                PlanToBuy.builder().id(1L).userId(this.USER_ID).build(),
                PlanToBuy.builder().id(2L).userId(this.USER_ID).build()));
    when(this.shoppingListItemMapper.saveAllAndFlush(
            argThat(
                list ->
                    ((List<ShoppingListItem>) list).size() == 2
                        && ((List<ShoppingListItem>) list).get(0).getPlanToBuyId() == 1L
                        && ((List<ShoppingListItem>) list).get(0).getShoppingListId() == 7L
                        && ((List<ShoppingListItem>) list).get(1).getPlanToBuyId() == 2L
                        && ((List<ShoppingListItem>) list).get(1).getShoppingListId() == 7L)))
        .thenReturn(
            Arrays.asList(
                ShoppingListItem.builder()
                    .id(1L)
                    .shoppingListId(this.SHOPPING_LIST_ID)
                    .planToBuyId(1L)
                    .build(),
                ShoppingListItem.builder()
                    .id(2L)
                    .shoppingListId(this.SHOPPING_LIST_ID)
                    .planToBuyId(2L)
                    .build()));
    List<ShoppingListItem> result =
        this.shoppingListItemService.add(
            Arrays.asList(1L, 2L), this.USER_ID, this.SHOPPING_LIST_ID);
    assertThat(result).isNotNull().matches(l -> l.size() == 2);
  }

  @Test
  void addWithEmptyCollectionOfId() {
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class,
            () -> this.shoppingListItemService.add(null, 1L, this.SHOPPING_LIST_ID));
    assertThat(exception).matches(e -> ResultCode.PARAM_INVALID.equals(e.getCode()));
  }

  @Test
  void addWithNotExistPlanToBuy() {
    when(this.planToBuyService.findByUserIdAndIds(anyLong(), anyList()))
        .thenReturn(Arrays.asList(PlanToBuy.builder().id(1L).userId(this.USER_ID).build()));
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class,
            () ->
                this.shoppingListItemService.add(
                    Arrays.asList(1L, 2L), this.USER_ID, this.SHOPPING_LIST_ID));
    assertThat(exception)
        .matches(
            e ->
                ResultCode.NOT_FOUND.equals(e.getCode())
                    && ErrorMessageConstants.PLAN_TO_BUY_NOT_FOUND_ERROR_MESSAGE.equals(
                        e.getMessage()));
  }

  @Test
  void delete() {
    when(this.shoppingListItemMapper.findAllById(anyList()))
        .thenReturn(
            Arrays.asList(
                ShoppingListItem.builder().shoppingListId(this.SHOPPING_LIST_ID).build(),
                ShoppingListItem.builder().shoppingListId(this.SHOPPING_LIST_ID).build(),
                ShoppingListItem.builder().shoppingListId(3L).build()));
    when(this.shoppingListMapper.findAllById(
            argThat(
                i ->
                    ((Set<Long>) i).size() == 2
                        && ((Set<Long>) i).contains(this.SHOPPING_LIST_ID)
                        && ((Set<Long>) i).contains(3L))))
        .thenReturn(
            Arrays.asList(
                ShoppingList.builder().id(this.SHOPPING_LIST_ID).userId(this.USER_ID).build(),
                ShoppingList.builder().id(3L).userId(this.USER_ID).build()));
    this.shoppingListItemService.delete(Arrays.asList(1L, 2L, 3L), this.USER_ID);
    verify(this.shoppingListItemMapper, times(1))
        .findAllById(argThat(list -> ((List) list).size() == 3));
    verify(this.shoppingListMapper, times(1))
        .findAllById(argThat(set -> ((Set<Long>) set).size() == 2));
    verify(this.roleService, times(0)).checkAdministrator(anyLong());
    verify(this.shoppingListItemMapper, times(1))
        .deleteAllById(
            argThat(
                list ->
                    ((List<Long>) list).size() == 3
                        && ((List<Long>) list).get(0) == 1L
                        && ((List<Long>) list).get(1) == 2L
                        && ((List<Long>) list).get(2) == 3L));
  }

  @Test
  void deleteWithNotExistId() {
    when(this.shoppingListItemMapper.findAllById(anyList()))
        .thenReturn(
            Arrays.asList(
                ShoppingListItem.builder().shoppingListId(this.SHOPPING_LIST_ID).build(),
                ShoppingListItem.builder().shoppingListId(this.SHOPPING_LIST_ID).build(),
                ShoppingListItem.builder().shoppingListId(3L).build()));
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class,
            () -> this.shoppingListItemService.delete(Arrays.asList(1L, 2L), this.USER_ID));
    assertThat(exception)
        .matches(
            e ->
                ResultCode.NOT_FOUND.equals(e.getCode())
                    && ErrorMessageConstants.SHOPPING_LIST_ITEM_NOT_FOUND_ERROR_MESSAGE.equals(
                        e.getMessage()));
  }

  @Test
  void deleteWithNotExistShoppingList() {
    when(this.shoppingListItemMapper.findAllById(anyList()))
        .thenReturn(
            Arrays.asList(
                ShoppingListItem.builder().shoppingListId(this.SHOPPING_LIST_ID).build(),
                ShoppingListItem.builder().shoppingListId(this.SHOPPING_LIST_ID).build(),
                ShoppingListItem.builder().shoppingListId(3L).build()));
    when(this.shoppingListMapper.findAllById(
            argThat(
                i ->
                    ((Set<Long>) i).size() == 2
                        && ((Set<Long>) i).contains(this.SHOPPING_LIST_ID)
                        && ((Set<Long>) i).contains(3L))))
        .thenReturn(
            Arrays.asList(
                ShoppingList.builder().id(this.SHOPPING_LIST_ID).userId(this.USER_ID).build()));
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class,
            () -> this.shoppingListItemService.delete(Arrays.asList(1L, 2L, 3L), this.USER_ID));
    assertThat(exception)
        .matches(
            e ->
                ResultCode.NOT_FOUND.equals(e.getCode())
                    && ErrorMessageConstants.SHOPPING_LIST_NOT_FOUND_ERROR_MESSAGE.equals(
                        e.getMessage()));
  }

  @Test
  void deleteOthersRecord() {
    when(this.shoppingListItemMapper.findAllById(anyList()))
        .thenReturn(
            Arrays.asList(
                ShoppingListItem.builder().shoppingListId(this.SHOPPING_LIST_ID).build(),
                ShoppingListItem.builder().shoppingListId(this.SHOPPING_LIST_ID).build(),
                ShoppingListItem.builder().shoppingListId(3L).build()));
    when(this.shoppingListMapper.findAllById(
            argThat(
                i ->
                    ((Set<Long>) i).size() == 2
                        && ((Set<Long>) i).contains(this.SHOPPING_LIST_ID)
                        && ((Set<Long>) i).contains(3L))))
        .thenReturn(
            Arrays.asList(
                ShoppingList.builder().id(this.SHOPPING_LIST_ID).userId(9L).build(),
                ShoppingList.builder().id(3L).userId(this.USER_ID).build()));
    this.shoppingListItemService.delete(Arrays.asList(1L, 2L, 3L), this.USER_ID);
    verify(this.shoppingListItemMapper, times(1))
        .findAllById(argThat(list -> ((List) list).size() == 3));
    verify(this.shoppingListMapper, times(1))
        .findAllById(argThat(set -> ((Set<Long>) set).size() == 2));
    verify(this.roleService, times(1)).checkAdministrator(this.USER_ID);
    verify(this.shoppingListItemMapper, times(1)).deleteAllById(anyList());
  }

  @Test
  void findById() {
    when(this.shoppingListItemMapper.findByIdJoinWithPlanToBuy(anyLong()))
        .thenReturn(
            Optional.of(
                ShoppingListItemQueryModel.builder()
                    .id(1L)
                    .shoppingListId(this.SHOPPING_LIST_ID)
                    .planToBuyId(2L)
                    .build()));
    when(this.shoppingListMapper.findById(this.SHOPPING_LIST_ID))
        .thenReturn(
            Optional.of(
                ShoppingList.builder().id(this.SHOPPING_LIST_ID).userId(this.USER_ID).build()));
    ShoppingListItemQueryModel result = this.shoppingListItemService.findById(1L, 1L);
    assertThat(result)
        .isNotNull()
        .matches(
            s ->
                1L == s.getId()
                    && this.SHOPPING_LIST_ID == s.getShoppingListId()
                    && 2L == s.getPlanToBuyId());
  }

  @Test
  void findByIdWithInvalidId() {
    when(this.shoppingListItemMapper.findByIdJoinWithPlanToBuy(anyLong()))
        .thenReturn(Optional.ofNullable(null));
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class, () -> this.shoppingListItemService.findById(1L, this.USER_ID));
    assertThat(exception)
        .matches(e -> ResultCode.NOT_FOUND.equals(e.getCode()))
        .matches(
            e ->
                ErrorMessageConstants.SHOPPING_LIST_ITEM_NOT_FOUND_ERROR_MESSAGE.equals(
                    e.getMessage()));
  }

  @Test
  void findByIdWithInvalidShoppingList() {
    when(this.shoppingListItemMapper.findByIdJoinWithPlanToBuy(anyLong()))
        .thenReturn(
            Optional.of(
                ShoppingListItemQueryModel.builder()
                    .id(1L)
                    .shoppingListId(this.SHOPPING_LIST_ID)
                    .planToBuyId(2L)
                    .build()));
    when(this.shoppingListMapper.findById(this.SHOPPING_LIST_ID))
        .thenReturn(Optional.ofNullable(null));
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class, () -> this.shoppingListItemService.findById(1L, this.USER_ID));
    assertThat(exception)
        .matches(e -> ResultCode.NOT_FOUND.equals(e.getCode()))
        .matches(
            e ->
                ErrorMessageConstants.SHOPPING_LIST_NOT_FOUND_ERROR_MESSAGE.equals(e.getMessage()));
  }

  @Test
  void findByIdToReadOthersRecordButNotAdmin() {
    when(this.shoppingListItemMapper.findByIdJoinWithPlanToBuy(anyLong()))
        .thenReturn(
            Optional.of(
                ShoppingListItemQueryModel.builder()
                    .id(1L)
                    .shoppingListId(this.SHOPPING_LIST_ID)
                    .planToBuyId(2L)
                    .build()));
    when(this.shoppingListMapper.findById(this.SHOPPING_LIST_ID))
        .thenReturn(
            Optional.of(ShoppingList.builder().id(this.SHOPPING_LIST_ID).userId(10L).build()));
    this.shoppingListItemService.findById(1L, this.USER_ID);
    verify(this.roleService, times(1)).checkAdministrator(this.USER_ID);
  }

  @Test
  void markBought() {
    when(this.shoppingListItemMapper.findById(anyLong()))
        .thenReturn(
            Optional.of(
                ShoppingListItem.builder()
                    .id(1L)
                    .shoppingListId(this.SHOPPING_LIST_ID)
                    .bought(false)
                    .build()));
    when(shoppingListMapper.findById(this.SHOPPING_LIST_ID))
        .thenReturn(
            Optional.of(
                ShoppingList.builder().id(this.SHOPPING_LIST_ID).userId(this.USER_ID).build()));
    when(this.shoppingListItemMapper.findByIdJoinWithPlanToBuy(anyLong()))
        .thenReturn(
            Optional.of(
                ShoppingListItemQueryModel.builder()
                    .id(1L)
                    .shoppingListId(this.SHOPPING_LIST_ID)
                    .bought(true)
                    .build()));
    ShoppingListItemQueryModel result = this.shoppingListItemService.markBought(1L, this.USER_ID);
    assertThat(result)
        .isNotNull()
        .matches(
            s ->
                s.getId() == 1L && s.getShoppingListId() == this.SHOPPING_LIST_ID && s.getBought());
    verify(this.shoppingListItemMapper, times(1))
        .saveAndFlush(
            argThat(
                record ->
                    record.getShoppingListId() == this.SHOPPING_LIST_ID
                        && record.getBought()
                        && record.getId() == 1L));
  }

  @Test
  void markBoughtWithNotExistId() {
    when(this.shoppingListItemMapper.findById(anyLong())).thenReturn(Optional.ofNullable(null));
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class, () -> this.shoppingListItemService.markBought(1L, this.USER_ID));
    assertThat(exception)
        .matches(
            e ->
                ResultCode.NOT_FOUND.equals(e.getCode())
                    && ErrorMessageConstants.SHOPPING_LIST_ITEM_NOT_FOUND_ERROR_MESSAGE.equals(
                        e.getMessage()));
  }

  @Test
  void markBoughtWithNotExistShoppingList() {
    when(this.shoppingListItemMapper.findById(anyLong()))
        .thenReturn(
            Optional.of(
                ShoppingListItem.builder()
                    .id(1L)
                    .shoppingListId(this.SHOPPING_LIST_ID)
                    .bought(false)
                    .build()));
    when(shoppingListMapper.findById(this.SHOPPING_LIST_ID)).thenReturn(Optional.ofNullable(null));
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class, () -> this.shoppingListItemService.markBought(1L, this.USER_ID));
    assertThat(exception)
        .matches(
            e ->
                ResultCode.NOT_FOUND.equals(e.getCode())
                    && ErrorMessageConstants.SHOPPING_LIST_NOT_FOUND_ERROR_MESSAGE.equals(
                        e.getMessage()));
  }

  @Test
  void markBoughtOthersRecord() {
    when(this.shoppingListItemMapper.findById(anyLong()))
        .thenReturn(
            Optional.of(
                ShoppingListItem.builder()
                    .id(1L)
                    .shoppingListId(this.SHOPPING_LIST_ID)
                    .bought(false)
                    .build()));
    when(shoppingListMapper.findById(this.SHOPPING_LIST_ID))
        .thenReturn(
            Optional.of(
                ShoppingList.builder().id(this.SHOPPING_LIST_ID).userId(this.USER_ID).build()));
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class, () -> this.shoppingListItemService.markBought(1L, 3L));
    assertThat(exception)
        .matches(
            e ->
                ResultCode.ACCESS_DENIED.equals(e.getCode())
                    && ErrorMessageConstants.NOT_ADMINISTRATOR_ERROR_MESSAGE.equals(
                        e.getMessage()));
  }
}
