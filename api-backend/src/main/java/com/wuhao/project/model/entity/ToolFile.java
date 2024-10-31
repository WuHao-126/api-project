package com.wuhao.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tb_tool_file")
public class ToolFile {
    /**
     * 主键
     */
    private Long id;
    /**
     * 工具包名称
     */
    private String name;
    /**
     *描述
     */
    private String description;
    /**
     *文件名
     */
    private String fileName;
    /**
     *原始文件名
     */
    private String originalFileName;

    private Long createBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
