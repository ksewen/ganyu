package com.github.ksewen.ganyu.helper;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.BeanUtils;

import com.github.ksewen.ganyu.configuration.exception.CommonException;
import com.github.ksewen.ganyu.enums.ResultCode;

import lombok.extern.slf4j.Slf4j;

/**
 * @program: librarian
 * @description:
 * @author: ksewen
 * @create: 2018-03-19 16:16
 **/
@Slf4j
public class BeanMapperHelpers {
    public <T> T createAndCopyProperties(Object source, Class<T> clazz) {
        try {
            T dest = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, dest);
            return dest;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CommonException(ResultCode.COPY_PROPERTIES_ERROR,
                    "the target object of the createAndCopyProperties must provided a default constructor");
        } catch (InvocationTargetException e) {
            throw new CommonException(ResultCode.COPY_PROPERTIES_ERROR);
        } catch (NoSuchMethodException e) {
            throw new CommonException(ResultCode.COPY_PROPERTIES_ERROR, "can not find the method");
        }
    }
}
