package com.wuhao.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuhao.project.model.entity.ToolFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ToolFileMapper extends BaseMapper<ToolFile> {
    List<ToolFile> getToolFileList(@Param("page") Page<ToolFile> page, @Param("name") String name);
}
