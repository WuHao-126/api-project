package com.wuhao.project.support.Id.algorithm;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import com.wuhao.project.support.Id.IdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: 雪花算法
 */
public class SnowFlakeAgorithm implements IdWorker {
    private final long workerId;
    private static final long START_TIMESTAMP = 1625097600000L; // 设置起始时间戳（2021-07-01 00:00:00）
    private static final long SEQUENCE_BITS = 8; // 调整序列号位数为 8 位
    private static final long WORKER_ID_BITS = 8; // 调整机器标识位数为 8 位
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1; // 最大序列号
    private static final long MAX_WORKER_ID = (1L << WORKER_ID_BITS) - 1; // 最大机器标识

    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowFlakeAgorithm(long workerId) {
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException("Worker ID must be between 0 and " + MAX_WORKER_ID);
        }
        this.workerId = workerId;
    }

    public synchronized Long getId() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID for " + (lastTimestamp - currentTimestamp) + " milliseconds.");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                // 当前毫秒的序列号用完，等待下一毫秒
                while (currentTimestamp <= lastTimestamp) {
                    currentTimestamp = System.currentTimeMillis();
                }
            }
        } else {
            // 不同毫秒，序列号重置
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        // 生成 ID
        long id = ((currentTimestamp - START_TIMESTAMP) << (SEQUENCE_BITS + WORKER_ID_BITS)) |
                (workerId << SEQUENCE_BITS) |
                sequence;

        // 保留 16 位
        return id & 0xFFFF;
    }
}
