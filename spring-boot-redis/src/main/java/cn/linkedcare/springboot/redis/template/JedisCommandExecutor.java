package cn.linkedcare.springboot.redis.template;

import redis.clients.jedis.Jedis;

/**
 * 
 * @author wl
 *
 * @param <T>
 */
public interface JedisCommandExecutor<T> {
    public T execute(Jedis jedis, Object... argArray);
}

