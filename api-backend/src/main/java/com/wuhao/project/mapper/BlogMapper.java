package com.wuhao.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuhao.project.model.entity.Blog;
import com.wuhao.project.model.entity.Tag;
import com.wuhao.project.model.request.blog.BlogQueryRequest;
import com.wuhao.project.model.response.HotBlogResponse;
import com.wuhao.project.model.response.HotUserResponse;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
* @Entity generator..Blog
*/
public interface BlogMapper extends BaseMapper<Blog> {


    Page<Blog> getPageList(Page page, @Param("request") BlogQueryRequest request);

    @Insert("Insert into tb_blog_like values(null,#{userId},#{blogId},null)")
    void addlike(@Param("userId") Long userId, @Param("blogId") Long blogId);

    @Delete("delete from tb_blog_like where blogId=#{blogId} and userId=#{userId}")
    void cancelLike(@Param("userId") Long userId, @Param("blogId") Long blogId);

    @Select("select count(1) from tb_blog_like where blogId=#{blogId} and userId=#{userId}")
    Integer isNoLike(@Param("userId") Long userId, @Param("blogId") Long blogId);

    @Insert("Insert into tb_blog_collect values(null,#{userId},#{blogId},null)")
    void addCollect(@Param("userId") Long userId, @Param("blogId") Long blogId);

    @Delete("delete from tb_blog_collect where blogId=#{blogId} and userId=#{userId}")
    void cancelCollect(@Param("userId") Long userId, @Param("blogId") Long blogId);

    @Select("select count(1) from tb_blog_collect where blogId=#{blogId} and userId=#{userId}")
    Integer isNoCollect(@Param("userId") Long userId, @Param("blogId") Long blogId);

    @Select("select blogId from tb_blog_collect where userId=#{id}")
    List<Long> getMyCollection(Long id);

    @Delete("delete from tb_blog_like where blogId=#{id}")
    void deleteLike(Long id);

    @Delete("delete from tb_blog_collect where blogId=#{id}")
    void deleteCollect(Long id);

    List<HotBlogResponse> getHotBlog();

    List<HotUserResponse> getHotUser();
}
