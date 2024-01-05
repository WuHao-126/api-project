package com.wuhao.project.model.request.blog;

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
     * 时间范围
     */
    private LocalDateTime localDateTime;
}
