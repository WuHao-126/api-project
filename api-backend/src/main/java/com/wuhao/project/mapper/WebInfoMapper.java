package com.wuhao.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuhao.project.model.entity.ExceptionalLog;
import com.wuhao.project.model.entity.Tag;
import com.wuhao.project.model.entity.WebInfo;
import com.wuhao.project.model.response.TimeoutInterfaceResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@Mapper
public interface WebInfoMapper extends BaseMapper {
    @Select("select * from tb_web_info")
    WebInfo getWebInfo();

    @Update("update tb_web_info set name=#{webinfo.name},logo=#{webinfo.logo},description=#{webinfo.description} where id=1")
    Boolean updateWebInfo(@Param("webinfo") WebInfo webInfo);

    @Select("select count(1) from tb_user")
    Integer getUserTotal();

    @Select("select count(1) from tb_user where state=1")
    Integer getDisableUserTotal();

    @Select("select count(1) from tb_blog")
    Integer getBlogTotal();

    @Select("select count(1) from tb_user")
    Integer getInterfaceTotal();


    Page<ExceptionalLog> getExceptionalList(Page page);

    @Select("select * from sys_tag")
    Page<Tag> getAllTags(Page page);

    @Delete("delete from sys_tag where id=#{id}")
    void deleteTag(Long id);

    @Insert("insert into sys_tag values(null,#{tag.name},#{tag.type},null,#{tag.other})")
    void addTag(@Param("tag") Tag tag);

    @Update("update sys_tag set name=#{tag.name},type=#{tag.type},other=#{tag.other} where id=#{tag.id}")
    void updateTag(@Param("tag") Tag tag);

    @Select("select * from sys_tag where id=#{id}")
    Tag getTagById(long id);
}
