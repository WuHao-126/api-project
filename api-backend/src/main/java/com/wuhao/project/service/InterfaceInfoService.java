package com.wuhao.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wuhao.common.entity.InterfaceInfo;


/**
 *
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {


    InterfaceInfo getInterfaceInfoByUrl(String url,String method);
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);
}
