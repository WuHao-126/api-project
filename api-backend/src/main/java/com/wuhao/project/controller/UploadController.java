package com.wuhao.project.controller;

import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.common.Result;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.model.vo.UploadFileVo;
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


}
