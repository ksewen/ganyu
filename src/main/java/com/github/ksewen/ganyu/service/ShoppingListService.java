package com.github.ksewen.ganyu.service;

import org.springframework.data.domain.Page;

import com.github.ksewen.ganyu.domain.ShoppingList;
import com.github.ksewen.ganyu.model.ShoppingListInsertModel;
import com.github.ksewen.ganyu.model.ShoppingListItemQueryModel;
import com.github.ksewen.ganyu.model.ShoppingListSearchModel;

/**
 * @author ksewen
 * @date 07.06.2023 12:27
 */
public interface ShoppingListService {

    ShoppingList save(ShoppingListInsertModel model);

    ShoppingList findById(long id, long operationUserId);

    ShoppingList markFinished(long id, long operationUserId);

    Page<ShoppingList> findAllByConditions(ShoppingListSearchModel model, int index, int count);

    Page<ShoppingListItemQueryModel> findAllItemsByShoppingListId(long shoppingListId, long operationUserId, int index,
                                                                  int count);

}
