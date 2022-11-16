package com.web.test.application.dao;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 提供操作redis缓存的工具方法
 */
@Slf4j
@Component
public class RedisBaseDao {

    @Autowired
    JedisPool jedisPool;

    /**
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = null;
        String value = null;

        try {
            jedis = jedisPool.getResource();//获取一个jedis实例
            /*jedis.select(indexdb);*/
            value = jedis.get(key);
        } catch (Exception e) {
            log.error("something error when get from redis ：" + e.getMessage());
        } finally {
            jedis.close();
        }
        return value;
    }


    /**
     * @param key
     * @param expireTime
     * @param value
     * @return
     */
    public String set(String key, int expireTime, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String res = jedis.setex(key, (long) expireTime, value);
            log.info(res);
        } catch (Exception e) {
            log.error("something error when set a temp key in redis ：" + e.getMessage());
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * @param key
     * @return
     */
    public String expire(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.expire(key, (long) 0);

        } catch (Exception e) {
            log.error("something error when expire key in redis ：" + e.getMessage());
        } finally {
            jedis.close();
        }
        return "";
    }


}
