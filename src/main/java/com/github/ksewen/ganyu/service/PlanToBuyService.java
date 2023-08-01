package com.github.ksewen.ganyu.service;

import com.github.ksewen.ganyu.domain.PlanToBuy;
import com.github.ksewen.ganyu.model.PlanToBuyInsertModel;
import com.github.ksewen.ganyu.model.PlanToBuyModifyModel;
import com.github.ksewen.ganyu.model.PlanToBuySearchModel;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * @author ksewen
 * @date 31.05.2023 13:57
 */
public interface PlanToBuyService {

    PlanToBuy save(PlanToBuyInsertModel model);

    PlanToBuy findById(long id, long operationUserId);

    List<PlanToBuy> findByUserIdAndIds(long userId, List<Long> ids);

    Page<PlanToBuy> findAllByConditions(PlanToBuySearchModel model, int index, int count);

    PlanToBuy modify(PlanToBuyModifyModel model, long operationUserId);

    void delete(long id, long operationUserId);

    List<PlanToBuy> share(long id, long operationUserId, List<Long> targetUserIds, boolean assigned);

}
