package com.wuhao.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.project.model.entity.InterfaceInfo;
import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.mapper.InterfaceInfoMapper;
import com.wuhao.project.model.response.TimeoutInterfaceResponse;
import com.wuhao.project.service.InterfaceInfoService;
import com.wuhao.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{
    @Autowired
    InterfaceInfoMapper interfaceInfoMapper;
    @Autowired
    InterfaceInfoService interfaceInfoService;
    @Autowired
    UserService userService;


    @Override
    public InterfaceInfo getInterfaceInfoByUrl(String url,String method) {
        QueryWrapper<InterfaceInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("url",url);
        queryWrapper.eq("method",method);
        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(queryWrapper);
        return interfaceInfo;
    }

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

    @Override
    public Page<TimeoutInterfaceResponse> getTimeoutList(Page page) {
        Page<TimeoutInterfaceResponse> timeList = interfaceInfoMapper.getTimeoutList(page);
        return timeList;
    }

    @Override
    public void deleteTimeoutData(Long id) {
        interfaceInfoMapper.deleteTimeoutData(id);
    }
}




