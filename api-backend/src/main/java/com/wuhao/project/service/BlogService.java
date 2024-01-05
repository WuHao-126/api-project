package com.wuhao.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.project.model.entity.Blog;
import com.wuhao.project.model.request.blog.BlogQueryRequest;

/**
*
*/
public interface BlogService extends IService<Blog> {
    QueryWrapper<Blog> getQueryWapper(BlogQueryRequest blogQueryRequest);
}
