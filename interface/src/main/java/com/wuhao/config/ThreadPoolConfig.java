package com.wuhao.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {

    @Bean("threadPoolExecutor01")
    public ThreadPoolExecutor threadPoolExecutor01() {

        // 创建线程池
        return new ThreadPoolExecutor(10,
                20,
                10000,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}