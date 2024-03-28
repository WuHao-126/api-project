package com.wuhao.project.model.request.comment;

import lombok.Data;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
public class CommentInsertRequest {
    private Integer id;
    private Long articleId;
    private String commentContent;
    private Integer userId;
}
