package com.wuhao.project.service.impl;

import com.wuhao.common.service.InnerUserInterfaceInfoService;
import com.wuhao.project.service.UserInterfaceInfoService;

/**
 *
 */
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    UserInterfaceInfoService userInterfaceInfoService;
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId,userId);
    }

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */

}
