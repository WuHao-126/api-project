package com.wuhao.project.model.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private String userIcon;

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
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Comment other = (Comment) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getArticleId() == null ? other.getArticleId() == null : this.getArticleId().equals(other.getArticleId()))
            && (this.getCommentContent() == null ? other.getCommentContent() == null : this.getCommentContent().equals(other.getCommentContent()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getUserIcon() == null ? other.getUserIcon() == null : this.getUserIcon().equals(other.getUserIcon()))
            && (this.getReplyId() == null ? other.getReplyId() == null : this.getReplyId().equals(other.getReplyId()))
            && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
            && (this.getReplyNum() == null ? other.getReplyNum() == null : this.getReplyNum().equals(other.getReplyNum()))
            && (this.getPraiseNum() == null ? other.getPraiseNum() == null : this.getPraiseNum().equals(other.getPraiseNum()))
            && (this.getDownNum() == null ? other.getDownNum() == null : this.getDownNum().equals(other.getDownNum()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getLevel() == null ? other.getLevel() == null : this.getLevel().equals(other.getLevel()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getArticleId() == null) ? 0 : getArticleId().hashCode());
        result = prime * result + ((getCommentContent() == null) ? 0 : getCommentContent().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getUserIcon() == null) ? 0 : getUserIcon().hashCode());
        result = prime * result + ((getReplyId() == null) ? 0 : getReplyId().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getReplyNum() == null) ? 0 : getReplyNum().hashCode());
        result = prime * result + ((getPraiseNum() == null) ? 0 : getPraiseNum().hashCode());
        result = prime * result + ((getDownNum() == null) ? 0 : getDownNum().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getLevel() == null) ? 0 : getLevel().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", articleId=").append(articleId);
        sb.append(", commentContent=").append(commentContent);
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", userIcon=").append(userIcon);
        sb.append(", replyId=").append(replyId);
        sb.append(", parentId=").append(parentId);
        sb.append(", replyNum=").append(replyNum);
        sb.append(", praiseNum=").append(praiseNum);
        sb.append(", downNum=").append(downNum);
        sb.append(", status=").append(status);
        sb.append(", level=").append(level);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}