package com.wuhao.support.sensitive.algorithm;



import com.wuhao.common.Constact;
import com.wuhao.support.sensitive.TrafficLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Component
public class SlidingTimeWindowLimiter implements TrafficLimiter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final Integer limit = 100;


    @Override
    public Boolean allowRequest(String fname) {
        String key= Constact.LIMIT_KEY+fname;
        // 当前时间戳（毫秒）
        long currentTime = System.currentTimeMillis();

        // 窗口开始时间是当前时间减去 60 秒（60000 毫秒）
        long windowStart = currentTime - 60 * 1000;

        // 使用 Redis 的 ZSet 操作
        ZSetOperations<String, String> zSetOps = stringRedisTemplate.opsForZSet();

        // 删除窗口开始时间之前的所有数据
        zSetOps.removeRangeByScore(key, Double.NEGATIVE_INFINITY, windowStart);

        // 计算当前窗口内的请求数
        Long currentRequests = zSetOps.zCard(key);

        // 如果当前请求数小于限制，则允许当前请求，并将其添加到窗口中
        if (currentRequests != null && currentRequests < limit) {
            zSetOps.add(key, String.valueOf(currentTime), currentTime);
            return true;
        }
        // 否则，拒绝请求
        return false;
    }
}
