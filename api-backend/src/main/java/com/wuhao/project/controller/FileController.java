package com.wuhao.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.common.Result;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.model.entity.ToolFile;
import com.wuhao.project.model.vo.UploadFileVo;
import com.wuhao.project.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@RestController
@RequestMapping("/upload")
public class FileController {
    @Autowired
    private FileService fileService;


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
        String s = fileService.uploadImage(uploadFileVo);
        if("markdown".equals(type)){
            return Result.success(s);
        }
        return Result.success(originalFilename);
    }

    @PostMapping("/file")
    public Result UploadFileUsers(@RequestParam("file") MultipartFile file) throws IOException {
        if(file.isEmpty()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return Result.success(fileService.upload(file));
    }

    @PostMapping("/tool/insert")
    public Result insertToolFile(MultipartFile file,@ModelAttribute ToolFile toolFile) throws IOException {
        if(file.isEmpty()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return Result.success(fileService.insertToolFile(file,toolFile));
    }

    @GetMapping("/tool/list")
    public Result UploadFileTool(@RequestParam(value = "name") String name,
                                 @RequestParam(value = "currentSize",defaultValue = "1") Long currentSize,
                                 @RequestParam(value = "pageSize",defaultValue = "20") Long pageSize)  {
        Page<ToolFile> page = new Page<>(currentSize,pageSize);
        page.setRecords(fileService.getToolFileList(page, name));
        return Result.success(page);
    }


}
