package com.orders.config.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Date: Created in 9:33 2019/3/15
 * @Modifired by:
 */
@Component
public class RedisUtil {
    private final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * Clears this cache instance
     */
    public void clear() {
        redisTemplate.execute((RedisCallback) connection -> {
            connection.flushDb();
            return null;
        });
    }

    /**
     * Remove cached query result from redis
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object removeObject(Object key) {
        try {
            redisTemplate.delete(key);
            logger.debug("Remove cached query result from redis");
        }
        catch (Throwable t) {
            logger.error("Redis remove failed", t);
        }
        return null;
    }


    /**
     * Put query result to redis
     *
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
    public void putObject(Object key, Object value, long EXPIRE_TIME, TimeUnit timeUnit) {
        try {
            ValueOperations opsForValue = redisTemplate.opsForValue();
            opsForValue.set(key, value, EXPIRE_TIME, timeUnit);
            logger.debug("Put query result to redis");
        }
        catch (Throwable t) {
            logger.error("Redis put failed", t);
        }
    }

    /**
     * Put geo result to redis
     *
     * @param key
     * @param m
     */
    @SuppressWarnings("unchecked")
    public Long geoAdd(Object key, Point point, Object m) {
        try {
            GeoOperations geoValue = redisTemplate.opsForGeo();
            logger.debug("geoAdd result to redis");
            return geoValue.geoAdd(key,point,m);

        }
        catch (Throwable t) {
            logger.error("geoAdd failed", t);
        }
        return null;
    }

    /**
     * geo dist result to redis
     *
     * @param key
     * @param m1,m2
     */
    @SuppressWarnings("unchecked")
    public Distance geoDist(Object key, Object m1, Object m2) {
        try {
            GeoOperations geoValue = redisTemplate.opsForGeo();
            logger.debug("geoAdd result to redis");
            return  geoValue.geoDist(key,m1,m1);
        }
        catch (Throwable t) {
            logger.error("geoAdd failed", t);
        }
        return null;
    }

    /**
     * nearDist
     * @param key
     * @param circle
     * @param args
     * @return
     */
    public GeoResults<RedisGeoCommands.GeoLocation> nearDist(Object key, Circle circle, RedisGeoCommands.GeoRadiusCommandArgs args) {
        try {
            GeoOperations geoValue = redisTemplate.opsForGeo();
            logger.debug("geoAdd result to redis");
            return  geoValue.geoRadius(key,circle,args);
        }
        catch (Throwable t) {
            logger.error("geoAdd failed", t);
        }
        return null;
    }



    public double zscore(String key, Object member) {
        double value = 0;
        value = redisTemplate.opsForZSet().score(key,member);
        return value;
    }

    /***
     * <p>
     * Description: 得到值
     * </p>
     *
     * @param key
     */
    public Object get(String key) {
        Object value = null;
        value = redisTemplate.opsForValue().get(key);
        return value;
    }

    /***
     * <p>
     * Description: 设置键值
     * </p>
     * @param key
     *            value
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key,value);
    }

    /***
     * <p>
     * Description: 设置键值 并同时设置有效期
     * </p>
     * @param key
     *            seconds秒数 value
     */
    public void setex(String key, String value) {
        redisTemplate.opsForValue().setIfAbsent(key,value);
    }

    /**
     *判断redis中是否已存在key对应的value
     * @param key
     * @return true/false
     *
     */
    public boolean isExistValue(String key){
        if (StringUtils.isBlank(key)){
            return false;
        }

        // 根据key获取redis的value
        Object value = redisTemplate.opsForValue().get(key);

        if (value == null){
            return false;
        }
        return true;
    }

    /**
     *判断redis中是否已存在key
     * @param key
     * @return true/false
     *
     */
    public boolean isExistKey(String key){
        if (StringUtils.isBlank(key)){
            return false;
        }
        return redisTemplate.hasKey(key);
    }
}
