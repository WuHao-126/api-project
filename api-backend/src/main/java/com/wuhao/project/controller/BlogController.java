package com.wuhao.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuhao.project.common.DeleteRequest;
import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.common.IdRequest;
import com.wuhao.project.common.Result;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.model.entity.Blog;
import com.wuhao.project.model.request.blog.BlogQueryRequest;
import com.wuhao.project.service.BlogService;
import com.wuhao.project.service.CommentService;
import com.wuhao.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@RestController("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @PostMapping("page")
    public Result getBlogPage(@RequestBody BlogQueryRequest blogQueryRequest){
        long current = blogQueryRequest.getCurrent();
        long pageSize = blogQueryRequest.getPageSize();
        Page<Blog> page = blogService.page(new Page<>(current, pageSize), blogService.getQueryWapper(blogQueryRequest));
        return Result.success(page);
    }

    @PostMapping("/insert")
    public Result insertBlog(@RequestBody Blog blog){
        boolean b = blogService.save(blog);
        if(b){
            return Result.success();
        }else{
            return Result.error(ErrorCode.INSERT_ERROR);
        }
    }

    @PostMapping("/delete")
    public Result deleteBlogById(@RequestBody DeleteRequest deleteRequest){
        boolean b = blogService.removeById(deleteRequest.getId());
        if(b){
            return Result.success();
        }else{
            return Result.error(ErrorCode.DELETE_ERROR);
        }
    }

    @PostMapping("/update")
    public Result updateBlog(@RequestBody Blog blog){
        boolean b = blogService.updateById(blog);
        if(b){
            return Result.success();
        }else{
            return Result.error(ErrorCode.UPDATE_ERROR);
        }
    }

    @PostMapping("/select/id")
    public Result getBlogById(@RequestBody IdRequest idRequest){
        Long id = idRequest.getId();
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

    @PostMapping("/insert/comment")
    public Result insertComment(){
        return Result.success();
    }

    @PostMapping("/delete/comment")
    public Result deleteComment(){
        return Result.success();
    }

}
