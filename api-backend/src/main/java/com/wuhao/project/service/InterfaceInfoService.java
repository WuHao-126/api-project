package com.wuhao.project.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.project.model.entity.InterfaceInfo;
import com.wuhao.project.model.response.TimeoutInterfaceResponse;

import java.util.List;


/**
 *
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {


    InterfaceInfo getInterfaceInfoByUrl(String url,String method);

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);

    Page<TimeoutInterfaceResponse> getTimeoutList(Page page);

    void deleteTimeoutData(Long id);
}
