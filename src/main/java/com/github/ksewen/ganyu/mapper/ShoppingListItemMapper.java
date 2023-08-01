package com.github.ksewen.ganyu.mapper;

import com.github.ksewen.ganyu.domain.ShoppingListItem;
import com.github.ksewen.ganyu.model.ShoppingListItemQueryModel;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author ksewen
 * @date 07.06.2023 12:33
 */
@Repository
public interface ShoppingListItemMapper
    extends JpaRepository<ShoppingListItem, Long>, JpaSpecificationExecutor<ShoppingListItem> {

  @Query(
      "select new com.github.ksewen.ganyu.model.ShoppingListItemQueryModel"
          + "(sli.id, sli.shoppingListId, sli.planToBuyId, ptb.name, ptb.brand, ptb.description, ptb.imageUrl, sli.bought, sli.createTime, sli.modifyTime) "
          + "from ShoppingListItem sli left join PlanToBuy ptb on sli.planToBuyId = ptb.id where sli.deleted = false and sli.id = :id")
  Optional<ShoppingListItemQueryModel> findByIdJoinWithPlanToBuy(long id);

  @Query(
      value =
          "select new com.github.ksewen.ganyu.model.ShoppingListItemQueryModel"
              + "(sli.id, sli.shoppingListId, sli.planToBuyId, ptb.name, ptb.brand, ptb.description, ptb.imageUrl, sli.bought, sli.createTime, sli.modifyTime) "
              + "from ShoppingListItem sli left join PlanToBuy ptb on sli.planToBuyId = ptb.id where sli.deleted = false and sli.shoppingListId = :shoppingListId",
      countQuery =
          "select count(shoppingListId) from ShoppingListItem where deleted = false and shoppingListId = :shoppingListId")
  Page<ShoppingListItemQueryModel> findAllByShoppingListIdJoinWithPlanToBuy(
      long shoppingListId, Pageable pageable);
}
