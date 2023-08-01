package com.github.ksewen.ganyu.mapper;

import com.github.ksewen.ganyu.domain.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author ksewen
 * @date 07.06.2023 12:33
 */
@Repository
public interface ShoppingListMapper
    extends JpaRepository<ShoppingList, Long>, JpaSpecificationExecutor<ShoppingList> {}
