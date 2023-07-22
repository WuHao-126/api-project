package com.wuhao;



import cn.hutool.json.ObjectMapper;
import com.google.gson.Gson;
import com.wuhao.client.ApiClient;
import com.wuhao.common.Vo.LoginUserVO;
import com.wuhao.service.UserService;
import okhttp3.*;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;



class InterfaceApplicationTests {

    @Autowired
    ApiClient apiClient;
    @Autowired
    UserService userService;
    public static OkHttpClient client = new OkHttpClient();
    public static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");
    @Test
    void contextLoads() throws Exception {
        JSONObject jsonObject=new JSONObject();
        String url="http://localhost:8102/interface/randomname";
        String s = doPost(url, jsonObject.toString());
        System.out.println(s);

    }
    public static String doPost(String url, String param) throws IOException {
        RequestBody body = RequestBody.create(param, MEDIA_TYPE);
        Headers headers=Headers.of();
        Request request = new Request.Builder()
                .headers(headers)
                .url(url)
                 .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    @Test
    void bbb(){
        byte a=1;
        byte b=2;
        char c=6;
        c+=a;
        System.out.println(c);
    }
    


}
