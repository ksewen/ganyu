package com.github.ksewen.ganyu.service;

import com.github.ksewen.ganyu.domain.ShoppingList;
import com.github.ksewen.ganyu.model.ShoppingListInsertModel;
import com.github.ksewen.ganyu.model.ShoppingListItemQueryModel;
import com.github.ksewen.ganyu.model.ShoppingListSearchModel;
import org.springframework.data.domain.Page;

/**
 * @author ksewen
 * @date 07.06.2023 12:27
 */
public interface ShoppingListService {

  ShoppingList save(ShoppingListInsertModel model);

  ShoppingList findById(long id, long operationUserId);

  ShoppingList markFinished(long id, long operationUserId);

  Page<ShoppingList> findAllByConditions(ShoppingListSearchModel model, int index, int count);

  Page<ShoppingListItemQueryModel> findAllItemsByShoppingListId(
      long shoppingListId, long operationUserId, int index, int count);
}
