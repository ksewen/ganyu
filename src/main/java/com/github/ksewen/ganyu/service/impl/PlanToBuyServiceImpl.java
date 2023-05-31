package com.github.ksewen.ganyu.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.github.ksewen.ganyu.domain.PlanToBuy;
import com.github.ksewen.ganyu.helper.BusinessHelpers;
import com.github.ksewen.ganyu.mapper.PlanToBuyMapper;
import com.github.ksewen.ganyu.model.PlanToBuyModel;
import com.github.ksewen.ganyu.service.PlanToBuyService;

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

    @Override
    public PlanToBuy save(PlanToBuyModel model) {
        PlanToBuy insert = PlanToBuy.builder().userId(model.getUserId()).name(model.getName()).brand(model.getBrand())
                .description(model.getDescription()).imageUrl(model.getImageUrl()).build();
        String businessType = Optional.ofNullable(model.getBusinessType())
                .map(list -> this.businessHelpers.listToStringCommaSeparated(list)).orElse(null);
        insert.setBusinessType(businessType);
        return this.planToBuyMapper.saveAndFlush(insert);
    }

}
