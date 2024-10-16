package com.wuhao.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Configuration
public class ThreadPoolConfig {

    @Bean("blogThreadPoolExecutor")
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                10,
                20,
                1,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
