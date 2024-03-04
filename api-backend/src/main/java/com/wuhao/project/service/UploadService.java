package com.wuhao.project.service;

import java.io.InputStream;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public interface UploadService {
    String uploadFile(String fileUrl,String objectName, String contentType);
}
