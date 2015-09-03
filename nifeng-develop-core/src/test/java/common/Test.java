package common;

import redis.clients.jedis.Jedis;

public class Test{
    @org.junit.Test
    public void test(){
        Jedis jedis = com.nifeng.util.RedisManager.getJedisInstance();
        jedis.set("nifeng","0");
        jedis.incr("nifeng");
        System.out.println(jedis.get("nifeng"));
    }
}