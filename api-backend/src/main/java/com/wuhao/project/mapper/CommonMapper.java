package com.wuhao.project.mapper;

import com.wuhao.project.model.entity.Tag;

import java.util.List;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public interface CommonMapper {
    List<Tag> getBlogTag(String type);
}
