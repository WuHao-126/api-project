package com.wuhao.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    public Result updateWebInfo(@RequestBody WebInfo webInfo){
        return Result.success(webInfoMapper.updateWebInfo(webInfo));
    }

    /**
     * 数据统计
     * @param webInfo
     * @return
     */
    @PostMapping("/notice")
    public Result saveNotice(@RequestBody  WebInfo webInfo){
        String notice = webInfo.getNotice();
        if(StringUtils.isEmpty(notice)){
            return Result.error(ErrorCode.PARAMS_ERROR);
        }
        stringRedisTemplate.opsForValue().set("api:web:notice",notice);
        return Result.success();
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
}
