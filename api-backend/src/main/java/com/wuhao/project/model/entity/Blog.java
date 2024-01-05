package com.wuhao.project.model.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName blog
 */
@TableName(value ="blog")
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
     * 文章Html格式
     */
    private String contenthtml;

    /**
     * 文章封面
     */
    private String contentcover;

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

    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    /**
     * 修改时间
     */
    private LocalDateTime updatetime;

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
        Blog other = (Blog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getContenthtml() == null ? other.getContenthtml() == null : this.getContenthtml().equals(other.getContenthtml()))
            && (this.getContentcover() == null ? other.getContentcover() == null : this.getContentcover().equals(other.getContentcover()))
            && (this.getAuthorid() == null ? other.getAuthorid() == null : this.getAuthorid().equals(other.getAuthorid()))
            && (this.getAuthorname() == null ? other.getAuthorname() == null : this.getAuthorname().equals(other.getAuthorname()))
            && (this.getAuthoravatar() == null ? other.getAuthoravatar() == null : this.getAuthoravatar().equals(other.getAuthoravatar()))
            && (this.getTag() == null ? other.getTag() == null : this.getTag().equals(other.getTag()))
            && (this.getIshot() == null ? other.getIshot() == null : this.getIshot().equals(other.getIshot()))
            && (this.getIstop() == null ? other.getIstop() == null : this.getIstop().equals(other.getIstop()))
            && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()))
            && (this.getUpdatetime() == null ? other.getUpdatetime() == null : this.getUpdatetime().equals(other.getUpdatetime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getContenthtml() == null) ? 0 : getContenthtml().hashCode());
        result = prime * result + ((getContentcover() == null) ? 0 : getContentcover().hashCode());
        result = prime * result + ((getAuthorid() == null) ? 0 : getAuthorid().hashCode());
        result = prime * result + ((getAuthorname() == null) ? 0 : getAuthorname().hashCode());
        result = prime * result + ((getAuthoravatar() == null) ? 0 : getAuthoravatar().hashCode());
        result = prime * result + ((getTag() == null) ? 0 : getTag().hashCode());
        result = prime * result + ((getIshot() == null) ? 0 : getIshot().hashCode());
        result = prime * result + ((getIstop() == null) ? 0 : getIstop().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        result = prime * result + ((getUpdatetime() == null) ? 0 : getUpdatetime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", content=").append(content);
        sb.append(", contenthtml=").append(contenthtml);
        sb.append(", contentcover=").append(contentcover);
        sb.append(", authorid=").append(authorid);
        sb.append(", authorname=").append(authorname);
        sb.append(", authoravatar=").append(authoravatar);
        sb.append(", tag=").append(tag);
        sb.append(", ishot=").append(ishot);
        sb.append(", istop=").append(istop);
        sb.append(", createtime=").append(createtime);
        sb.append(", updatetime=").append(updatetime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}