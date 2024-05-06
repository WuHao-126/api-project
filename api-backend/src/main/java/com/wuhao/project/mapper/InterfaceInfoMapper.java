package com.wuhao.project.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuhao.project.model.entity.InterfaceInfo;
import com.wuhao.project.model.response.TimeoutInterfaceResponse;

/**
 * @Entity generator.domain.InterfaceInfo
 */
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {

    Page<TimeoutInterfaceResponse> getTimeoutList(Page page);

    void deleteTimeoutData(Long id);
}




