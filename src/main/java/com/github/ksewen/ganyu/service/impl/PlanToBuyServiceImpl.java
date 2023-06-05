package com.github.ksewen.ganyu.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
import com.github.ksewen.ganyu.model.PlanToBuySearchModel;
import com.github.ksewen.ganyu.service.PlanToBuyService;
import com.github.ksewen.ganyu.service.RoleService;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

/**
 * @author ksewen
 * @date 31.05.2023 13:58
 */
@Service
@RequiredArgsConstructor
public class PlanToBuyServiceImpl implements PlanToBuyService {

    private final PlanToBuyMapper planToBuyMapper;

    private final BusinessHelpers businessHelpers;

    private final SpecificationHelpers specificationHelpers;

    private final BeanMapperHelpers beanMapperHelpers;
    private final RoleService roleService;

    @Override
    public PlanToBuy save(PlanToBuyInsertModel model) {
        PlanToBuy insert = PlanToBuy.builder().userId(model.getUserId()).name(model.getName()).brand(model.getBrand())
                .description(model.getDescription()).imageUrl(model.getImageUrl()).build();
        String businessType = Optional.ofNullable(model.getBusinessType())
                .map(list -> this.businessHelpers.listToStringCommaSeparated(list)).orElse(null);
        insert.setBusinessType(businessType);
        return this.planToBuyMapper.saveAndFlush(insert);
    }

    @Override
    public PlanToBuy findById(Long id, long operationUserId) {
        PlanToBuy record = this.planToBuyMapper.findById(id)
                .orElseThrow(() -> new CommonException(ResultCode.NOT_FOUND));
        if (operationUserId != record.getUserId()) {
            this.roleService.checkAdministrator(operationUserId);
        }
        return record;
    }

    @Override
    public Page<PlanToBuy> findAByConditions(PlanToBuySearchModel model, int index, int count) {
        return this.planToBuyMapper.findAll((Specification<PlanToBuy>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.hasLength(model.getBrand())) {
                list.add(criteriaBuilder.like(root.get("brand").as(String.class),
                        this.specificationHelpers.generateFullFuzzyKeyword(model.getBrand())));
            }
            if (model.getUserId() != null) {
                list.add(criteriaBuilder.equal(root.get("userId").as(Long.class), model.getUserId()));
            }
            if (model.getShareFrom() != null) {
                list.add(criteriaBuilder.equal(root.get("shareFrom").as(Long.class), model.getShareFrom()));
            }
            if (model.getAssigned() != null) {
                list.add(criteriaBuilder.equal(root.get("assigned").as(Boolean.class), model.getAssigned()));
            }

            if (StringUtils.hasLength(model.getName())) {
                list.add(criteriaBuilder.equal(root.get("name").as(String.class), model.getName()));
            }
            if (StringUtils.hasLength(model.getBusinessType())) {
                list.add(criteriaBuilder.like(root.get("businessType").as(String.class),
                        this.specificationHelpers.generateFullFuzzyKeyword(model.getBusinessType())));
            }

            this.specificationHelpers.buildTimeRangeCondition(list, model, root, criteriaBuilder);

            Predicate[] array = new Predicate[list.size()];
            Predicate[] predicates = list.toArray(array);
            return criteriaBuilder.and(predicates);
        }, PageRequest.of(index, count, Sort.by("createTime").descending()));
    }

    @Override
    public PlanToBuy modify(PlanToBuyModifyModel model, long operationUserId) {
        PlanToBuy insert = this.planToBuyMapper.findById(model.getId())
                .orElseThrow(() -> new CommonException(ResultCode.NOT_FOUND));
        if (insert.getUserId() != operationUserId) {
            this.roleService.checkAdministrator(operationUserId);
        }
        BeanUtils.copyProperties(model, insert, this.beanMapperHelpers.getNullPropertyNames(model));
        if (!CollectionUtils.isEmpty(model.getBusinessType())) {
            insert.setBusinessType(this.businessHelpers.listToStringCommaSeparated(model.getBusinessType()));
        }
        return this.planToBuyMapper.saveAndFlush(insert);
    }

    @Override
    public void delete(long id, long operationUserId) {
        PlanToBuy record = this.planToBuyMapper.findById(id)
                .orElseThrow(() -> new CommonException(ResultCode.NOT_FOUND));
        if (operationUserId != record.getUserId()) {
            this.roleService.checkAdministrator(operationUserId);
        }
        this.planToBuyMapper.deleteById(id);
    }

    @Override
    public List<PlanToBuy> share(long id, long operationUserId, List<Long> targetUserIds, boolean assigned) {
        PlanToBuy record = this.planToBuyMapper.findById(id)
                .orElseThrow(() -> new CommonException(ResultCode.NOT_FOUND));
        if (operationUserId != record.getUserId()) {
            throw new CommonException(ResultCode.ACCESS_DENIED,
                    ErrorMessageConstants.SHARE_RECORD_OF_OTHER_USER_ERROR_MESSAGE);
        }
        List<PlanToBuy> list = new ArrayList<>();
        for (Long targetUserId : targetUserIds) {
            PlanToBuy item = PlanToBuy.builder().userId(targetUserId).brand(record.getBrand())
                    .shareFrom(record.getUserId()).assigned(assigned).name(record.getName())
                    .description(record.getDescription()).imageUrl(record.getImageUrl())
                    .businessType(record.getBusinessType()).build();
            list.add(item);
        }
        return this.planToBuyMapper.saveAllAndFlush(list);
    }

}
