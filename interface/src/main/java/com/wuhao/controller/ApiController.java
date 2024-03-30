package com.wuhao.controller;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.wuhao.mapper.UsernameMapper;
import com.wuhao.model.entity.User;
import com.wuhao.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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
    public User userName(String name){
        User user=new User();
        user.setAge("18");
        user.setName(name);
        return user;
    }
    @PostMapping("/user")
    public User useraaa(@RequestBody User user){
        return user;
    }


    @GetMapping("/qq")
    public String qq(Long qq) {
        Map<String,Object> map=new HashMap<>();
        map.put("key","db5ca4ef3fa94ae492c570cc19910f18");
        map.put("qq",qq);
        HttpResponse response = HttpRequest.get("http://japi.juhe.cn/qqevaluate/qq").form(map).execute();
        String body = response.body();
        System.out.println(body);
        return body;
    }

    @GetMapping("xing")
    public String xing(String xing) {
        Map<String,Object> map=new HashMap<>();
        map.put("key","75ea046e81c6b98bd7d1403405d18989");
        map.put("xing",xing);
        HttpResponse response = HttpRequest.get("http://apis.juhe.cn/fapigx/surname/query").form(map).execute();
        String body = response.body();
        return body;
    }

//    支持类型
//    top(推荐,默认)
//    guonei(国内)
//    guoji(国际)
//    yule(娱乐)
//    tiyu(体育)
//    junshi(军事)
//    keji(科技)
//    caijing(财经)
//    youxi(游戏)
//    qiche(汽车)
//    jiankang(健康)
    @GetMapping("/news")
    public String news(HttpServletRequest request) {
        String type = (String)request.getAttribute("type");
//        Map<String,Object> map=new HashMap<>();
//        map.put("key","cd727fc5bf00ff1db61f3c77596e4d67");
//        map.put("type","yule");
//        HttpResponse response = HttpRequest.get("http://v.juhe.cn/toutiao/index").form(map).execute();
//        String body = response.body();
//        return body;
        return type;
    }


}
