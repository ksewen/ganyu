package com.github.ksewen.ganyu.service;

import com.github.ksewen.ganyu.domain.ShoppingListItem;
import com.github.ksewen.ganyu.model.ShoppingListItemQueryModel;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * @author ksewen
 * @date 07.06.2023 16:08
 */
public interface ShoppingListItemService {

    List<ShoppingListItem> add(List<Long> ids, long userId, long shoppingListId);

    void delete(List<Long> ids, long operationUserId);

    ShoppingListItemQueryModel findById(long id, long operationUserId);

    Page<ShoppingListItemQueryModel> findAllByShoppingListId(long shoppingListId, int index, int count);

    ShoppingListItemQueryModel markBought(long id, long operationUserId);



}
