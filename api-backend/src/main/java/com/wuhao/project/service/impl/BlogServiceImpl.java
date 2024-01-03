package com.wuhao.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.project.mapper.BlogMapper;
import com.wuhao.project.model.entity.Blog;
import com.wuhao.project.service.BlogService;
import org.springframework.stereotype.Service;

/**
*
*/
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
implements BlogService {

}
