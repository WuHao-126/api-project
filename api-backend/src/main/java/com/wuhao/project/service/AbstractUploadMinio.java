package com.wuhao.project.service;

import com.wuhao.project.common.PropertyConfig;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.model.vo.UploadFileVo;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public abstract  class AbstractUploadMinio implements FileService{


    @Override
    public String uploadImage(UploadFileVo uploadFileVo) {
        return upload(uploadFileVo);
    }

    @Override
    public UploadFileVo upload(MultipartFile file){
        if(file.isEmpty()){
            throw new BusinessException("");
        }
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString().replace("-","")+suffix;
        File downloadUrl = new File(PropertyConfig.downloadUrl);
        if (!downloadUrl.exists()) {
            downloadUrl.mkdir();
        }
        String path = PropertyConfig.downloadUrl+newFileName;
        File cacheFile = new File(path);
        try {
            file.transferTo(cacheFile);
            UploadFileVo uploadFileVo = UploadFileVo.builder()
                    .absolutePath(path)
                    .newFileName(newFileName)
                    .originalFilename(originalFilename)
                    .contentType(contentType)
                    .build();
            uploadFile(uploadFileVo);
            return uploadFileVo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            cacheFile.delete();
        }
    };

    public abstract String upload(UploadFileVo fileVo);


    public abstract Boolean uploadFile(UploadFileVo fileVo) throws Exception;
}
