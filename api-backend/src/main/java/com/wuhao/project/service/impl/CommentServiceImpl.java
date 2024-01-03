package com.wuhao.project.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.project.mapper.CommentMapper;
import com.wuhao.project.model.entity.Comment;
import com.wuhao.project.service.CommentService;
import org.springframework.stereotype.Service;

/**
*
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
implements CommentService {

}
