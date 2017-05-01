/**
 * @(#)JsonUtils.java 1.0 2017年4月24日
 * @Copyright:  Copyright 2007 - 2017 
 * @Description: 
 * 
 * Modification History:
 * Date:        2017年4月24日
 * Author:      lucius.lv
 * Version:     1.0.0.0
 * Description: (Initialize)
 * Reviewer:    
 * Review Date: 
 */
package org.lucius.components.data.converter.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class JsonUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private static ObjectMapper objectMapperMessagePack = new ObjectMapper(new MessagePackFactory());
    
    static{
        // 如果为空则不输出
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        // 对于空的对象转json的时候不抛出错误
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 禁用序列化日期为timestamps
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 禁用遇到未知属性抛出异常
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 视空字符传为null
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        // 低层级配置
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        // 允许属性名称没有引号
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许单引号
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 取消对非ASCII字符的转码
        objectMapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);
        
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        
        
        
        
        // 如果为空则不输出
        objectMapperMessagePack.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        // 对于空的对象转json的时候不抛出错误
        objectMapperMessagePack.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 禁用序列化日期为timestamps
        objectMapperMessagePack.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 禁用遇到未知属性抛出异常
        objectMapperMessagePack.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 视空字符传为null
        objectMapperMessagePack.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        // 低层级配置
        objectMapperMessagePack.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        // 允许属性名称没有引号
        objectMapperMessagePack.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许单引号
        objectMapperMessagePack.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 取消对非ASCII字符的转码
        objectMapperMessagePack.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);
        
        objectMapperMessagePack.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        objectMapperMessagePack.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
        objectMapperMessagePack.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        objectMapperMessagePack.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        
        
    }
    
    private JsonUtils(){
        
    }

    public static <T> T fromJsonThrowable(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }
    
    public static <T> T fromJsonThrowable(String json, TypeReference<?> typeReference) throws IOException {
        return objectMapper.readValue(json, typeReference);
    }
    
    public static <T> String toJsonThrowable(T src) throws IOException {
        return objectMapper.writeValueAsString(src);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return fromJsonThrowable(json, clazz);
        } catch (IOException e) {
            logger.error("convert json to object exception : " , e);
        }
        return null;
    }
    
    public static <T> T fromJson(String json, TypeReference<?> typeReference) {
        try {
            return fromJsonThrowable(json, typeReference);
        } catch (IOException e) {
            logger.error("convert json to object exception : " , e);
        }
        return null;
    }
    
    public static <T> String toJson(T src) {
        try {
            return toJsonThrowable(src);
        } catch (IOException e) {
            logger.error("convert object to json exception : " , e);
        }
        return null;
    }
    
    
    public static byte[] messagePackSerializeThrowable(Object obj) throws JsonProcessingException {
        return objectMapperMessagePack.writeValueAsBytes(obj);
    }

    public static <T> T messagePackDeserializeThrowable(byte[] b, Class<T> className)
                    throws JsonParseException, JsonMappingException, IOException {
        return objectMapperMessagePack.readValue(b, className);
    }
    
    public static <T> T messagePackDeserializeThrowable(byte[] b, TypeReference<T> typeReference)
            throws JsonParseException, JsonMappingException, IOException {
        return objectMapperMessagePack.readValue(b, typeReference);
    }
    
    public static byte[] messagePackSerialize(Object obj) {
        try {
            return messagePackSerializeThrowable(obj);
        } catch (IOException e) {
            logger.error("convert object to byte[] exception : " , e);
        }
        return null;
    }

    public static <T> T messagePackDeserialize(byte[] b, Class<T> className) {
        try {
            return messagePackDeserializeThrowable(b, className);
        } catch (IOException e) {
            logger.error("convert byte[] to object exception : " , e);
        }
        return null;
    }
    
    public static <T> T messagePackDeserialize(byte[] b, TypeReference<T> typeReference) {
        try {
            return messagePackDeserializeThrowable(b, typeReference);
        } catch (IOException e) {
            logger.error("convert byte[] to object exception : " , e);
        }
        return null;
    }
    
    public static <T> T fromJsonThrowable(Reader reader, Class<T> clazz) throws IOException {
        return objectMapper.readValue(reader, clazz);
    }
    
    public static <T> T fromJsonThrowable(Reader reader, TypeReference<?> typeReference) throws IOException {
        return objectMapper.readValue(reader, typeReference);
        
    }
    
    public static <T> T fromJson(Reader reader, Class<T> clazz) throws IOException {
        try {
            return fromJsonThrowable(reader, clazz);
        } catch (IOException e) {
            logger.error("convert byte[] to object exception : " , e);
        }
        return null;
    }
    
    public static <T> T fromJson(Reader reader, TypeReference<?> typeReference) throws IOException {
        try {
            return fromJsonThrowable(reader, typeReference);
        } catch (IOException e) {
            logger.error("convert byte[] to object exception : " , e);
        }
        return null;
    }
    
    public static <T> T fromJsonThrowable(InputStream in, Class<T> clazz) throws IOException {
        return objectMapper.readValue(in, clazz);
    }
    
    public static <T> T fromJsonThrowable(InputStream in, TypeReference<?> typeReference) throws IOException {
        return objectMapper.readValue(in, typeReference);
        
    }
    
    public static <T> T fromJson(InputStream in, Class<T> clazz) throws IOException {
        try {
            return fromJsonThrowable(in, clazz);
        } catch (IOException e) {
            logger.error("convert byte[] to object exception : " , e);
        }
        return null;
    }
    
    public static <T> T fromJson(InputStream in, TypeReference<?> typeReference) throws IOException {
        try {
            return fromJsonThrowable(in, typeReference);
        } catch (IOException e) {
            logger.error("convert byte[] to object exception : " , e);
        }
        return null;
    }
    
}



