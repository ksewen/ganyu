package com.github.ksewen.ganyu.configuration.exception;

import com.github.ksewen.ganyu.enums.ResultCode;

/**
 * @author ksewen
 * @date 13.05.2023 10:57
 */
public class SerializationOrDeserializationException extends CommonException {

    public SerializationOrDeserializationException() {
        super(ResultCode.SERIALIZATION_OR_DESERIALIZATION_ERROR);
    }

    public SerializationOrDeserializationException(String message) {
        super(ResultCode.SERIALIZATION_OR_DESERIALIZATION_ERROR, message);
    }

}
