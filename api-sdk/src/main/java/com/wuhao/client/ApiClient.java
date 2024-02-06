package com.wuhao.client;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.wuhao.util.SignUtils;

import java.util.*;

public class ApiClient {

    private static final String GATEWAY_HOST = "http://localhost:8090";

    private static final List<String> list= Arrays.asList("GET","POST","DELETE","PUT");

    private String accessKey;
    private String secretKey;

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 帮用户设置请求头中的内容
     * @return
     */
    private Map<String, String> getHeaderMap() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        //获得随机数防止重放
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", SignUtils.getSign(secretKey));
        return hashMap;
    }

    public String getResponse(String url,String method,Map<String,Object> map){
        if(!list.contains(method)){
            throw new RuntimeException("没有该请求方法 405");
        }
       if("GET".equals(method)){
           HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/interface/randomname/")
                   .addHeaders(getHeaderMap())
                   .execute();
           String result = httpResponse.body();
           return result;
       }
       if("POST".equals(method)){
           HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/interface/userlogin/")
                   .headerMap(getHeaderMap(),false)
                   .form(map)
                   .execute();
           String result = httpResponse.body();
           return result;
       }
       return null;
    }
//
//    public String getRandomUsername(Object sdkUser) {
//        String userJson = JSONUtil.toJsonStr(sdkUser);
//        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/interface/randomname/")
//                .addHeaders(getHeaderMap(userJson))
//                .body(userJson)
//                .execute();
//        String result = httpResponse.body();
//        return result;
//    }
//
//    public LoginUserVO loginUser(String userAccount,String userPassword,Object sdkUser){
//        String userJson = JSONUtil.toJsonStr(sdkUser);
//        if(StringUtils.isAllEmpty(userAccount,userPassword)){
//            return null;
//        }
//        Map<String, Object> paramMap = new HashMap<>();
//        paramMap.put("userAccount", userAccount);
//        paramMap.put("userPassword",userPassword);
//        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/interface/userlogin/")
//                .headerMap(getHeaderMap(userJson),false)
//                .body(userJson)
//                .form(paramMap)
//                .execute();
//        String result = httpResponse.body();
//        Gson gson=new Gson();
//        LoginUserVO loginUserVO = gson.fromJson(result, LoginUserVO.class);
//        return loginUserVO;
//    }

}
