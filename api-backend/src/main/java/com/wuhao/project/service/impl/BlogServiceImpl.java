package com.wuhao.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.common.IdRequest;
import com.wuhao.project.constant.CommonConstant;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.mapper.BlogMapper;
import com.wuhao.project.mapper.CommonMapper;
import com.wuhao.project.model.entity.Blog;
import com.wuhao.project.model.entity.Comment;
import com.wuhao.project.model.entity.Tag;
import com.wuhao.project.model.entity.User;
import com.wuhao.project.model.request.blog.BlogQueryRequest;
import com.wuhao.project.model.response.HotBlogResponse;
import com.wuhao.project.model.response.HotUserResponse;
import com.wuhao.project.service.BlogService;
import com.wuhao.project.service.CommentService;
import com.wuhao.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
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
    @Autowired
    private CommentService commentService;

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
        QueryWrapper<Blog> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(id>0,"authorId",id);
        queryWrapper.like(StringUtils.isBlank(title),"title",title);
        return queryWrapper;
    }

    @Override
    public Integer likeBlog(IdRequest idRequest) {
        User loginUser = userService.getLoginUser();
        if(loginUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //登录用户ID
        Long userId = loginUser.getId();
        //博客ID
        Long blogId = idRequest.getId();
        Long add = stringRedisTemplate.opsForSet().add(CommonConstant.BLOG_LIKE+blogId, userId + "");
        if(add>0){
            blogMapper.addlike(userId,blogId);
            return 1;
        }else{
            Long remove = stringRedisTemplate.opsForSet().remove(CommonConstant.BLOG_LIKE + blogId, userId + "");
            blogMapper.cancelLike(userId,blogId);
            return -1;
        }
    }

    @Override
    public Page<Blog> getPageList(Page page, BlogQueryRequest blogQueryRequest) {
        Page<Blog> pageList=blogMapper.getPageList(page,blogQueryRequest);
        List<Blog> records = pageList.getRecords();
        User loginUser = userService.getLoginUser();
        if(loginUser==null){
            records.forEach(
                    e->{
                        e.setIsNoLike(false);
                        e.setIsNoCollect(false);
                    }
            );
        }else{
            Long userId = loginUser.getId();
            records.forEach(
                    e->{
                        Long id = e.getId();
                        Integer noLike = blogMapper.isNoLike(userId, id);
                        Integer noCollect = blogMapper.isNoCollect(userId, id);
                        if(noLike>0){
                            e.setIsNoLike(true);
                        }else{
                            e.setIsNoLike(false);
                        }
                        if(noCollect>0){
                            e.setIsNoCollect(true);
                        }else {
                            e.setIsNoCollect(false);
                        }
                    }
            );
        }
        return pageList;
    }

    @Override
    public Integer collectBlog(IdRequest idRequest) {
        User loginUser = userService.getLoginUser();
        if(loginUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //登录用户ID
        Long userId = loginUser.getId();
        //博客ID
        Long blogId = idRequest.getId();
        Long add = stringRedisTemplate.opsForSet().add(CommonConstant.BLOG_Collect+blogId, userId + "");
        if(add>0){
            blogMapper.addCollect(userId,blogId);
            return 1;
        }else{
            Long remove = stringRedisTemplate.opsForSet().remove(CommonConstant.BLOG_Collect + blogId, userId + "");
            blogMapper.cancelCollect(userId,blogId);
            return -1;
        }
    }

    @Override
    public List<Long> getMyCollection(Long id) {
        return blogMapper.getMyCollection(id);
    }

    @Override
    @Transactional
    public void deleteBlog(Long id) {
        log.error(id+"");
        //删除redis中的数据
        stringRedisTemplate.delete(CommonConstant.BLOG_Collect+id);
        stringRedisTemplate.delete(CommonConstant.BLOG_LIKE+id);
        //删除博客
        blogMapper.deleteById(id);
        //删除相关评论
        commentService.remove(new QueryWrapper<Comment>().eq("articleId", id));
        //删除收藏
        blogMapper.deleteLike(id);
        blogMapper.deleteCollect(id);
    }

    @Override
    public List<HotBlogResponse> getHotBlog() {
        stringRedisTemplate.opsForZSet();
        return blogMapper.getHotBlog();
    }

    @Override
    public List<HotUserResponse> getHotUser() {
        return blogMapper.getHotUser();
    }
}
