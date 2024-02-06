import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.wuhao.project.MainrApplication;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: TODO
 */
//@SpringBootTest(classes = MainrApplication.class)
public class mailTest {
    @Autowired
    private JavaMailSender javaMailSender;
    @Test
    public void mailTest(){
        SimpleMailMessage smm = new SimpleMailMessage();
        String emailContent="<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Document</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"email-box\">\n" +
                "       <h1>API接口开放平台</h1>\n" +
                "       <hr>\n" +
                "       <div class=\"a\">\n" +
                "            <p>尊敬的客户您好！</p>\n" +
                "        <div>\n" +
                "            <p>AccessKey:"+123+"</p>\n" +
                "            <p>SecretKey:"+123+"</p>\n" +
                "        </div>\n" +
                "        <div>\n" +
                "            <p>请谨慎保管，切勿泄露他人</p>\n" +
                "       </div>\n" +
                "       \n" +
                "       </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "<style>\n" +
                "    *{\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "    }\n" +
                "    .a{\n" +
                "        margin-top: 20px;\n" +
                "    }\n" +
                "    .email-box{\n" +
                "        width: 600px;\n" +
                "        height: 330px;\n" +
                "        margin: 100px auto;\n" +
                "        color: #666666;\n" +
                "        text-align: center;\n" +
                "    }\n" +
                "</style>\n" +
                "</html>";
        smm.setFrom("1345498749@qq.com");//发送者
        smm.setTo("1345498749@qq.com");//收件人
        smm.setSubject("Hello Word");//邮件主题
        smm.setText(emailContent);//邮件内容
        javaMailSender.send(smm);//发送邮件
    }

    @Test
    public void idTest(){
        long id = IdWorker.getId();
        System.out.println(id);
    }
    @Test
    public void testJsonData(){
        Child child=new Child();
        child.cname="666";
        child.csex="777";
        Student student=new Student();
        student.name="aaaa";
        student.age="bbbb";
        student.child=child;
        Student student1=new Student();
        student1.name="nvsdf";
        student1.age="asd";
        student1.sex="aswd";
        student1.child=child;
        List<Student> list=new ArrayList<>();
        list.add(student);
        list.add(student1);
        String s = JSONUtil.toJsonStr(list);
        System.out.println(s);
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
