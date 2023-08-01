package com.github.ksewen.ganyu.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.constant.ErrorMessageConstants;
import com.github.ksewen.ganyu.domain.PlanToBuy;
import com.github.ksewen.ganyu.enums.ResultCode;
import com.github.ksewen.ganyu.helper.BeanMapperHelpers;
import com.github.ksewen.ganyu.helper.BusinessHelpers;
import com.github.ksewen.ganyu.helper.SpecificationHelpers;
import com.github.ksewen.ganyu.mapper.PlanToBuyMapper;
import com.github.ksewen.ganyu.model.PlanToBuyInsertModel;
import com.github.ksewen.ganyu.model.PlanToBuyModifyModel;
import com.github.ksewen.ganyu.service.PlanToBuyService;
import com.github.ksewen.ganyu.service.RoleService;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.CollectionUtils;

/**
 * @author ksewen
 * @date 04.06.2023 13:12
 */
@SpringBootTest(classes = PlanToBuyServiceImpl.class)
class PlanToBuyServiceImplTest {

  @Autowired private PlanToBuyService planToBuyService;

  @MockBean private BusinessHelpers businessHelpers;

  @MockBean private PlanToBuyMapper planToBuyMapper;

  @MockBean private SpecificationHelpers specificationHelpers;

  @MockBean private BeanMapperHelpers beanMapperHelpers;

  @MockBean private RoleService roleService;

  private final String name = "name";
  private final String brand = "brand";
  private final String description = "description";
  private final String businessType = "Markt,Basar";
  final String modifiedBusinessType = "Supermarkt,Drogerie";

  @Test
  void saveSuccess() {
    when(this.businessHelpers.listToStringCommaSeparated(anyList())).thenReturn(this.businessType);
    PlanToBuyInsertModel model =
        PlanToBuyInsertModel.builder()
            .name(this.name)
            .brand(this.brand)
            .userId(1L)
            .description("description")
            .businessType(new ArrayList<>())
            .build();
    this.planToBuyService.save(model);
    verify(this.planToBuyMapper, times(1))
        .saveAndFlush(
            argThat(
                p ->
                    this.name.equals(p.getName())
                        && this.brand.equals(p.getBrand())
                        && Objects.equals(1L, p.getUserId())
                        && this.description.equals(p.getDescription())
                        && this.businessType.equals(p.getBusinessType())));
  }

  @Test
  void modifyOwnSuccess() {
    when(this.planToBuyMapper.findById(anyLong()))
        .thenReturn(
            Optional.of(
                PlanToBuy.builder()
                    .id(5L)
                    .name(this.name)
                    .brand(this.brand)
                    .userId(1L)
                    .description(this.description)
                    .businessType(this.businessType)
                    .build()));
    when(this.beanMapperHelpers.getNullPropertyNames(any()))
        .thenReturn(new String[] {"brand", "name", "description"});
    when(this.businessHelpers.listToStringCommaSeparated(anyList()))
        .thenReturn(modifiedBusinessType);
    this.planToBuyService.modify(
        PlanToBuyModifyModel.builder()
            .id(5L)
            .businessType(Arrays.asList("Supermarkt", "Drogerie"))
            .build(),
        1L);
    verify(this.roleService, times(0)).checkAdministrator(anyLong());
    verify(this.planToBuyMapper, times(1))
        .saveAndFlush(
            argThat(
                p ->
                    Objects.equals(5L, p.getId())
                        && this.name.equals(p.getName())
                        && this.brand.equals(p.getBrand())
                        && Objects.equals(1L, p.getUserId())
                        && this.description.equals(p.getDescription())
                        && this.modifiedBusinessType.equals(p.getBusinessType())));
  }

  @Test
  void modifyAnotherSuccess() {
    when(this.planToBuyMapper.findById(anyLong()))
        .thenReturn(
            Optional.of(
                PlanToBuy.builder()
                    .id(5L)
                    .name(this.name)
                    .brand(this.brand)
                    .userId(2L)
                    .description(this.description)
                    .businessType(this.businessType)
                    .build()));
    when(this.beanMapperHelpers.getNullPropertyNames(any()))
        .thenReturn(new String[] {"brand", "name", "description"});
    when(this.businessHelpers.listToStringCommaSeparated(anyList()))
        .thenReturn(modifiedBusinessType);
    this.planToBuyService.modify(
        PlanToBuyModifyModel.builder()
            .id(5L)
            .businessType(Arrays.asList("Supermarkt", "Drogerie"))
            .build(),
        1L);
    verify(this.roleService, times(1)).checkAdministrator(1L);
    verify(this.planToBuyMapper, times(1))
        .saveAndFlush(
            argThat(
                p ->
                    Objects.equals(5L, p.getId())
                        && this.name.equals(p.getName())
                        && this.brand.equals(p.getBrand())
                        && Objects.equals(2L, p.getUserId())
                        && this.description.equals(p.getDescription())
                        && modifiedBusinessType.equals(p.getBusinessType())));
  }

