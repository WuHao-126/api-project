package com.wuhao.project.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.project.mapper.CustomizedMapper;
import com.wuhao.project.model.entity.Customized;
import com.wuhao.project.service.CustomizedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Service
public class CustomizedServiceimpl extends ServiceImpl<CustomizedMapper, Customized> implements CustomizedService {

    @Autowired
    private CustomizedMapper customizedMapper;

    @Override
    public Page<Customized> getAllList(Page page) {
        Page<Customized> allList = customizedMapper.getAllList(page);
        return allList;
    }
}
