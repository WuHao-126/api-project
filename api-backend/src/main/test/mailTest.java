import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.wuhao.client.ApiTemplate;
import com.wuhao.project.MainrApplication;
import com.wuhao.project.util.IdUtils;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import springfox.documentation.spring.web.json.Json;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
//@SpringBootTest(classes = MainrApplication.class)
public class mailTest {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ApiTemplate apiTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void RedisTest(){
        Map<String, String> map=new HashMap<>();
        map.put("666","666");
        map.put("777","777");
        stringRedisTemplate.opsForStream().add("www",map);
    }

    @Test
    public void mailTest(){
        SimpleMailMessage smm = new SimpleMailMessage();
        String emailContent="";
        smm.setFrom("1345498749@qq.com");//发送者
        smm.setTo("1345498749@qq.com");//收件人
        smm.setSubject("Hello Word");//邮件主题
        smm.setText(emailContent);//邮件内容
        javaMailSender.send(smm);//发送邮件
    }
    @Test
    public void sdkTest(){
        Map<String, Object> map=new HashMap<>();
        map.put("xing","吴");
        String get = apiTemplate.sendRequest("http://101.126.87.57:8090/interface/xing", "GET", map);
        System.out.println(get);
    }

    @Test
    public void idTest(){
        String s = HttpUtil.get("http://localhost:8102/interface/randomname");
        System.out.println(s);
    }
    @Test
    public void test(){
        Map<String,Object> map=new HashMap<>();
        map.put("name","wuhao");
        map.put("age","age");
        String s = JSONUtil.toJsonStr(map);
        // 构建POST请求
        HttpRequest request = HttpRequest.post("http://localhost:8102/interface/aaa").body(s);

        // 发送请求并获取响应
        HttpResponse response = request.execute();

        // 处理响应
        if (response.isOk()) {
            String responseBody = response.body();
            System.out.println("Response: " + responseBody);
        } else {
            System.out.println("Request failed with status code: " + response.getStatus());
        }
    }
    @Test
    public void testJsonData(){
        Integer a=-128;
        Integer b=-128;
        System.out.println(a==b);
    }
    @Data
    class Student{
        private String name;
        private String age;
        private String sex;
        private Child child;

    }
    @Data
    class Child{
        private String cname;
        private String csex;
    }
}
