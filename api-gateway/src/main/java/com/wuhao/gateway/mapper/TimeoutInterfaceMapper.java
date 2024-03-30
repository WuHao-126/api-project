package com.wuhao.gateway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuhao.gateway.entity.TimeoutInterface;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Mapper
public interface TimeoutInterfaceMapper extends BaseMapper<TimeoutInterface> {
    @Select("select id from tb_interface_info where url=#{url}")
    Long getInterfaceId(String url);

    @Update("update `tb_interface_info` set useTotal=useTotal+1 where id=#{id}")
    void updateInterfaceTotal(Long id);
}
