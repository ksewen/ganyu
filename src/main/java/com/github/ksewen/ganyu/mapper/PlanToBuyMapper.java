package com.github.ksewen.ganyu.mapper;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.github.ksewen.ganyu.domain.PlanToBuy;

/**
 * @author ksewen
 * @date 31.05.2023 13:57
 */
@Repository
public interface PlanToBuyMapper extends JpaRepository<PlanToBuy, Long>, JpaSpecificationExecutor<PlanToBuy> {

    Page<PlanToBuy> findAllByUserId(Long userId, Pageable pageable);

    List<PlanToBuy> findByUserIdAndIdIn(Long userId, List<Long> ids);
}
