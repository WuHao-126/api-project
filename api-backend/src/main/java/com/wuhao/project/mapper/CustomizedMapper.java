package com.wuhao.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuhao.project.model.entity.Customized;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Mapper
public interface CustomizedMapper extends BaseMapper<Customized> {
    Page<Customized> getAllList(Page page);
}
