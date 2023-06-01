package com.github.ksewen.ganyu.service;

import org.springframework.data.domain.Page;

import com.github.ksewen.ganyu.domain.PlanToBuy;
import com.github.ksewen.ganyu.model.PlanToBuyInsertModel;
import com.github.ksewen.ganyu.model.PlanToBuySearchModel;

/**
 * @author ksewen
 * @date 31.05.2023 13:57
 */
public interface PlanToBuyService {

    PlanToBuy save(PlanToBuyInsertModel model);

    Page<PlanToBuy> findAByConditions(PlanToBuySearchModel model, int index, int count);

}
