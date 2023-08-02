package com.github.ksewen.ganyu.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.constant.ErrorMessageConstants;
import com.github.ksewen.ganyu.domain.ShoppingList;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.helper.SpecificationHelpers;
import com.github.ksewen.ganyu.mapper.ShoppingListMapper;
import com.github.ksewen.ganyu.model.ShoppingListInsertModel;
import com.github.ksewen.ganyu.service.RoleService;
import com.github.ksewen.ganyu.service.ShoppingListItemService;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author ksewen
 * @date 02.08.2023 21:23
 */
@SpringBootTest(classes = ShoppingListServiceImpl.class)
class ShoppingListServiceImplTest {

  @Autowired private ShoppingListServiceImpl shoppingListService;

  @MockBean private ShoppingListMapper shoppingListMapper;

  @MockBean private ShoppingListItemService shoppingListItemService;

  @MockBean private RoleService roleService;

  @MockBean private SpecificationHelpers specificationHelpers;

  private String MOCK_NAME = "test-name";

  private String MOCK_DESCRIPTION = "test-description";

  private long MOCK_USER_ID = 1L;

  @Test
  void save() {
    ShoppingListInsertModel model =
        ShoppingListInsertModel.builder()
            .name(this.MOCK_NAME)
            .userId(this.MOCK_USER_ID)
            .description(this.MOCK_DESCRIPTION)
            .itemIds(Arrays.asList(1L, 2L))
            .build();
    when(this.shoppingListMapper.saveAndFlush(
            argThat(
                record ->
                    this.MOCK_NAME.equals(record.getName())
                        && this.MOCK_DESCRIPTION.equals(record.getDescription())
                        && this.MOCK_USER_ID == record.getUserId())))
        .thenReturn(
            ShoppingList.builder()
                .id(1L)
                .name(model.getName())
                .description(model.getDescription())
                .userId(model.getUserId())
                .build());
    ShoppingList result = this.shoppingListService.save(model);
    assertThat(result)
        .isNotNull()
        .matches(
            s ->
                s.getId() == 1L
                    && this.MOCK_NAME.equals(s.getName())
                    && this.MOCK_DESCRIPTION.equals(s.getDescription())
                    && this.MOCK_USER_ID == s.getUserId());
    verify(this.shoppingListItemService, times(1))
        .add(argThat(list -> list.size() == 2), eq(this.MOCK_USER_ID), eq(1L));
  }

  @Test
  void saveWithOutItems() {
    ShoppingListInsertModel model =
        ShoppingListInsertModel.builder()
            .name(this.MOCK_NAME)
            .userId(this.MOCK_USER_ID)
            .description(this.MOCK_DESCRIPTION)
            .build();
    when(this.shoppingListMapper.saveAndFlush(
            argThat(
                record ->
                    this.MOCK_NAME.equals(record.getName())
                        && this.MOCK_DESCRIPTION.equals(record.getDescription())
                        && this.MOCK_USER_ID == record.getUserId())))
        .thenReturn(
            ShoppingList.builder()
                .id(1L)
                .name(model.getName())
                .description(model.getDescription())
                .userId(model.getUserId())
                .build());
    ShoppingList result = this.shoppingListService.save(model);
    assertThat(result)
        .isNotNull()
        .matches(
            s ->
                s.getId() == 1L
                    && this.MOCK_NAME.equals(s.getName())
                    && this.MOCK_DESCRIPTION.equals(s.getDescription())
                    && this.MOCK_USER_ID == s.getUserId());
    verify(this.shoppingListItemService, times(0))
        .add(argThat(list -> list.size() == 2), eq(this.MOCK_USER_ID), eq(1L));
  }

  @Test
  void findById() {
    when(this.shoppingListMapper.findById(anyLong()))
        .thenReturn(Optional.of(ShoppingList.builder().id(1L).userId(this.MOCK_USER_ID).build()));
    ShoppingList result = this.shoppingListService.findById(1L, this.MOCK_USER_ID);
    assertThat(result)
        .isNotNull()
        .matches(s -> 1L == s.getId() && this.MOCK_USER_ID == s.getUserId());
  }

