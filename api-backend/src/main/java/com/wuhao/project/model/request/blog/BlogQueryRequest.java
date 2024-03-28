package com.wuhao.project.model.request.blog;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wuhao.project.common.PageRequest;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Data
public class BlogQueryRequest extends PageRequest {
    /**
     * 用户ID
     */
    Long id;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 文章标签
     */
    private String tag;
    /**
     * 查询最热文章
     */
    private Integer isHot;
    /**
     * 最新文章
     */
    private String newBlog;
    /**
     * 时间范围
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime beginDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endDate;
}
