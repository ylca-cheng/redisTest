package com.cheng.utils;

import com.cheng.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.locks.ReentrantLock;

/**
 * redis 工具类
 * Created by niecheng on 2018/3/28.
 */
public class RedisUtils {
    protected static ReentrantLock lockPool = new ReentrantLock();
    protected static ReentrantLock lockJedis = new ReentrantLock();

    private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);
    // 服务器ip
    private static String redisHost = "127.0.0.1";
    // 端口号
    private static int redisPort = 6379;
    // 访问密码
    private static String redisAuth = "123456";
    // 可用连接实例的最大数目，默认值为8；
    // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int redisMaxActive = 8;
    // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int redisMaxIdle = 8;
    // 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int redisMaxWait = 3000;
    // 超时时间
    private static int redisMaxTimeOut = 10000;
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean redisTestOnBrrow = true;

    // jedis连接池
    private static JedisPool jedisPool = null;

    //redis过期时间,以秒为单位
    public final static int EXRP_HOUR = 60 * 60;            // 一小时
    public final static int EXRP_DAY = 60 * 60 * 24;        // 一天
    public final static int EXRP_MONTH = 60 * 60 * 24 * 30; // 一个月

    /**
     * 初始化redis连接池
     */
    private static void initialPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisMaxActive);       // 设置最大连接实例数
        config.setMaxIdle(redisMaxIdle);          // 设置最大空闲的实例数
        config.setMaxWaitMillis(redisMaxWait);    // 设置最大等待连接时间，单位毫秒
        config.setTestOnBorrow(redisTestOnBrrow); // 设置borrow一个jedis实例时，是否提前进行validate操作
        // 需要设置redis密码 在redis客户端修改：config set requirepass "123456"
        //jedisPool =  new JedisPool(config, redisHost, redisPort, redisMaxTimeOut, redisAuth);
        jedisPool =  new JedisPool(config, redisHost, redisPort, redisMaxTimeOut);
    }

    /**
     * 多线程环境同步初始化
     */
    private static void poolInit(){
        lockPool.lock(); // 上锁
        try {
            if (jedisPool == null){
                initialPool();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lockPool.unlock();//解锁
        }
    }

    /**
     * 获取redis实例
     */
    private static Jedis getJedis(){
        lockJedis.lock(); // 上锁
        if (jedisPool == null){
            initialPool();
        }
        Jedis jedis = null;
        try {
            if(jedisPool != null){
                jedis = jedisPool.getResource();
            }
        }catch (Exception e){
            logger.error("获取redis实例失败！");
        }finally {
            // 释放资源后，该实例还是可用的，也可被其他线程拿到，否则只能在当前线程中有作用，因为其他线程拿不到该实例
            // 如果当前线程再次从连接池获取redis实例，可能因为连接池已经没有可用的实例而获取不到新的实例
            returnResource(jedis);
            lockJedis.unlock();
        }
        return jedis;
    }

    /**
     * 释放资源
     */
    public static void returnResource(final Jedis jedis){
        if(jedis != null){
            jedis.close();
        }
    }

    /**
     * 设置值
     */
    public synchronized static <T> void setUser(String key, T value){
        try {
            getJedis().set(key.getBytes(), SerializeUtil.serializeObject(value));
        }catch (Exception e){
            logger.error("放值失败！");
        }
    }

    /**
     * 设置 过期时间
     *
     * @param key
     * @param seconds 以秒为单位
     * @param value
     */
    public synchronized static <T> void setUser(String key, T value, int seconds) {
        try {
            getJedis().setex(key.getBytes(), seconds, SerializeUtil.serializeObject(value));
        } catch (Exception e) {
            logger.error("Set keyex error : " + e);
        }
    }

    /**
     * 获取用户对象
     */
    public synchronized static <T> T getUser(String key){
        Jedis jedis = getJedis();
        if(jedis == null){
            return null;
        }else if(!jedis.exists(key.getBytes())){
            return null;
        } else{
            return (T)SerializeUtil.deserializeObject(jedis.get(key.getBytes()));
        }
    }

    /**
     * 删除缓存
     */
    public synchronized  static void delUser(String key){
        try {
            getJedis().del(key.getBytes());
        } catch (Exception e) {
            logger.error("Del key error : " + e);
        }
    }
}
