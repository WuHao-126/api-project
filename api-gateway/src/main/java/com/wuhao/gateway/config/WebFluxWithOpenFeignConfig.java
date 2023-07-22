package com.wuhao.gateway.config;

import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.stream.Collectors;

@Configuration
public class WebFluxWithOpenFeignConfig {
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }
    @Bean
    public Decoder decoder(){
        return new ResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter()));
    }
    private ObjectFactory<HttpMessageConverters> feignHttpMessageConverter(){
        HttpMessageConverters httpMessageConverters=new HttpMessageConverters
                (new MappingJackson2HttpMessageConverter());
        return ()->httpMessageConverters;
    }
}
