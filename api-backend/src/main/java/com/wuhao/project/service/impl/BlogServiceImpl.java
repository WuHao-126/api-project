package com.wuhao.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.common.IdRequest;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.mapper.BlogMapper;
import com.wuhao.project.mapper.CommonMapper;
import com.wuhao.project.model.entity.Blog;
import com.wuhao.project.model.entity.Tag;
import com.wuhao.project.model.entity.User;
import com.wuhao.project.model.request.blog.BlogQueryRequest;
import com.wuhao.project.service.BlogService;
import com.wuhao.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
*
*/
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private CommonMapper commonMapper;

    @Override
    public List<Tag> getTagList() {
        return commonMapper.getBlogTag("BLOG");
    }

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

    @Override
    public boolean likeBlog(IdRequest idRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if(loginUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //登录用户ID
        Long userId = loginUser.getId();
        //博客ID
        Long blogId = idRequest.getId();
        Long add = stringRedisTemplate.opsForSet().add(blogId + "", userId + "");

        return false;
    }
}
