package com.example.demo.util;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis集群缓存类
 *
 * @Author HuangChuXin
 */
@Component
public class RedisClusterUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    private Logger logger = Logger.getLogger(RedisClusterUtil.class);
    private final int REDIS_WAY = 2;

    /*//可以用工厂类获取连接
    @Autowired
    RedisConnectionFactory connectionFactory;*/

    /**
     * 批量删除对应的value
     *
     * @param keys 缓存的键
     */
    public void remove(final String... keys) {
        try {
            for (String key : keys) {
                remove(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern 缓存的键
     */
    public void removePattern(final String pattern) {
        try {
            Set<Serializable> keys = redisTemplate.keys(pattern);
            if (keys.size() > 0) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 删除对应的value
     *
     * @param key 缓存的键
     */
    public void remove(final String key) {
        try {
            if (exists(key)) {
                redisTemplate.delete(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key 缓存的键
     * @return
     */
    public boolean exists(final String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 判断键值是否存在
     *
     * @param key 缓存的键
     * @return 如果是1，表示有，0表示没有，-1表示异常
     */
    public int existsEx(final String key) {
        try {
            return redisTemplate.hasKey(key) ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return -1;
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 读取缓存对象
     *
     * @param key 缓存的键
     * @return
     */
    public Object getObject(final String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 缓存对象
     *
     * @param key 缓存的键
     * @param obj 缓存对象
     * @return
     */
    public boolean setObject(final String key, Object obj) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, obj);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * 缓存对象
     *
     * @param key        缓存的键
     * @param obj        缓存对象
     * @param expireTime 缓存时间
     * @return
     */
    public boolean setObject(final String key, Object obj, Long expireTime) {
        boolean result = false;
        try {
            redisTemplate.opsForValue().set(key, obj);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * 缓存map
     *
     * @param key 缓存的键
     * @param map 缓存的map数据
     * @return
     */
    public boolean setHashMapAll(final String key, Map<String, Object> map) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().putAll(key, map);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * 根据key和hashKey保存map对象
     *
     * @param key     缓存的键
     * @param hashKey map的键
     * @param obj     缓存到map的对象
     * @return
     */
    public boolean setHashMap(final String key, final String hashKey, Object obj) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().put(key, hashKey, obj);
            //redisTemplate.expire()
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * 将储存数据转为json格式再保存
     *
     * @param key     缓存的键
     * @param hashKey map的键
     * @param obj     缓存到map的对象
     * @return
     */
    public boolean setHashMapToString(final String key, final String hashKey, Object obj) {
        boolean result = false;
        try {
            String json = JSONObject.fromObject(obj).toString();
            redisTemplate.opsForHash().put(key, hashKey, json);
            //redisTemplate.expire()
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * 获取map集合
     *
     * @param key 缓存的键
     * @return
     */
    public <T> Map<String, T> getHashMapAll(final String key) {
        Map<String, T> result = null;
        try {
            result = redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * 根据key和hashKey，获取对象值
     *
     * @param key     缓存的键
     * @param hashKey map键
     * @return
     */
    public Object getHashMapValue(final String key, final String hashKey) {
        Object result = null;
        try {
            result = redisTemplate.opsForHash().get(key, hashKey);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * 根据key和hashKey，获取对象值
     *
     * @param key     缓存的键
     * @param hashKey map键
     * @param c       实体对象class
     * @return
     */
    public Object getHashMapValueToBean(final String key, final String hashKey, Class c) {
        Object result = null;
        try {
            result = redisTemplate.opsForHash().get(key, hashKey);
            result = JSONObject.toBean(JSONObject.fromObject(result), c);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    public <T> T getHashMapValueNew(final String key, final String hashKey) {
        T result = null;
        try {
            result = (T) redisTemplate.opsForHash().get(key, hashKey);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * 删除缓存map中value
     *
     * @param key     缓存的键
     * @param hashKey map的键
     * @return
     */
    public boolean removeHashMapByHashKey(final String key, String hashKey) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().delete(key, hashKey);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * 批量删除缓存map中value
     *
     * @param key      缓存的键
     * @param hashKeys map的键(String数组)
     * @return
     */
    public boolean removeHashMapByHashKeys(final String key, Object[] hashKeys) {

        try {
            redisTemplate.opsForHash().delete(key, hashKeys);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return false;
    }

    /**
     * 缓存List数据
     *
     * @param key  缓存的键
     * @param list 待缓存的List数据
     * @return 成功返回true, 失败返回false
     */
    public boolean setList(final String key, List<? extends Serializable> list) {
        boolean result = false;
        try {
            // redisTemplate.opsForList().leftPush(key, list);
            redisTemplate.opsForList().leftPushAll(key, list);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * 缓存Object对象到List
     *
     * @param key 缓存的键
     * @param obj 缓存到List的对象
     * @param way 保存方式:1--从前面插入，2--从后面插入
     * @return
     */
    public boolean setListByObject(final String key, Object obj, int way) {
        boolean result = false;
        try {
            ListOperations listOperation = redisTemplate.opsForList();
            if (way == REDIS_WAY) {
                listOperation.rightPush(key, obj);
            } else {
                listOperation.leftPush(key, obj);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * 根据缓存的键(key)，获取缓存list(以List方式保存)
     *
     * @param key 缓存的键
     * @return
     */
    public <T> List<T> getList(final String key) {
        List<T> list = new ArrayList<T>();
        try {
            //list = (List<T>) redisTemplate.opsForList().rightPop(key);
            list = redisTemplate.opsForList().range(key, 0, -1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return list;
    }

    /**
     * 根据缓存的键(key)，获取缓存list(以List方式保存)
     *
     * @param key 缓存的键
     * @return
     */
    public <T> List<T> getList1(final String key, long start, long end) {
        List<T> list = new ArrayList<T>();
        try {
            if (end == 0) {
                end = 5;
            }
            list = redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return list;
    }

    /**
     * 根据缓存的键(key)，获取缓存list(以Object方式保存)
     *
     * @param key 缓存的键
     * @return
     */
    public <T> List<T> getListObject(final String key) {
        List<T> list = new ArrayList<T>();
        try {
            ListOperations listOperation = redisTemplate.opsForList();
            long size = listOperation.size(key);
            for (int i = 0; i < size; i++) {
                list.add((T) listOperation.index(key, i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return list;
    }

    /**
     * 缓存Set数据集合
     *
     * @param key 缓存的键
     * @param set 待缓存的set对象
     * @return
     */
    public boolean setSets(final String key, Set<String> set) {
        boolean result = false;
        try {
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                redisTemplate.opsForSet().add(key, iterator.next());
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * @param key
     * @param otherKey
     * @description 找出兩個集合的并集，并進行遍历
     */
    public Set<String> union(String key, String otherKey) {
        try {
            SetOperations<String, String> oper = redisTemplate.opsForSet();
            return oper.union(key, otherKey);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return new HashSet<>();
    }

    /**
     * 缓存Object对象到Set
     *
     * @param key 缓存的键
     * @param obj 待缓存的obj对象
     * @return
     */
    public boolean setSetByObject(final String key, Object obj) {
        boolean result = false;
        try {
            redisTemplate.opsForSet().add(key, obj);
            result = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * 根据缓存key，获取Set数据
     *
     * @param key 缓存的键
     * @return
     */
    public Set<String> getSet(final String key) {
        Set<String> resultSet = null;
        try {
            resultSet = redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return resultSet;
    }

    /**
     * 判断set缓存是否有值
     *
     * @param key
     * @param skey
     * @return 如果为1表示有值，0表示没有值
     */
    public int existsSet(final String key, final String skey) {
        try {
            return redisTemplate.opsForSet().members(key).contains(skey) ? 1 : 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return 0;
    }

    /**
     * 删除set Key中的值
     *
     * @param key  缓存的key
     * @param skey 缓存的value
     */
    public boolean removeSetValue(final String key, final String skey) {
        try {
            Long remove = 0L;
            if (StringUtils.isNotBlank(skey)) {
                remove = redisTemplate.opsForSet().remove(key, skey);
            } else {
                redisTemplate.delete(key);
                remove = 1L;
            }
            return remove > 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return false;
    }


    /**
     * 删除zSet Key中的值
     *
     * @param key  缓存的key
     * @param zkey 缓存的value
     */
    public boolean removeZset(final String key, final String zkey) {
        try {
            Long remove = 0L;
            if (StringUtils.isNotBlank(zkey)) {
                remove = redisTemplate.opsForZSet().remove(key, zkey);
            }
            return remove > 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return false;
    }

    /**
     * 缓存Object对象
     *
     * @param key
     * @param obj
     * @return
     */
    public boolean setZset(final String key, Object obj) {
        boolean result = false;
        try {
            ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
            long num = zset.zCard(key);
            zset.add(key, obj, num);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    public Set<String> getZset(final String key) {
        Set<String> resultZset = null;
        try {

            resultZset = redisTemplate.opsForZSet().range(key, 0, -1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return resultZset;
    }



    public Double incrZset(final String key, String member, Double score) {
        Double resultZset = null;
        try {
            resultZset = redisTemplate.opsForZSet().incrementScore(key, member, score);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return resultZset;
    }


    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * ZSET添加(覆盖)元素
     *
     * @param key
     * @param id
     * @param score
     * @return
     */
    public boolean ZSetAdd(String key, String id, Double score) {
        Boolean add = false;
        try {
            add = redisTemplate.opsForZSet().add(key, id, score);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return (add != null);

    }

    /**
     * ZSET删除其中元素
     *
     * @param setKey
     * @param value
     * @return
     */
    public Long ZSetRemove(String setKey, String value) {
        Long remove = 0L;
        try {
            remove = redisTemplate.opsForZSet().remove(setKey, value);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return remove;

    }

    /**
     * 倒序输出
     *
     * @param key    有序集合键名称
     * @param min    最小分数值,可使用"-inf"(最小值)代替
     * @param max    最大分数值,可使用"+inf"(最大值)代替
     * @param offset 返回结果起始位置
     * @param count  返回结果数量
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeZSetByScoreWithScores(String key, final double min, final double max, final long offset,
                                                                                    final long count) {
        Set set = null;
        try {
            set = redisTemplate.opsForZSet()
                    .reverseRangeByScoreWithScores(key, min, max, offset, count);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return set;
    }

    /**
     * 排序正序
     * * @param key    有序集合键名称
     *
     * @param min    最小分数值,可使用"-inf"(最小值)代替
     * @param max    最大分数值,可使用"+inf"(最大值)代替
     * @param offset 返回结果起始位置
     * @param count  返回结果数量
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> rangeByScoreWithScores(String key, final double min, final double max, final long offset,
                                                                         final long count) {
        Set set = null;
        try {
            set = redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, offset, count);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return set;
    }


    /**
     * 获取总长度
     *
     * @param key
     * @return
     */
    public Long zCard(String key) {
        Long num = 0L;
        try {
            num = redisTemplate.opsForZSet()
                    .zCard(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return num;

    }

    public List<Map<String, Object>> getRedisHashVoByCode(Set<ZSetOperations.TypedTuple<Object>> typedTuples, String key) {
        List<Map<String, Object>> result = new LinkedList<>();
        try {
            if (typedTuples == null || typedTuples.size() == 0) {
                return null;
            }
            for (ZSetOperations.TypedTuple<Object> typedTuple : typedTuples) {
                String id = (String) typedTuple.getValue();
                HashMap<String, Object> vo = (HashMap<String, Object>) this
                        .getHashMapValue(key, id);
                if (vo != null && vo.size() != 0) {
                    result.add(vo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }

    /**
     * 根据有序集合取缓存中的对象
     *
     * @param typedTuples
     * @param key
     * @return
     */
    public List<Object> getRedisHashObject(Set<ZSetOperations.TypedTuple<Object>> typedTuples, String key) {
        List<Object> result = new LinkedList<>();
        try {
            if (typedTuples == null || typedTuples.size() == 0) {
                return null;
            }
            for (ZSetOperations.TypedTuple<Object> typedTuple : typedTuples) {
                String id = (String) typedTuple.getValue();
                Object vo = this.getHashMapValue(key, id);
                if (vo != null) {
                    result.add(vo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return result;
    }


    /**
     * 根据key获取到set集合的id，再根据id获取缓存实体对象
     *
     * @param key    获取set集合列表的key
     * @param objKey 实体对象的key
     * @param <T>
     * @return
     */
    public <T> List<T> getListObjectBySet(String key, String objKey) {
        List<T> list = new ArrayList<>();
        Set<String> set = getSet(key);
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            list.add((T) getObject(objKey + iterator.next()));
        }
        return list;
    }

    /**
     * 消息发布
     *
     * @param channel
     * @param message
     */
    public void sendMessage(String channel, Serializable message) {
        try {
            redisTemplate.convertAndSend(channel, message);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    /**
     * 增量(加)
     *
     * @param key key
     * @return
     */
    public long incr(String key) {
        try {
            return redisTemplate.opsForValue().increment(key, 1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return 0;
    }

    /**
     * 增量(减)
     *
     * @param key key
     * @return
     */
    public long decr(String key) {
        try {
            return redisTemplate.opsForValue().increment(key, -1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return 0;
    }

    /**
     * Hash增量(加)
     *
     * @param key key
     * @return
     */
    public long incr(String key, String hkey) {
        return incr(key, hkey, 1L);
    }

    /**
     * Hash增量(加)
     *
     * @param key key
     * @return
     */
    public long incr(String key, String hkey, long incr) {
        try {
            return redisTemplate.opsForHash().increment(key, hkey, incr);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return 0;
    }

    /**
     * Hash增量(减)
     *
     * @param key key
     * @return
     */
    public long decr(String key, String hkey) {
        try {
            return redisTemplate.opsForHash().increment(key, hkey, new Integer(-1));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return 0L;
    }


    //获取前99名排行
    public Set<ZSetOperations.TypedTuple> zSort(String key, Integer pageNo, Integer pageSize ) {
        try {
            return redisTemplate.opsForZSet().reverseRangeWithScores(key, (pageNo - 1) * pageSize,  (pageNo - 1) * pageSize + pageSize - 1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return null;
    }


    //zSet通过key获取排行
    public Long getNumByKey(String key, String member) {
        try {
            return redisTemplate.opsForZSet().reverseRank(key, member);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return -1L;
    }

    //获取分数
    public Double getZsetScore(final String key, String userId) {
        Double resultZset = null;
        try {

            resultZset = redisTemplate.opsForZSet().score(key, userId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            //redisTemplate.exec();
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
        return resultZset;
    }


    //修改key名
    public void renameKey(String oldKey, String newKey) {
        try {
            redisTemplate.rename(oldKey, newKey);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }

    public void expire(String key){
        try {
            redisTemplate.expire(key, (3600 * 24 * 30), TimeUnit.SECONDS);//设置30天的有效期
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
        }
    }
}
