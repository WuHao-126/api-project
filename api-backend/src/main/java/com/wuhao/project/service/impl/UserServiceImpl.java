package com.wuhao.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.project.common.IdRequest;
import com.wuhao.project.common.Result;
import com.wuhao.project.constant.RedisConstant;
import com.wuhao.project.constant.UserConstant;
import com.wuhao.project.exception.ThrowUtils;
import com.wuhao.project.model.request.user.UserLoginRequest;
import com.wuhao.project.model.request.user.UserRegisterRequest;
import com.wuhao.project.model.request.user.UserUpdateMyRequest;
import com.wuhao.project.model.response.FirstTokenResponse;
import com.wuhao.project.model.entity.User;
import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.mapper.UserMapper;
import com.wuhao.project.model.enmus.UserRoleEnum;
import com.wuhao.project.model.request.user.UserQueryRequest;
import com.wuhao.project.model.response.LoginUser;
import com.wuhao.project.service.BlogService;
import com.wuhao.project.service.CommentService;
import com.wuhao.project.service.UserService;
import com.wuhao.project.util.IdUtils;
import com.wuhao.project.util.JwtUtil;
import com.wuhao.project.util.RegexUtils;
import com.wuhao.project.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.wuhao.project.constant.UserConstant.USER_LOGIN_STATE;

