package com.wuhao.project.controller;

import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.common.Result;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
    @PostMapping("/blog")
    public Result uploadFileBlog(@RequestParam("file") MultipartFile file,String buckent) throws IOException {
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
        uploadService.uploadFileBlog(absolutePath,originalFilename,contentType);
        return Result.success(originalFilename);
    }
    @PostMapping("/markdown")
    public Result UploadFileMarkdown(@RequestParam("file") MultipartFile file) throws IOException {
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
        String s = uploadService.uploadFileMarkdown(absolutePath, originalFilename, contentType);
        return Result.success(s);
    }
    @PostMapping("/interface")
    public Result UploadFileInterface(@RequestParam("file") MultipartFile file) throws IOException {
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
        String s = uploadService.uploadFileInterface(absolutePath, originalFilename, contentType);
        return Result.success(originalFilename);
    }
    @PostMapping("/user")
    public Result UploadFileUser(@RequestParam("file") MultipartFile file) throws IOException {
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
        String s = uploadService.uploadFileUser(absolutePath, originalFilename, contentType);
        return Result.success(originalFilename);
    }

}
