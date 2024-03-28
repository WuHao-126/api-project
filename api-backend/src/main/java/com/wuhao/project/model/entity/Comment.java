package com.wuhao.project.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName comment
 */
@TableName(value ="tb_comment")
@Data
public class Comment implements Serializable {
    /**
     * 评论主键Id
     */
    @TableId
    private Integer id;

    /**
     * 评论文章Id
     */
    private Long articleId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 回复评论用户Id
     */
    private Long userId;

    /**
     * 回复评论用户昵称
     */
    private String userName;

    /**
     * 回复评论用户头像
     */
    private String userAvatar;

    /**
     * 回复评论Id
     */
    private Integer replyId;

    /**
     * 父评论ID
     */
    private Integer parentId;

    /**
     * 回复次数
     */
    private Integer replyNum;

    /**
     * 点赞数
     */
    private Integer praiseNum;

    /**
     * 踩点数
     */
    private Integer downNum;

    /**
     * @Enum(0，正常，1，隐藏)
     */
    private Boolean status;

    /**
     * 评论层级
     */
    private Integer level;

    /**
     * 评论时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private List<Comment> childCommentList;

}