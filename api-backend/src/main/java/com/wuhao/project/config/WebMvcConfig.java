package com.wuhao.project.config;

import com.wuhao.project.interceptor.RequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowCredentials(true)
//                .allowedMethods("GET", "HEAD", "Post", "PUT", "DELETE", "OPTIONS")
//                .maxAge(3600);;
//    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getRequestInterceptor())
                .addPathPatterns("/**")
                // .excludePathPatterns(ignoreWhiteList.getWhites())
                .order(-10);
    }
    /**
     * 自定义请求头拦截器
     */
    public RequestInterceptor getRequestInterceptor() {
        return new RequestInterceptor();
    }
}
