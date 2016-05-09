package net.fengni.uitl;

import redis.clients.jedis.Jedis;

/**
 * Created by Administrator on 2015/8/31.
 */
public class RedisManager {
    private static Jedis jedis = new Jedis("192.168.1.111",6379);

    static {
    }
    private RedisManager(){}
    public static Jedis getJedisInstance(){
        return jedis;
    }
}
