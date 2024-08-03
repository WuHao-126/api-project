package com.wuhao.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuhao.project.annotation.AuthCheck;
import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.common.Result;
import com.wuhao.project.mapper.WebInfoMapper;
import com.wuhao.project.model.entity.ExceptionalLog;
import com.wuhao.project.model.entity.Tag;
import com.wuhao.project.model.entity.WebInfo;
import com.wuhao.project.model.response.TimeoutInterfaceResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@RestController
@RequestMapping("/web")
@Slf4j
public class WebInfoController {
    @Resource
    private WebInfoMapper webInfoMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 获取服务器信息
     * @return
     */
    @GetMapping
    public Result getWebInfo(){
        return Result.success(webInfoMapper.getWebInfo());
    }

    /**
     * 修改服务器信息
     * @param webInfo
     * @return
     */
    @PostMapping
    @AuthCheck(anyRole = {"superadmin","admin"})
    public Result updateWebInfo(@RequestBody WebInfo webInfo){
        return Result.success(webInfoMapper.updateWebInfo(webInfo));
    }

    /**
     * 数据统计
     * @param webInfo
     * @return
     */
    @PostMapping("/notice/save")
    @AuthCheck(anyRole = {"superadmin","admin"})
    public Result saveNotice(@RequestBody  WebInfo webInfo){
        String notice = webInfo.getNotice();
        Integer day = webInfo.getDay();
        if(StringUtils.isEmpty(notice)){
            return Result.error(ErrorCode.PARAMS_ERROR);
        }
        if(day==null){
            return Result.error(100,"发布时间最少为一天");
        }
        stringRedisTemplate.opsForValue().set("api:web:notice",notice,day, TimeUnit.DAYS);
        return Result.success();
    }
    @GetMapping("/notice/delete")
    @AuthCheck(anyRole = {"superadmin","admin"})
    public Result deleteNotice(){
        stringRedisTemplate.delete("api:web:notice");
        return Result.success();
    }

    @GetMapping("/notice")
    public Result getNotice(){
        String s = stringRedisTemplate.opsForValue().get("api:web:notice");
        if(StringUtils.isEmpty(s)){
            return Result.error(ErrorCode.NOTICE_NULL);
        }
        return Result.success(s);
    }
    @GetMapping("/exceptional")
    public Result getExceptionalList(Page page){
        Page<ExceptionalLog> page1=webInfoMapper.getExceptionalList(page);
        return Result.success(page1);
    }

    @PostMapping("/all/tag")
    public Result getTagsList(Page page){
        Page<Tag> page1=webInfoMapper.getAllTags(page);
        return Result.success(page1);
    }

    @GetMapping("/tag/delete")
    @AuthCheck(anyRole = {"admin,superadmin"})
    public Result deleteTag(Long id){
        webInfoMapper.deleteTag(id);
        return Result.success();
    }

    /**
     * 添加标签
     * @return
     */
    @PostMapping("/tag/add")
    @AuthCheck(anyRole = {"superadmin","admin"})
    public Result addTag(@RequestBody Tag tag){
        webInfoMapper.addTag(tag);
        return Result.success();
    }

    @PostMapping("/tag/update")
    @AuthCheck(anyRole = {"superadmin","admin"})
    public Result updateTag(@RequestBody Tag tag){
        webInfoMapper.updateTag(tag);
        return Result.success();
    }
}
