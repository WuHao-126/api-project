package com.wuhao.project.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuhao.project.mapper.ToolFileMapper;
import com.wuhao.project.model.entity.ToolFile;
import com.wuhao.project.model.vo.UploadFileVo;
import com.wuhao.project.service.AbstractUploadMinio;
import com.wuhao.project.util.IdUtils;
import com.wuhao.project.util.UserUtil;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Service
public class FileUploadServiceImpl extends AbstractUploadMinio {

    @Value("${minio.endpoint}")
    private String endpoint="http://101.126.87.57:9090";

    @Value("${spring.application.name}")
    private String bucketName; // 存储桶名称

    @Autowired
    private ToolFileMapper toolFileMapper;

    @Autowired
    private MinioClient minioClient;


    @Override
    public String upload(UploadFileVo uploadFileVo) {
        String absolutePath = uploadFileVo.getAbsolutePath();
        String type = uploadFileVo.getType();
        String originalFilename = uploadFileVo.getOriginalFilename();
        String contentType = uploadFileVo.getContentType();
        try {
                UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket(bucketName) //桶名
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

    @Override
    public Boolean uploadFile(UploadFileVo uploadFileVo) {
        String absolutePath = uploadFileVo.getAbsolutePath();
        String newFileName = uploadFileVo.getNewFileName();
        String contentType = uploadFileVo.getContentType();
        try {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket(bucketName) //桶名
                    .filename(absolutePath) //本地文件路径（需要上传的文件）
                    .object("tool/"+newFileName) //minio 上传后要放到那个文件下
                    .contentType(contentType) //上传文件类型
                    .build();
            minioClient.uploadObject(uploadObjectArgs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public List<ToolFile> getToolFileList(Page<ToolFile> page, String name) {
        List<ToolFile> toolFileList = toolFileMapper.getToolFileList(page, name);
        return toolFileList;
    }

    @Override
    public Boolean insertToolFile(MultipartFile file, ToolFile toolFile) {
        UploadFileVo upload = upload(file);
        toolFile.setId(IdUtils.getId());
        toolFile.setOriginalFileName(upload.getNewFileName());
        toolFile.setFileName(upload.getNewFileName());
        toolFile.setCreateTime(LocalDateTime.now());
        toolFile.setCreateBy(1l);
        toolFile.setUpdateTime(LocalDateTime.now());
        int insert = toolFileMapper.insert(toolFile);
        return insert > 0;
    }
}
