package com.wuhao.gateway;

import com.wuhao.common.entity.InterfaceInfo;
import com.wuhao.common.entity.User;
import com.wuhao.gateway.Client.UserCilent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

//@SpringBootTest
class ApiGatewayApplicationTests {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UserCilent userCilent;

    @Test
    void contextLoads() {
         String path="http://localhost:8102/interface/randomname/";
         int size=path.length();
        System.out.println(path.substring(0,size-1));
    }

}
