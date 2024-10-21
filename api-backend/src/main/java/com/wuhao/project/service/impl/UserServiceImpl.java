package com.wuhao.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.project.model.request.user.UserRegisterRequest;
import com.wuhao.project.model.response.FirstTokenResponse;
import com.wuhao.project.model.entity.User;
import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.mapper.UserMapper;
import com.wuhao.project.model.enmus.UserRoleEnum;
import com.wuhao.project.model.request.user.UserQueryRequest;
import com.wuhao.project.service.UserService;
import com.wuhao.project.util.IdUtils;
import com.wuhao.project.util.JwtUtil;
import com.wuhao.project.util.RegexUtils;
import com.wuhao.project.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.Calendar;
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
    private UserService userService;
    @Override
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        // 判断参数是否为空
        String userName=userRegisterRequest.getUserName();
        if(StringUtils.isEmpty(userName)){
            userName="user_"+ RandomUtil.randomNumbers(6);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String email = userRegisterRequest.getEmail();
        //检查密码参数是否为空
        if(StringUtils.isAllEmpty(userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度小于8");
        }
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        }
        //密码参数校验
//        if(RegexUtils.isPasswordInvalid(userPassword) && RegexUtils.isPasswordInvalid(userPassword)){
//            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码含有特殊字符");
//        }
        if(StringUtils.isAllEmpty(userAccount,email)){
           throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //查询数据库中是否含有该账号
        synchronized (userAccount.intern()) {
            //账号注册
            if(!StringUtils.isEmpty(userAccount)){
                //账号参数校验
                if (userAccount.length() < 4) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度小于4");
                }
                if(RegexUtils.isAccountError(userAccount)){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号含有特殊字符");
                }
                // 账户不能重复
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("userAccount", userAccount);
                User dataUser = this.baseMapper.selectOne(queryWrapper);
                if (dataUser !=null) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
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
                this.save(user);
                return 0l;
            }else if(!StringUtils.isEmpty(email)){
                //邮箱注册
                if (true){
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
                    this.save(user);
                    return 0l;
                }
            }
            return 0l;
        }
    }

    @Override
    public FirstTokenResponse userLogin(String userAccount, String userPassword, String email) {
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
        String token = JwtUtil.getToken(String.valueOf(user.getId()));
        firstTokenResponse.setUserId(user.getId());
        firstTokenResponse.setToken(token);
        redisTemplate.opsForValue().set("api:user:token:"+user.getId(),token,1, TimeUnit.DAYS);
        return firstTokenResponse;
    }

    @Override
    public User getLoginUser() {
        String userId = UserUtil.getUserId();
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
        User loginUser = userService.getLoginUser();
        if(loginUser == null){
            return true;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
//        if (userQueryRequest == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
//        }
//        Long id = userQueryRequest.getId();
//        String unionId = userQueryRequest.getUnionId();
//        String mpOpenId = userQueryRequest.getMpOpenId();
//        String userName = userQueryRequest.getUserName();
//        String userProfile = userQueryRequest.getUserProfile();
//        String userRole = userQueryRequest.getUserRole();
//        String sortField = userQueryRequest.getSortField();
//        String sortOrder = userQueryRequest.getSortOrder();
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq(id != null, "id", id);
//        queryWrapper.eq(StringUtils.isNotBlank(unionId), "unionId", unionId);
//        queryWrapper.eq(StringUtils.isNotBlank(mpOpenId), "mpOpenId", mpOpenId);
//        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
//        queryWrapper.like(StringUtils.isNotBlank(userProfile), "userProfile", userProfile);
//        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
//        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(PostConstant.SORT_ORDER_ASC),
//                sortField);
//        return queryWrapper;
        return null;
    }

    @Override
    public User getAccessKeyUser(String accessKsy) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("accessKey",accessKsy);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }
}




