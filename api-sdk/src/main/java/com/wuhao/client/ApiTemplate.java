package com.wuhao.client;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.wuhao.util.SignUtils;

import java.util.*;

public final class ApiTemplate {

    private static final List<String> list= Arrays.asList("GET","POST");

    private String accessKey;
    private String secretKey;

    public ApiTemplate(String accessKey, String secretKey) {
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
        hashMap.put("secretKey", SignUtils.getSign(secretKey));
        return hashMap;
    }

    public String sendRequest(String url,String method){
       return getResponse(url,method,null,null);
    }

    public String sendRequest(String url,String method,Map<String,Object> paramMap){
        return getResponse(url,method,paramMap,null);
    }

    public String sendRequest(String url,String method,Map<String,Object> paramMap,Map<String,String> headerMap){
        return getResponse(url,method,paramMap,headerMap);
    }

    private String getResponse(String url,String method,Map<String,Object> paramMap,Map<String,String> headerMap){
        if(!list.contains(method)){
            throw new RuntimeException("没有该请求方法 405");
        }
       if("GET".equals(method)){
           HttpResponse httpResponse = HttpRequest.get(url)
                   .addHeaders(getHeaderMap())
                   .addHeaders(headerMap)
                   .form(paramMap)
                   .execute();
           String result = httpResponse.body();
           return result;
       }
       if("POST".equals(method)){
           HttpResponse httpResponse = HttpRequest.post(url)
                   .addHeaders(getHeaderMap())
                   .addHeaders(headerMap)
                   .form(paramMap)
                   .execute();
           String result = httpResponse.body();
           return result;
       }
       return null;
    }

}
