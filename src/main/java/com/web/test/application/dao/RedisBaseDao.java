package com.web.test.application.dao;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@Component
public class RedisBaseDao {

    @Autowired
    JedisPool jedisPool;

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
