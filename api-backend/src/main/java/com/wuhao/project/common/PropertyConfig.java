package com.wuhao.project.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyConfig {

    public static String downloadUrl = "";

    @Value("${file.downloadUrl}")
    public  void setDownloadUrl(String downloadUrl) {
        PropertyConfig.downloadUrl = downloadUrl;
    }

}
