package com.github.ksewen.ganyu.helper;

import java.util.List;

import com.github.ksewen.ganyu.model.base.DataSearchModel;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * @author ksewen
 * @date 01.06.2023 22:29
 */
public class SpecificationHelpers {

    public String generateLeftFuzzyKeyword(String str) {
        StringBuilder builder = new StringBuilder("%").append(str);
        return builder.toString();
    }

    public String generateRightFuzzyKeyword(String str) {
        StringBuilder builder = new StringBuilder(str).append("%");
        return builder.toString();
    }

    public String generateFullFuzzyKeyword(String str) {
        StringBuilder builder = new StringBuilder("%").append(str).append("%");
        return builder.toString();
    }

    public void buildTimeRangeCondition(List<Predicate> list, DataSearchModel model, Root root,
                                        CriteriaBuilder criteriaBuilder) {
        this.buildCreateTimeRangeCondition(list, model, root, criteriaBuilder);
        this.buildModifyTimeRangeCondition(list, model, root, criteriaBuilder);
    }

    public void buildCreateTimeRangeCondition(List<Predicate> list, DataSearchModel model, Root root,
                                              CriteriaBuilder criteriaBuilder) {
        if (model.getCreateTimeAfter() != null && model.getCreateTimeBefore() != null
                && model.getCreateTimeAfter().isBefore(model.getCreateTimeBefore())) {
            list.add(criteriaBuilder.between(root.get("createTime"), model.getCreateTimeAfter(),
                    model.getCreateTimeBefore()));
        }
    }

    public void buildModifyTimeRangeCondition(List<Predicate> list, DataSearchModel model, Root root,
                                              CriteriaBuilder criteriaBuilder) {
        if (model.getModifyTimeAfter() != null && model.getModifyTimeBefore() != null
                && model.getModifyTimeAfter().isBefore(model.getModifyTimeBefore())) {
            list.add(criteriaBuilder.between(root.get("modifyTime"), model.getModifyTimeAfter(),
                    model.getModifyTimeBefore()));
        }
    }
}
