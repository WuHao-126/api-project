package com.wuhao.factory;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.wuhao.util.SignUtils;

import java.util.HashMap;
import java.util.Map;

public class RequestMethodFactory {
    private static final String GATEWAY_HOST = "http://localhost:8090";

//    private Map<String, String> getHeaderMap(String userJson) {
//        Map<String, String> hashMap = new HashMap<>();
//        hashMap.put("accessKey", accessKey);
//        //获得随机数防止重放
//        hashMap.put("nonce", RandomUtil.randomNumbers(4));
//        hashMap.put("body", userJson);
//        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
//        hashMap.put("sign", SignUtils.getSign(userJson, secretKey));
//        return hashMap;
//    }
//
//    public String doGet(String url, Map<String,Object> map){
//        String userJson = JSONUtil.toJsonStr(sdkUser);
//        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/interface/randomname/")
//                .addHeaders(getHeaderMap(userJson))
//                .body(userJson)
//                .execute();
//        String result = httpResponse.body();
//        return result;
//    }
}
