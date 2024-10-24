package com.wuhao.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuhao.project.annotation.AuthCheck;
import com.wuhao.project.common.DeleteRequest;
import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.common.Result;
import com.wuhao.project.mapper.CustomizedMapper;
import com.wuhao.project.model.entity.Customized;
import com.wuhao.project.model.entity.User;
import com.wuhao.project.service.CustomizedService;
import com.wuhao.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
@RestController
@RequestMapping("/customized")
public class CustomizedController {
    @Autowired
    private UserService userService;

    @Autowired
    private CustomizedService customizedService;

    @PostMapping("/add")
    public Result insertCustomized(@RequestBody Customized customized){
        User loginUser = userService.getLoginUser();
        if(loginUser==null){
            return Result.error(ErrorCode.NOT_LOGIN_ERROR);
        }
        customized.setUserId(loginUser.getId());
        customizedService.save(customized);
        return Result.success();
    }

    @PostMapping("/page")
    public Result getPageList(Page page){
        User loginUser = userService.getLoginUser();
        if(loginUser==null){
            return Result.error(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long userId = loginUser.getId();
        Page pageList = customizedService.query().eq("userId", userId).page(page);
        return Result.success(pageList);
    }

    @PostMapping("/all")
    @AuthCheck(anyRole = {"admin,superadmin"})
    public Result getAllList(Page page){
        Page<Customized> list = customizedService.getAllList(page);
        return Result.success(list);
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody DeleteRequest deleteRequest){
        Long id = deleteRequest.getId();
        if(id==null){
            return Result.error(ErrorCode.PARAMS_NULL);
        }
        customizedService.removeById(id);
        return Result.success();
    }
    @PostMapping("/update")
    public Result updateState(@RequestBody Customized customized){
        boolean b = customizedService.updateById(customized);
        return Result.success();
    }
}
