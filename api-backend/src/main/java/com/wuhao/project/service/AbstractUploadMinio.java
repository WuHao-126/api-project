package com.wuhao.project.service;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public abstract class AbstractUploadMinio implements UploadService{

    @Value("${minio.endpoint}")
    private String endpoint="http://182.92.7.24:9090";

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    private String bucketName="api"; // 存储桶名称

//    @Override
    public String uploadImage(String fileUrl, String objectName, String contentType) {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials("minioadmin","minioadmin")
                .build();

        return null;
    }
}
