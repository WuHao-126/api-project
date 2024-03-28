package com.wuhao.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.project.common.IdRequest;
import com.wuhao.project.model.entity.Blog;
import com.wuhao.project.model.entity.Tag;
import com.wuhao.project.model.request.blog.BlogQueryRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
*
*/
public interface BlogService extends IService<Blog> {
    List<Tag> getTagList();

    QueryWrapper<Blog> getQueryWapper(BlogQueryRequest blogQueryRequest);

    Integer likeBlog(IdRequest idRequest, HttpServletRequest request);

    Page<Blog> getPageList(Page page, BlogQueryRequest blogQueryRequest,HttpServletRequest request);

    Integer collectBlog(IdRequest idRequest, HttpServletRequest request);

    List<Long> getMyCollection(Long id);
}
