package com.wuhao.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication()
public class MainrApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainrApplication.class, args);
    }

}
