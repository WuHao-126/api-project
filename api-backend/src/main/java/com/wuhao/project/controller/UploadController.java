package com.wuhao.project.controller;

import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.common.Result;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.model.vo.UploadFileVo;
import com.wuhao.project.service.UploadService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;

//    @Autowired
//    private MinioClient minioClient;

    @PostMapping("/image")
    public Result UploadFileUser(@RequestParam("file") MultipartFile file,@RequestParam("type") String type) throws IOException {
        if(file.isEmpty()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //创建临时文件
        File tempFile=File.createTempFile("minio",".temp");
        file.transferTo(tempFile);
        //获得文件路径
        String absolutePath = tempFile.getAbsolutePath();
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        UploadFileVo uploadFileVo = UploadFileVo.builder()
                .absolutePath(absolutePath)
                .originalFilename(originalFilename)
                .contentType(contentType)
                .type(type)
                .build();
        String s = uploadService.uploadImage(uploadFileVo);
        if("markdown".equals(type)){
            return Result.success(s);
        }
        return Result.success(originalFilename);
    }

    @PostMapping("/file")
    public Result UploadFileUsers(String name) throws IOException {
//        if(file.isEmpty()){
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        String contentType = file.getContentType();
//        String originalFilename = file.getOriginalFilename();
//        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
//        long size = file.getSize();
//        File file1 = new File("E://aa//");
//        if(!file1.exists()){
//            file1.mkdir();
//        }
//        String string = UUID.randomUUID().toString();
//        String path = string + substring;
//        File file2 = new File("E://aa//"+path);
//        file.transferTo(file2);
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://101.126.87.57:9090")
                .credentials("minioadmin","minioadmin")
                .build();
        try {
            String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs
                    .builder()
                    .object("user/"+name)
                    .bucket("api")
                    .method(Method.GET)
                    .expiry(7, TimeUnit.DAYS)
                    .build());
            return Result.success(url);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