  @Test
  void modifyNotExistRecord() {
    when(this.planToBuyMapper.findById(anyLong())).thenReturn(Optional.ofNullable(null));
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class,
            () -> this.planToBuyService.modify(PlanToBuyModifyModel.builder().build(), 1L));
    assertThat(exception)
        .matches(e -> ResultCode.NOT_FOUND.equals(e.getCode()))
        .matches(e -> ResultCode.NOT_FOUND.getMessage().equals(e.getMessage()));
  }

  @Test
  void deleteOwnSuccess() {
    when(this.planToBuyMapper.findById(anyLong()))
        .thenReturn(Optional.of(PlanToBuy.builder().id(1L).userId(5L).build()));
    this.planToBuyService.delete(1L, 5L);
    verify(this.roleService, times(0)).checkAdministrator(anyLong());
    verify(this.planToBuyMapper, times(1)).deleteById(1L);
  }

  @Test
  void deleteAnotherSuccess() {
    when(this.planToBuyMapper.findById(anyLong()))
        .thenReturn(Optional.of(PlanToBuy.builder().id(1L).userId(5L).build()));
    this.planToBuyService.delete(1L, 1L);
    verify(this.roleService, times(1)).checkAdministrator(1L);
    verify(this.planToBuyMapper, times(1)).deleteById(1L);
  }

  @Test
  void deleteNotExistRecord() {
    when(this.planToBuyMapper.findById(anyLong())).thenReturn(Optional.ofNullable(null));
    CommonException exception =
        Assertions.assertThrows(CommonException.class, () -> this.planToBuyService.delete(1L, 1L));
    assertThat(exception)
        .matches(e -> ResultCode.NOT_FOUND.equals(e.getCode()))
        .matches(e -> ResultCode.NOT_FOUND.getMessage().equals(e.getMessage()));
    verify(this.roleService, times(0)).checkAdministrator(anyLong());
    verify(this.planToBuyMapper, times(0)).deleteById(anyLong());
  }

  @Test
  void shareSuccess() {
    when(this.planToBuyMapper.findById(anyLong()))
        .thenReturn(
            Optional.of(
                PlanToBuy.builder()
                    .id(1L)
                    .name(this.name)
                    .description(this.description)
                    .userId(5L)
                    .build()));
    this.planToBuyService.share(1L, 5L, Arrays.asList(1L, 2L, 3L), false);
    verify(this.planToBuyMapper, times(1))
        .saveAllAndFlush(
            argThat(
                l -> {
                  List<PlanToBuy> list = (List<PlanToBuy>) l;
                  return !CollectionUtils.isEmpty(list)
                      && list.size() == 3
                      && this.name.equals(list.get(0).getName())
                      && this.name.equals(list.get(1).getName())
                      && this.name.equals(list.get(2).getName())
                      && this.description.equals(list.get(0).getDescription())
                      && this.description.equals(list.get(1).getDescription())
                      && this.description.equals(list.get(2).getDescription())
                      && Objects.equals(1L, list.get(0).getUserId())
                      && Objects.equals(2L, list.get(1).getUserId())
                      && Objects.equals(3L, list.get(2).getUserId());
                }));
  }

  @Test
  void shareTheRecordOfAnotherUser() {
    when(this.planToBuyMapper.findById(anyLong()))
        .thenReturn(
            Optional.of(
                PlanToBuy.builder()
                    .id(1L)
                    .name(this.name)
                    .description(this.description)
                    .userId(5L)
                    .build()));
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class,
            () -> this.planToBuyService.share(1L, 1L, Arrays.asList(1L, 2L, 3L), false));
    assertThat(exception)
        .matches(e -> ResultCode.ACCESS_DENIED.equals(e.getCode()))
        .matches(
            e ->
                ErrorMessageConstants.SHARE_RECORD_OF_OTHER_USER_ERROR_MESSAGE.equals(
                    e.getMessage()));
  }

  @Test
  void shareNotExistRecord() {
    when(this.planToBuyMapper.findById(anyLong())).thenReturn(Optional.ofNullable(null));
    CommonException exception =
        Assertions.assertThrows(
            CommonException.class,
            () -> this.planToBuyService.share(1L, 1L, Arrays.asList(1L, 2L, 3L), false));
    assertThat(exception)
        .matches(e -> ResultCode.NOT_FOUND.equals(e.getCode()))
        .matches(e -> ResultCode.NOT_FOUND.getMessage().equals(e.getMessage()));
    verify(this.planToBuyMapper, times(0)).saveAllAndFlush(any());
  }
}
