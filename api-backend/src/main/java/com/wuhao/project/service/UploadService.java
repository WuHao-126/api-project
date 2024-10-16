package com.wuhao.project.service;

import com.wuhao.project.model.vo.UploadFileVo;
import io.minio.MinioClient;

import java.io.InputStream;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public interface UploadService {

    String uploadImage(UploadFileVo uploadFileVo);

    String upload(MinioClient minioClient,UploadFileVo uploadFileVo);

}
