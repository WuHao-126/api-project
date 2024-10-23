package com.wuhao.project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "security.ignore")
public class IgnoreWhiteList {
    private static List<String> whites = new ArrayList<>();
    public static List<String> getWhites() {
        return whites;
    }
    public void setWhites(List<String> whites) {
        IgnoreWhiteList.whites = whites;
    }
}