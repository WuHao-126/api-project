package com.wuhao.project.service;

import java.io.InputStream;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public interface UploadService {

    String uploadImage(String absolutePath, String originalFilename, String contentType, String type);

}
