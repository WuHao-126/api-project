package com.wuhao;


import com.wuhao.client.ApiTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("api.client")
@ConditionalOnClass()
public class ApiClientConfig {

    private String accessKey;
    private String secretKey;

    @Bean
    public ApiTemplate apiClient(){
        return new ApiTemplate(accessKey,secretKey);
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }


}
