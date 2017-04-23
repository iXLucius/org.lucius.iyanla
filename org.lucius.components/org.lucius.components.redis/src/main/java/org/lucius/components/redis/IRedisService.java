/**
 * @(#)IRedisService.java 1.0 2017年4月23日
 * @Copyright:  Copyright 2007 - 2017 
 * @Description: 
 * 
 * Modification History:
 * Date:        2017年4月23日
 * Author:      lucius.lv
 * Version:     1.0.0.0
 * Description: (Initialize)
 * Reviewer:    
 * Review Date: 
 */
package org.lucius.components.redis;

public interface IRedisService {
    
    <T> void set(final String key,final T obj,final int seconds);
    
    <T> T get(final String key,Class<T> clazz);

    Long ttl(String key);

    Boolean persist(String key);

    Boolean expire(String key, int seconds);

    long del(String[] keys);

    Boolean exists(String key);

    void setString(String key, String value);

    void setString(String key, String value, int seconds);

    <T> void set(String key, T obj);

}

