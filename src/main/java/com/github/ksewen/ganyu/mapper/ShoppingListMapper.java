package com.github.ksewen.ganyu.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.github.ksewen.ganyu.domain.ShoppingList;

/**
 * @author ksewen
 * @date 07.06.2023 12:33
 */
@Repository
public interface ShoppingListMapper extends JpaRepository<ShoppingList, Long>, JpaSpecificationExecutor<ShoppingList> {
}
