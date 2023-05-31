package com.github.ksewen.ganyu.service;

import org.springframework.data.domain.Page;

import com.github.ksewen.ganyu.domain.PlanToBuy;
import com.github.ksewen.ganyu.model.PlanToBuyModel;

/**
 * @author ksewen
 * @date 31.05.2023 13:57
 */
public interface PlanToBuyService {

    PlanToBuy save(PlanToBuyModel model);

    Page<PlanToBuy> findAll(int index, int count);

    Page<PlanToBuy> findAllByUserId(long userId, int index, int count);

}
