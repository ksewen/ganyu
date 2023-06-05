package com.github.ksewen.ganyu.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.github.ksewen.ganyu.domain.PlanToBuy;
import com.github.ksewen.ganyu.model.PlanToBuyInsertModel;
import com.github.ksewen.ganyu.model.PlanToBuyModifyModel;
import com.github.ksewen.ganyu.model.PlanToBuySearchModel;

/**
 * @author ksewen
 * @date 31.05.2023 13:57
 */
public interface PlanToBuyService {

    PlanToBuy save(PlanToBuyInsertModel model);

    PlanToBuy findById(Long id, long operationUserId);

    Page<PlanToBuy> findAByConditions(PlanToBuySearchModel model, int index, int count);

    PlanToBuy modify(PlanToBuyModifyModel model, long operationUserId);

    void delete(long id, long operationUserId);

    PlanToBuy markBought(long id, long operationUserId);

    List<PlanToBuy> share(long id, long operationUserId, List<Long> targetUserIds, boolean assigned);

}
