package com.wuhao.project.model.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName blog
 */
@TableName(value ="tb_blog")
@Data
public class Blog implements Serializable {
    /**
     * 文章Id
     */
    @TableId
    private Long id;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章封面
     */
    private String cover;

    /**
     * 作者ID
     */
    private Long authorid;

    /**
     * 作者昵称
     */
    private String authorname;

    /**
     * 作者头像
     */
    private String authoravatar;

    /**
     * 标签
     */
    private String tag;

    /**
     * 热度
     */
    private Byte ishot;

    /**
     * 是否置顶（0：否 1：是）
     */
    private Byte istop;

    @TableField(exist = false)
    private Integer likeNum;

    @TableField(exist = false)
    private Integer collectNum;

    @TableField(exist = false)
    private Integer messageNum;

    @TableField(exist = false)
    private Boolean isNoLike;

    @TableField(exist = false)
    private Boolean isNoCollect;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private LocalDateTime createtime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private LocalDateTime updatetime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}