/**
 *
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    private static String SALT="wuhao";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;
    @Override
    public void userRegister(UserRegisterRequest userRegisterRequest) {
        if(userRegisterRequest==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        String userAccount=userRegisterRequest.getUserAccount();
        String userPassword=userRegisterRequest.getCheckPassword();
        String checkPassword=userRegisterRequest.getCheckPassword();
        String email = userRegisterRequest.getEmail();
        //判断密码参数是否为空
        if(StringUtils.isAllEmpty(userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断账号或者邮箱是否为全空
        if(StringUtils.isAllEmpty(email,userAccount)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断参数是否为空
        String userName=userRegisterRequest.getUserName();
        if(StringUtils.isEmpty(userName)){
            userName="user_"+ RandomUtil.randomNumbers(6);
        }
        //检查密码参数是否为空
        if(StringUtils.isAllEmpty(userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请输入账号或者邮箱");
        }
        if(RegexUtils.isPasswordError(userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码中含有非法字符");
        }
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        }
        //查询数据库中是否含有该账号
        synchronized (userAccount.intern()) {
            //账号注册
            if(!StringUtils.isEmpty(userAccount)){
                //账号参数校验
                if(RegexUtils.isAccountError(userAccount)){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号含有特殊字符");
                }
                // 账户不能重复
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("userAccount", userAccount);
                User dataUser = this.baseMapper.selectOne(queryWrapper);
                if (dataUser !=null) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "您输入的账号："+userAccount+"已注册");
                }
                // 2. 加密
                String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
                // 3. 插入数据
                User user = new User();
                BeanUtil.copyProperties(userRegisterRequest,user);
                user.setUserName(userName);
                user.setId(IdUtils.getId());
                user.setUserPassword(encryptPassword);
                if(StringUtils.isEmpty(user.getUserAvatar())){
                    user.setUserAvatar("default.jpg");
                }
                userMapper.insert(user);
            }else if(!StringUtils.isEmpty(email)){
                //邮箱注册
                User datauser = this.query().eq("email", email).one();
                if(datauser!=null){
                    throw new BusinessException(ErrorCode.ALREADY_REGISTER,"此邮箱已经注册");
                }
                //TODO 验证码验证
                // 2. 加密
                String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
                // 3. 插入数据
                User user = new User();
                BeanUtil.copyProperties(userRegisterRequest,user);
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month = Calendar.getInstance().get(Calendar.MONTH)-1;
                int day = Calendar.getInstance().get(Calendar.DATE);
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append(year).append(month).append(day).append(RandomUtil.randomNumbers(4));
                user.setUserAccount(stringBuilder.toString());
                user.setUserName(userName);
                user.setId(IdUtils.getId());
                user.setUserPassword(encryptPassword);
                user.setUserAvatar("default.jpg");
                userMapper.insert(user);
            }
        }
    }

    @Override
    public FirstTokenResponse userLogin(UserLoginRequest userLoginRequest) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        String email = userLoginRequest.getEmail();
        //对账号合法性进行判断
        if(RegexUtils.isAccountError(userAccount)){
            throw new BusinessException(ErrorCode.USER_ACCOUNT_ILLEGAL);
        }
        User user = query()
                .eq(StringUtils.isNoneBlank(userAccount),"userAccount", userAccount)
                .or()
                .eq(StringUtils.isNoneBlank(email),"email", email)
                .one();
        if(user == null){
            throw new BusinessException("登录用户: "+userAccount+" 不存在");
        }
        Integer state = user.getState();
        // 1.封号  2.注销
        if (state == 1){
            throw new BusinessException(ErrorCode.USER_STATUS_ONE);
        }else if(state == 2){
            throw new BusinessException(ErrorCode.USER_STATUS_TWO);
        }
        //对密码合法性进行判断
        if (RegexUtils.isPasswordError(userPassword)) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_ILLEGAL);
        }
        String md5Password=DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        if(!md5Password.equals(user.getUserPassword())){
            throw new BusinessException(ErrorCode.USER_PASSWORD_ERROR);
        }
        FirstTokenResponse firstTokenResponse = new FirstTokenResponse();
        //JWT存储的信息
        Map<String,Object> map = new HashMap<>();
        map.put("randomNum",RandomUtil.randomNumbers(6));
        map.put("userAccount",userAccount);
        map.put("userId",user.getId());
        String token = JwtUtil.createToken(map);
        firstTokenResponse.setUserId(user.getId());
        firstTokenResponse.setToken(token);
        RBucket<FirstTokenResponse> bucket = redissonClient.getBucket(RedisConstant.API_USER_TOKEN + user.getId());
        FirstTokenResponse redisUser = bucket.get();
        if(redisUser != null){
            //说明已经有登录的用户，顶下线
            bucket.delete();
        }
        bucket.set(firstTokenResponse,1,TimeUnit.DAYS);
        return firstTokenResponse;
    }

    @Override
    public LoginUser adminLogin(UserLoginRequest userLoginRequest) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        //参数进行校验
        if (RegexUtils.isAccountError(userAccount)) {
            throw new BusinessException(ErrorCode.USER_ACCOUNT_ILLEGAL);
        }
        if (RegexUtils.isPasswordError(userPassword)) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_ILLEGAL);
        }

        String md5Password= DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        User user = userService.query()
                .eq("userAccount", userAccount).one();
        if(user ==null){
            throw new BusinessException(ErrorCode.USER_ACCOUNT_NULL,"您登陆的账号："+userAccount+" 不存在");
        }
        String userRole = user.getUserRole();
        if(UserConstant.SUPER_ADMIN_ROLE.equals(userRole) || UserConstant.ADMIN_ROLE.equals(userRole)){
            String s = user.getUserPassword();
            if(!s.equals(md5Password)){
                throw new BusinessException(ErrorCode.USER_PASSWORD_ERROR);
            }
            LoginUser loginUser = new LoginUser();
            Map<String,Object> map = new HashMap<>();
            map.put("userAccount",userAccount);
            map.put("userId",user.getId());
            String token = JwtUtil.createToken(map);
            loginUser.setUserId(user.getId().toString());
            loginUser.setUsername(user.getUserName());
            loginUser.setToken(token);
            return loginUser;
        }else{
            //记录日志，或进行封号
            log.error("账号：{} 尝试无权限登录", userAccount);
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
    }

    @Override
    @Transactional
    public Boolean updateMyUser(UserUpdateMyRequest userUpdateMyRequest) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser();
        Long id = loginUser.getId();
        Long id1 = userUpdateMyRequest.getId();
        String userRole = loginUser.getUserRole();
        //查看是否是本人操作 或者是超级管理操作
        if(id.equals(id1) || userRole.equals("superadmin")){
            User userAccount = userService.getOne(new QueryWrapper<User>().eq("userAccount", userUpdateMyRequest.getUserAccount()));
            if(!StringUtils.isEmpty(userUpdateMyRequest.getUserPassword())){
                userUpdateMyRequest.setUserPassword(DigestUtils
                        .md5DigestAsHex(("wuhao"+userUpdateMyRequest.getUserPassword()).getBytes()));
            }
            // TODO 有问题  会修改博客创建时间，成为最新的博客
            BeanUtils.copyProperties(userUpdateMyRequest, userAccount);
            //修改用户表的信息
            boolean result = userService.updateById(userAccount);
            //修改博客里面的信息
            blogService.update().set("authorName", userUpdateMyRequest.getUserName())
                    .set("authorAvatar", userUpdateMyRequest.getUserAvatar())
                    .eq("authorId", id).update();
            //修改评论里面的信息
            commentService.update().set("userName",userUpdateMyRequest.getUserName())
                    .set("userAvatar",userUpdateMyRequest.getUserAvatar())
                    .eq("userId",id).update();
            return  result;
        }else{
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
    }

    @Override
    public Boolean deleteUser(IdRequest idRequest) {
        User user = userService.query().eq("id", idRequest.getId()).one();
        if(user==null){
            throw new BusinessException(ErrorCode.USER_IS_NULL);
        }
        //超级管理员账号不可删除
        if(UserConstant.SUPER_ADMIN_ROLE.equals(user.getUserRole())){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        user.setState(idRequest.getOther());
        return userService.updateById(user);
    }

    @Override
    public User getLoginUser() {
        String userId = UserUtil.getUserId();
        log.error("当前用户登录Id为：{}",userId);
        if(StringUtils.isEmpty(userId)){
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_STATUS_ERROR);
        }
        String accessKey = user.getAccessKey();
        String secretKey = user.getSecretKey();
        if(!StringUtils.isAnyBlank(accessKey,secretKey)){
            user.setIsExistKey(true);
        }
        return user;
    }

    @Override
    public boolean userLogout() {
        LoginUser loginUser = UserUtil.getLoginUser();
        if(loginUser == null){
            return true;
        }
        String token = loginUser.getToken();
        redissonClient.getBucket(RedisConstant.API_USER_TOKEN+token).delete();
        // 移除登录态
        return true;
    }


    @Override
    public Boolean updateUser(User user, User loginUser) {
        Long id= user.getId();
        if(false){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(loginUser.getId()!=id || isAdmin(loginUser)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        User oldUser = query().eq("id", id).one();
        if(oldUser==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean flag = updateById(user);
        return flag;
    }



    @Override
    public Boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    @Override
    public User getAccessKeyUser(String accessKsy) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("accessKey",accessKsy);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    @Override
    public Page<User> getUserListPage(UserQueryRequest userQueryRequest) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Date beginDate = userQueryRequest.getBeginDate();
        Date endDate = userQueryRequest.getEndDate();
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(!StringUtils.isBlank(userQueryRequest.getUserRole()),"userRole",userQueryRequest.getUserRole())
                .eq(!StringUtils.isEmpty(userQueryRequest.getUserState()),"state",userQueryRequest.getUserState())
                .between(beginDate!=null && endDate!=null,"createTime",beginDate,endDate)
                .like(!StringUtils.isBlank(userQueryRequest.getKeywords()),"userName",userQueryRequest.getKeywords())
                .or()
                .like(!StringUtils.isBlank(userQueryRequest.getKeywords()),"userAccount",userQueryRequest.getKeywords())
                .or()
                .like(!StringUtils.isEmpty(userQueryRequest.getKeywords()),"email",userQueryRequest.getKeywords())
                .or()
                .like(!StringUtils.isEmpty(userQueryRequest.getKeywords()),"phone",userQueryRequest.getKeywords())
                .orderByAsc("createTime");
        Page<User> userPage = userService.page(new Page<>(current, size),queryWrapper);
        userPage.getRecords().stream().forEach(data ->{
            if(StringUtils.isEmpty(data.getAccessKey())){
                data.setIsExistKey(false);
            }else{
                data.setIsExistKey(true);
            }
        });
        return userPage;
    }

    @Override
    public void applyAsk() {
        User loginUser = userService.getLoginUser();
        Long id = loginUser.getId();
        User user = userService.getById(id);
        if (user == null) {
            throw new RuntimeException("非法申请AKSK,账户：" + loginUser.getUserAccount());
        }
        //用户邮箱
        String email = user.getEmail();
        if (StringUtils.isEmpty(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String secretKey = user.getSecretKey();
        String accessKey = user.getAccessKey();
        String userAccount = user.getUserAccount();
        if (StringUtils.isAllEmpty(secretKey, accessKey)) {
            //分配aksk
            String newAccessKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(5));
            String newSecretKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(8));
            user.setAccessKey(newAccessKey);
            user.setSecretKey(newSecretKey);
            userService.updateById(user);
            String emailContent = "<!DOCTYPE html>\n" +
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
                    "            <p>AccessKey:" + newAccessKey + "</p>\n" +
                    "            <p>SecretKey:" + newSecretKey + "</p>\n" +
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
            SimpleMailMessage smm = new SimpleMailMessage();
            //TODO
            smm.setFrom("1345498749@qq.com");//发送者
            smm.setTo(email);//收件人
            smm.setSubject("公钥密钥申请");//邮件主题
            smm.setText(emailContent);//邮件内容
            javaMailSender.send(smm);//发送邮件
        }
    }

}




