package com.wuhao.controller;


import com.wuhao.mapper.UsernameMapper;
import com.wuhao.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
        return "Hello,World";
//        Long aLong = usernameMapper.selectCount(null);
//        long l = RandomUtil.randomLong(1, aLong);
//        QueryWrapper<Username> queryWrapper=new QueryWrapper<>();
//        queryWrapper.eq("id",l);
//        Username username = usernameMapper.selectOne(queryWrapper);
//        return username.getName();
    }

}
