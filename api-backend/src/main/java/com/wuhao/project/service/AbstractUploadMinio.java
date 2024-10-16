package com.wuhao.project.service;

import com.wuhao.project.model.vo.UploadFileVo;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public abstract   class AbstractUploadMinio implements UploadService{

    @Value("${minio.endpoint}")
    private String endpoint="http://182.92.7.24:9090";

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    private String bucketName="api"; // 存储桶名称


    @Override
    public String uploadImage(UploadFileVo uploadFileVo) {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials("minioadmin","minioadmin")
                .build();
        return upload(minioClient,uploadFileVo);
    }

    public abstract String upload(MinioClient minioClient,UploadFileVo fileVo);
}