  @Test
  void findByIdToReadOthers() {
    when(this.shoppingListMapper.findById(anyLong()))
        .thenReturn(Optional.of(ShoppingList.builder().id(1L).userId(3L).build()));
    ShoppingList result = this.shoppingListService.findById(1L, this.MOCK_USER_ID);
    verify(this.roleService, times(1)).checkAdministrator(this.MOCK_USER_ID);
  }

  @Test
  void markFinished() {
    when(this.shoppingListMapper.findById(anyLong()))
        .thenReturn(
            Optional.of(
                ShoppingList.builder().id(1L).userId(this.MOCK_USER_ID).finished(false).build()));
    when(this.shoppingListMapper.saveAndFlush(
            argThat(
                record ->
                    record.getFinished()
                        && record.getUserId() == this.MOCK_USER_ID
                        && record.getId() == 1L)))
        .thenReturn(ShoppingList.builder().id(1L).userId(this.MOCK_USER_ID).finished(true).build());
    ShoppingList result = this.shoppingListService.markFinished(1L, this.MOCK_USER_ID);
    assertThat(result)
        .isNotNull()
        .matches(s -> s.getId() == 1L && s.getUserId() == this.MOCK_USER_ID && s.getFinished());
  }

  @Test
  void markFinishedWithNotExistId() {
    when(this.shoppingListMapper.findById(anyLong())).thenReturn(Optional.ofNullable(null));
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class,
            () -> this.shoppingListService.markFinished(1L, this.MOCK_USER_ID));
    assertThat(exception).matches(e -> ResultCode.NOT_FOUND.equals(e.getCode()));
  }

  @Test
  void markFinishedWithOthersRecord() {
    when(this.shoppingListMapper.findById(anyLong()))
        .thenReturn(Optional.of(ShoppingList.builder().id(1L).userId(3L).finished(false).build()));
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class,
            () -> this.shoppingListService.markFinished(1L, this.MOCK_USER_ID));
    assertThat(exception)
        .matches(e -> ResultCode.ACCESS_DENIED.equals(e.getCode()))
        .matches(
            e ->
                ErrorMessageConstants.SHARE_RECORD_OF_OTHER_USER_ERROR_MESSAGE.equals(
                    e.getMessage()));
  }

  @Test
  void findAllItemsByShoppingListId() {
    when(this.shoppingListMapper.findById(anyLong()))
        .thenReturn(Optional.of(ShoppingList.builder().id(1L).userId(this.MOCK_USER_ID).build()));
    this.shoppingListService.findAllItemsByShoppingListId(1L, this.MOCK_USER_ID, 1, 10);
    verify(this.roleService, times(0)).checkAdministrator(anyLong());
    verify(this.shoppingListItemService, times(1)).findAllByShoppingListId(1L, 1, 10);
  }

  @Test
  void findAllItemsByShoppingListIdWithNotExistId() {
    when(this.shoppingListMapper.findById(anyLong())).thenReturn(Optional.ofNullable(null));
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class,
            () ->
                this.shoppingListService.findAllItemsByShoppingListId(
                    1L, this.MOCK_USER_ID, 1, 10));
    assertThat(exception)
        .matches(e -> ResultCode.NOT_FOUND.equals(e.getCode()))
        .matches(
            e ->
                ErrorMessageConstants.SHOPPING_LIST_NOT_FOUND_ERROR_MESSAGE.equals(e.getMessage()));
  }

  @Test
  void findAllItemsByShoppingListIdWithOthersRecords() {
    when(this.shoppingListMapper.findById(anyLong()))
        .thenReturn(Optional.of(ShoppingList.builder().id(1L).userId(3L).build()));
    this.shoppingListService.findAllItemsByShoppingListId(1L, this.MOCK_USER_ID, 1, 10);
    verify(this.roleService, times(1)).checkAdministrator(this.MOCK_USER_ID);
  }
}
