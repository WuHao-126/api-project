package com.wuhao.project;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.concurrent.*;


@SpringBootApplication
public class MainrApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainrApplication.class, args);
    }
    @Bean
    public ThreadPoolExecutor getThreadPool(){
        // 创建线程池
        return new ThreadPoolExecutor(20,
                50,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
