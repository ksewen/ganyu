package com.github.ksewen.ganyu.service;

import com.github.ksewen.ganyu.domain.PlanToBuy;
import com.github.ksewen.ganyu.model.PlanToBuyModel;

/**
 * @author ksewen
 * @date 31.05.2023 13:57
 */
public interface PlanToBuyService {

    PlanToBuy save(PlanToBuyModel model);

}
