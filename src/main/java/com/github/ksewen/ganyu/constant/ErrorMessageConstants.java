package com.github.ksewen.ganyu.constant;

/**
 * @author ksewen
 * @date 28.05.2023 17:10
 */
public class ErrorMessageConstants {

  public static final String USER_NOT_FOUND_ERROR_MESSAGE = "can not find a exist user by given id";

  public static final String NOT_ADMINISTRATOR_ERROR_MESSAGE =
      "only administrator can read or edit the data of other users";

  public static final String SHARE_RECORD_OF_OTHER_USER_ERROR_MESSAGE =
      "only can operation own record";

  public static final String SHOPPING_LIST_NOT_FOUND_ERROR_MESSAGE =
      "can not find a shopping list by given id";

  public static final String SHOPPING_LIST_ITEM_NOT_FOUND_ERROR_MESSAGE =
      "can not find any shopping list items by given ids";

  public static final String SHOPPING_LIST_AND_ITEM_NOT_MATCH_ERROR_MESSAGE =
      "found any items not match the given shopping list";

  public static final String PLAN_TO_BUY_NOT_FOUND_ERROR_MESSAGE =
      "can not found any records by given ids and user id";

  public static final String CAPTCHA_TYPE_NOT_FOUND_ERROR_MESSAGE = "can not find the captcha type";
}
