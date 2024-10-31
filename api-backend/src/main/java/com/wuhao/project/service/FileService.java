package com.wuhao.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuhao.project.model.entity.ToolFile;
import com.wuhao.project.model.vo.UploadFileVo;
import io.minio.MinioClient;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public interface FileService {

    String uploadImage(UploadFileVo uploadFileVo);

    String upload(UploadFileVo uploadFileVo);

    List<ToolFile> getToolFileList(Page<ToolFile> page, String name);

    UploadFileVo upload(MultipartFile file);

    Boolean insertToolFile(MultipartFile file, ToolFile toolFile);
}
