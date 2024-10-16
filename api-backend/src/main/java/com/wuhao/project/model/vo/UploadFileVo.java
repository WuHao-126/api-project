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
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileVo {

    String absolutePath;
    String originalFilename;
    String contentType;
    String type;
    String bucketName;
}
