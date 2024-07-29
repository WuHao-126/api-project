package com.wuhao.project.config;

import com.zhipu.oapi.ClientV4;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {
    @Bean
    public ClientV4 clientV4(){
       return new ClientV4.Builder("24d68a14e896645cb48a807b3407fe9d.3Tu1tQ3btePyMBBB").build();
    }
}