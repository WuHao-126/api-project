package com.wuhao.project.service.impl;

import com.wuhao.project.model.vo.UploadFileVo;
import com.wuhao.project.service.AbstractUploadMinio;
import com.wuhao.project.service.UploadService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Service
public class UploadServiceImpl extends AbstractUploadMinio {
    @Value("${minio.endpoint}")
    private String endpoint="http://101.126.87.57:9090";
//
//    @Value("${minio.accessKey}")
//    private String accessKey;
//
//    @Value("${minio.secretKey}")
//    private String secretKey;
////
    private String bucketName="api"; // 存储桶名称
//
//    @Override
//    public String uploadImage(String absolutePath, String originalFilename, String contentType, String type) {
//        MinioClient minioClient = MinioClient.builder()
//                .endpoint(endpoint)
//                .credentials("minioadmin","minioadmin")
//                .build();
//        try {
//            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
//                    .bucket(bucketName) //桶名
//                    .filename(absolutePath) //本地文件路径（需要上传的文件）
//                    .object(type+"/"+originalFilename) //minio 上传后要放到那个文件下
//                    .contentType(contentType) //上传文件类型
//                    .build();
//            minioClient.uploadObject(uploadObjectArgs);
//        } catch (Exception e) {
//            // 处理异常
//            e.printStackTrace();
//        }
//        return endpoint+"/"+bucketName+"/"+type+"/"+originalFilename;
//    }

    @Override
    public String upload(MinioClient minioClient, UploadFileVo uploadFileVo) {

        String absolutePath = uploadFileVo.getAbsolutePath();
        String type = uploadFileVo.getType();
        String originalFilename = uploadFileVo.getOriginalFilename();
        String contentType = uploadFileVo.getContentType();

        try {
                UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket("api") //桶名
                    .filename(absolutePath) //本地文件路径（需要上传的文件）
                    .object(type+"/"+originalFilename) //minio 上传后要放到那个文件下
                    .contentType(contentType) //上传文件类型
                    .build();
            minioClient.uploadObject(uploadObjectArgs);
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
        return endpoint+"/"+bucketName+"/"+type+"/"+originalFilename;
    }

//    public void deleteFile(String objectName) {
//        try {
//            MinioClient minioClient = MinioClient.builder()
//                    .endpoint(endpoint)
//                    .credentials(accessKey, secretKey)
//                    .build();
//
//            minioClient.removeObject(
//                    RemoveObjectArgs.builder()
//                            .bucket(bucketName)
//                            .object(objectName)
//                            .build()
//            );
//        } catch (Exception e) {
//            // 处理异常
//            e.printStackTrace();
//        }
//    }
}
