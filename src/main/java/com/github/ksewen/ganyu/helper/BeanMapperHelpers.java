package com.github.ksewen.ganyu.helper;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.enums.ResultCode;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;

/**
 * @program: librarian
 * @description:
 * @author: ksewen
 * @create: 2018-03-19 16:16
 */
@Slf4j
public class BeanMapperHelpers {

  private final String EMPTY_SOURCE_MESSAGE = "given source object is empty";

  public <T> T createAndCopyProperties(Object source, Class<T> clazz) {
    Assert.notNull(source, this.EMPTY_SOURCE_MESSAGE);
    try {
      T dest = clazz.getDeclaredConstructor().newInstance();
      BeanUtils.copyProperties(source, dest);
      return dest;
    } catch (InstantiationException | IllegalAccessException e) {
      throw new CommonException(
          ResultCode.COPY_PROPERTIES_ERROR,
          "the target object of the createAndCopyProperties must provided a default constructor");
    } catch (InvocationTargetException e) {
      throw new CommonException(ResultCode.COPY_PROPERTIES_ERROR);
    } catch (NoSuchMethodException e) {
      throw new CommonException(ResultCode.COPY_PROPERTIES_ERROR, "can not find the method");
    }
  }

  public <T> T createAndCopyNotNullProperties(Object source, Class<T> clazz) {
    Assert.notNull(source, this.EMPTY_SOURCE_MESSAGE);
    try {
      T dest = clazz.getDeclaredConstructor().newInstance();
      BeanUtils.copyProperties(source, dest, getNullPropertyNames(source));
      return dest;
    } catch (InstantiationException | IllegalAccessException e) {
      throw new CommonException(
          ResultCode.COPY_PROPERTIES_ERROR,
          "the target object of the createAndCopyProperties must provided a default constructor");
    } catch (InvocationTargetException e) {
      throw new CommonException(ResultCode.COPY_PROPERTIES_ERROR);
    } catch (NoSuchMethodException e) {
      throw new CommonException(ResultCode.COPY_PROPERTIES_ERROR, "can not find the method");
    }
  }

  public String[] getNullPropertyNames(Object source) {
    Assert.notNull(source, this.EMPTY_SOURCE_MESSAGE);
    final BeanWrapper src = new BeanWrapperImpl(source);
    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
    Set emptyNames = new HashSet();
    for (java.beans.PropertyDescriptor pd : pds) {
      // check if value of this property is null then add it to the collection
      Object srcValue = src.getPropertyValue(pd.getName());
      if (srcValue == null) {
        emptyNames.add(pd.getName());
      }
    }
    String[] result = new String[emptyNames.size()];
    return (String[]) emptyNames.toArray(result);
  }
}
