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
}
