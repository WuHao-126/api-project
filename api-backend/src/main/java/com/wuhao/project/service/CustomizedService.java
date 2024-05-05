package com.wuhao.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.project.model.entity.Customized;

import java.util.List;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
public interface CustomizedService extends IService<Customized> {
    Page<Customized> getAllList(Page page);
}
