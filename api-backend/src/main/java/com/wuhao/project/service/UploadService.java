package com.wuhao.project.service;

import java.io.InputStream;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public interface UploadService {
    String uploadFileBlog(String fileUrl,String objectName, String contentType);

    String uploadFileMarkdown(String fileUrl,String objectName, String contentType);

    String uploadFileInterface(String fileUrl,String objectName, String contentType);

    String uploadFileUser(String fileUrl,String objectName, String contentType);

    String uploadileWeb(String fileUrl,String objectName, String contentType);
}
