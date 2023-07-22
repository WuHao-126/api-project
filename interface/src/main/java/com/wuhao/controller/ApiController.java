package com.wuhao.controller;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wuhao.common.Vo.LoginUserVO;
import com.wuhao.common.entity.User;
import com.wuhao.mapper.UsernameMapper;
import com.wuhao.mode.domain.Username;
import com.wuhao.service.UserService;
import com.wuhao.util.SignUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/interface")
@ResponseBody
public class ApiController {
    @Autowired
    private UsernameMapper usernameMapper;
    @Autowired
    private UserService userService;

    /**
     * 获取随机用户名
     * @return
     */
    @GetMapping("/randomname")
    public String userName(HttpServletRequest request){
        Long aLong = usernameMapper.selectCount(null);
        long l = RandomUtil.randomLong(1, aLong);
        QueryWrapper<Username> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",l);
        Username username = usernameMapper.selectOne(queryWrapper);
        return username.getName();
    }
    @PostMapping("/userlogin")
    public LoginUserVO userLogin(String userAccount,String userPassword){
        if(StringUtils.isAllEmpty(userAccount,userPassword)){
            return null;
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword);
        return loginUserVO;
    }

}
