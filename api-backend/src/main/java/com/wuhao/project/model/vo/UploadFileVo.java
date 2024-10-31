package com.wuhao.project.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
@Builder
public class UploadFileVo {
    private String absolutePath;

    private String originalFilename;

    private String newFileName;

    private String contentType;

    private String type;

    private String bucketName;
}
