package com.wuhao.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.wuhao.project.annotation.AuthCheck;
import com.wuhao.project.annotation.Limiter;
import com.wuhao.project.common.DeleteRequest;
import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.common.IdRequest;
import com.wuhao.project.common.Result;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.model.entity.Blog;
import com.wuhao.project.model.entity.Comment;
import com.wuhao.project.model.entity.User;
import com.wuhao.project.model.request.blog.BlogQueryRequest;
import com.wuhao.project.model.response.HotBlogResponse;
import com.wuhao.project.service.BlogService;
import com.wuhao.project.service.CommentService;
import com.wuhao.project.service.UserService;
import com.wuhao.project.support.Id.algorithm.SnowFlakeAgorithm;
import com.wuhao.project.support.sensitive.TrafficLimiter;
import com.wuhao.project.support.sensitive.algorithm.CounterLimiter;
import com.wuhao.project.support.sensitive.algorithm.SlidingTimeWindowLimiter;
import com.wuhao.project.util.IdUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@RestController()
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    private TrafficLimiter limiter = new SlidingTimeWindowLimiter();

    /**
     * 获取博客列表
     * @param blogQueryRequest
     * @return
     */
    @PostMapping("/page")
    public Result getBlogPage(@RequestBody BlogQueryRequest blogQueryRequest,HttpServletRequest request){
        long current = blogQueryRequest.getCurrent();
        long pageSize = blogQueryRequest.getPageSize();
        Page<Blog> pageList=blogService.getPageList(new Page(current,pageSize),blogQueryRequest,request);
        return Result.success(pageList);
    }

    /**
     * 添加博客
     * @param blog
     * @param request
     * @return
     */
    @PostMapping("/insert")
    public Result insertBlog(@RequestBody Blog blog, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        if(loginUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        if(blog ==null || StringUtils.isBlank(blog.getTitle()) || StringUtils.isBlank(blog.getContent())){
            return Result.error(400,"请补充完成标题或者内容");
        }
        //设置文章用户信息
        blog.setId(IdUtils.getId());
        blog.setAuthorid(loginUser.getId());
        blog.setAuthorname(loginUser.getUserName());
        blog.setAuthoravatar(loginUser.getUserAvatar());
        blog.setCreatetime(LocalDateTime.now());
        boolean b = blogService.save(blog);
        if(b){
            return Result.success();
        }else{
            return Result.error(ErrorCode.INSERT_ERROR);
        }
    }

    /**
     * 删除博客
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    public Result deleteBlogById(@RequestBody DeleteRequest deleteRequest){
        if(deleteRequest==null){
            return Result.error(ErrorCode.PARAMS_NULL);
        }
        blogService.deleteBlog(deleteRequest.getId());
        return Result.success();
    }

    /**
     * 修改博客
     * @param blog
     * @return
     */
    @PostMapping("/update")
    public Result updateBlog(@RequestBody Blog blog,HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        if (loginUser==null){
            return Result.error(ErrorCode.USER_STATUS_ERROR);
        }
        //文章作者Id
        Long authorid = blog.getAuthorid();
        Long userId = loginUser.getId();
        if(!authorid.equals(userId)){
            return Result.error(ErrorCode.PARAMS_ERROR);
        }
        boolean b = blogService.updateById(blog);
        if(b){
            return Result.success();
        }else{
            return Result.error(ErrorCode.UPDATE_ERROR);
        }
    }

    /**
     * 根据id获取博客信息
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public Result getBlogById(@PathVariable Long id){
        if(id<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Blog byId = blogService.getById(id);
        if(byId==null){
            return Result.error(ErrorCode.NOT_FOUND_ERROR);
        }else{
            return Result.success(byId);
        }
    }
    @GetMapping("/tag")
    public Result getTagList(){
        return Result.success(blogService.getTagList());
    }

    /**
     * 给博客点赞
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/like")
    public Result likeBlog(@RequestBody IdRequest idRequest,HttpServletRequest request){
        Integer integer = blogService.likeBlog(idRequest, request);
        return Result.success(integer);
    }

    /**
     * 收藏博客
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/collect")
    public Result likeCollect(@RequestBody IdRequest idRequest,HttpServletRequest request){
        Integer integer = blogService.collectBlog(idRequest, request);
        return Result.success(integer);
    }

    /**
     * 添加评论
     * @param comment
     * @param request
     * @return
     */
    @PostMapping("/insert/comment")
    public Result insertComment(@RequestBody Comment comment,HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        if(loginUser==null){
            return Result.error(ErrorCode.NOT_LOGIN_ERROR);
        }
        String commentContent = comment.getCommentContent();
        if(StringUtils.isEmpty(commentContent)){
            return Result.error(ErrorCode.COMMENT_NULL);
        }
        boolean b = SensitiveWordHelper.contains(commentContent);
        if(b){
            return Result.error(ErrorCode.COMMENT_ILLEGAL);
        }
        comment.setUserId(loginUser.getId());
        comment.setUserName(loginUser.getUserName());
        comment.setUserAvatar(loginUser.getUserAvatar());
        comment.setCreateTime(new Date());
        boolean save = commentService.save(comment);
        if(save){
            return Result.success();
        }else{
            return Result.error(ErrorCode.OPERATION_ERROR);
        }
    }

    /**
     * 获取评论列表
     * @param id
     * @return
     */
    @GetMapping("/comment/list/{id}")
    public Result getCommentList(@PathVariable Long id){
        List<Comment> commentList = commentService.query().eq("articleId", id).orderByDesc("createTime").list();
        List<Comment> collect = commentList.stream().filter(s -> s.getLevel() == 0).collect(Collectors.toList());
        Map<Integer, List<Comment>> commentMap = commentList
                .stream()
                .filter(s -> s.getLevel() != 0)
                .collect(Collectors.groupingBy(Comment::getParentId, Collectors.toList()));
        for (Comment comment : collect) {
            Integer commentId = comment.getId();
            List<Comment> commentList1 = commentMap.get(commentId);
            comment.setChildCommentList(commentList1);
        }
        return Result.success(collect);
    }

    /**
     * 删除评论
     * @return
     */
    @PostMapping("/delete/comment")
    public Result deleteComment(@RequestBody DeleteRequest deleteRequest,HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        if(loginUser==null){
             return Result.error(ErrorCode.USER_STATUS_ERROR);
        }
        Long id = deleteRequest.getId();
        commentService.remove(new QueryWrapper<Comment>().eq("id",id).or().eq("parentId",id));
        return Result.success();
    }

    /**
     * 我的博客
     * @param idRequest
     * @return
     */
    @PostMapping("/my")
    public Result getMyBlog(@RequestBody IdRequest idRequest){
        Long id = idRequest.getId();
        if(id == null){
            return Result.error(ErrorCode.PARAMS_ERROR);
        }
        Page<Blog> page = blogService.query().eq("authorId", id).orderByDesc("createTime").page(new Page<Blog>(1, 10));
        return Result.success(page);
    }

    /**
     * 我的收藏
     * @param idRequest
     * @return
     */
    @PostMapping("/my/collect")
    public Result getMyCollection(@RequestBody IdRequest idRequest){
        Long id = idRequest.getId();
        if(id == null){
            return Result.error(ErrorCode.PARAMS_ERROR);
        }
        List<Long> blogIdList=blogService.getMyCollection(id);
        List<Blog> list=new ArrayList<>();
        for (Long aLong : blogIdList) {
            Blog byId = blogService.getById(aLong);
            if(byId!=null){
                list.add(byId);
            }
        }
        Page<Blog> page=new Page<>();
        page.setRecords(list);
        return Result.success(page);
    }

    @GetMapping("hot/blog")
    public Result getHotBlog(){
       return  Result.success(blogService.getHotBlog());
    }

    @GetMapping("hot/user")
    public Result getHotUser(){
        return Result.success(blogService.getHotUser());
    }

}
