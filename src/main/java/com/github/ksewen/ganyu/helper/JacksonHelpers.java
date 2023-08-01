package com.github.ksewen.ganyu.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ksewen.ganyu.configuration.exception.SerializationOrDeserializationException;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ksewen
 * @date 13.05.2023 10:55
 */
@AllArgsConstructor
@Slf4j
public class JacksonHelpers {

  private final ObjectMapper DEFAULT_OBJECT_MAPPER;

  public <T> T toObject(String string, Class<T> cls) {
    try {
      return this.DEFAULT_OBJECT_MAPPER.readValue(string, cls);
    } catch (JsonProcessingException e) {
      log.error("deserialization to object failed, text: {}", string);
      throw new SerializationOrDeserializationException();
    }
  }

  public <T> T toObject(String string, TypeReference<T> valueTypeRef) {
    try {
      return this.DEFAULT_OBJECT_MAPPER.readValue(string, valueTypeRef);
    } catch (JsonProcessingException e) {
      log.error("deserialization to object failed, text: {}", string);
      throw new SerializationOrDeserializationException();
    }
  }

  public Map toMap(String string) {
    try {
      return this.DEFAULT_OBJECT_MAPPER.readValue(string, Map.class);
    } catch (JsonProcessingException e) {
      log.error("deserialization to object failed, text: {}", string);
      throw new SerializationOrDeserializationException();
    }
  }

  public String toJsonString(Object object) {
    try {
      return this.DEFAULT_OBJECT_MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("serialization to json string failed, object type: {}", object.getClass());
      throw new SerializationOrDeserializationException();
    }
  }

  public <T> List<T> toObjectList(String string, TypeReference<List<T>> typeReference) {
    try {
      return this.DEFAULT_OBJECT_MAPPER.readValue(string, typeReference);
    } catch (Exception e) {
      log.error("deserialization to object list failed, text: {}", string);
      throw new SerializationOrDeserializationException();
    }
  }

  public JsonNode toJsonNode(String string) {
    try {
      return this.DEFAULT_OBJECT_MAPPER.readTree(string);
    } catch (JsonProcessingException e) {
      log.error("deserialization to json node failed, text: {}", string);
      throw new SerializationOrDeserializationException();
    }
  }

  public JsonNode toJsonNode(Object object) {
    try {
      return this.DEFAULT_OBJECT_MAPPER.valueToTree(object);
    } catch (IllegalArgumentException e) {
      log.error("parse to json node failed, object type: {}", object.getClass());
      throw new SerializationOrDeserializationException();
    }
  }

  public <T> T toObject(String string, Class<T> cls, ObjectMapper objectMapper) {
    try {
      return objectMapper.readValue(string, cls);
    } catch (JsonProcessingException e) {
      log.error("deserialization to object failed, text: {}", string);
      throw new SerializationOrDeserializationException();
    }
  }

  public <T> T toObject(String string, TypeReference<T> valueTypeRef, ObjectMapper objectMapper) {
    try {
      return objectMapper.readValue(string, valueTypeRef);
    } catch (JsonProcessingException e) {
      log.error("deserialization to object failed, text: {}", string);
      throw new SerializationOrDeserializationException();
    }
  }

  public Map toMap(String string, ObjectMapper objectMapper) {
    try {
      return objectMapper.readValue(string, Map.class);
    } catch (JsonProcessingException e) {
      log.error("deserialization to object failed, text: {}", string);
      throw new SerializationOrDeserializationException();
    }
  }

  public String toJsonString(Object object, ObjectMapper objectMapper) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("serialization to json string failed, object type: {}", object.getClass());
      throw new SerializationOrDeserializationException();
    }
  }

  public <T> List<T> toObjectList(
      String string, TypeReference<List<T>> typeReference, ObjectMapper objectMapper) {
    try {
      return objectMapper.readValue(string, typeReference);
    } catch (Exception e) {
      log.error("deserialization to object list failed, text: {}", string);
      throw new SerializationOrDeserializationException();
    }
  }

  public JsonNode toJsonNode(String string, ObjectMapper objectMapper) {
    try {
      return objectMapper.readTree(string);
    } catch (JsonProcessingException e) {
      log.error("deserialization to json node failed, text: {}", string);
      throw new SerializationOrDeserializationException();
    }
  }

  public JsonNode toJsonNode(Object object, ObjectMapper objectMapper) {
    try {
      return objectMapper.valueToTree(object);
    } catch (IllegalArgumentException e) {
      log.error("parse to json node failed, object type: {}", object.getClass());
      throw new SerializationOrDeserializationException();
    }
  }
}
