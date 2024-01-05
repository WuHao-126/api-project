package com.wuhao.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.project.mapper.BlogMapper;
import com.wuhao.project.model.entity.Blog;
import com.wuhao.project.model.request.blog.BlogQueryRequest;
import com.wuhao.project.service.BlogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
*
*/
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
implements BlogService {

    @Override
    public QueryWrapper<Blog> getQueryWapper(BlogQueryRequest blogQueryRequest) {
        Long id = blogQueryRequest.getId();
        String title = blogQueryRequest.getTitle();
        String tag = blogQueryRequest.getTag();
        Integer isHot = blogQueryRequest.getIsHot();
        LocalDateTime localDateTime = blogQueryRequest.getLocalDateTime();
        QueryWrapper<Blog> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(id>0,"authorId",id);
        queryWrapper.like(StringUtils.isBlank(title),"title",title);
        return queryWrapper;
    }
}
