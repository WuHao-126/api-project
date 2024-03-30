package com.wuhao.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.project.model.request.user.UserRegisterRequest;
import com.wuhao.project.model.response.LoginUserResponse;
import com.wuhao.project.model.entity.User;
import com.wuhao.project.common.ErrorCode;
import com.wuhao.project.exception.BusinessException;
import com.wuhao.project.mapper.UserMapper;
import com.wuhao.project.model.enmus.UserRoleEnum;
import com.wuhao.project.model.request.user.UserQueryRequest;
import com.wuhao.project.service.UserService;
import com.wuhao.project.util.IdUtils;
import com.wuhao.project.util.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.Calendar;

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
                if(RegexUtils.isAccountInvalid(userAccount)){
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
                user.setUserAvatar("default.jpg");
                this.save(user);
                return user.getId();
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
                    return user.getId();
                }
            }
            return 0l;
        }
    }

    @Override
    public LoginUserResponse userLogin(String userAccount, String userPassword,String email,HttpServletRequest servletRequest) {
        if(StringUtils.isAllEmpty(userAccount,email)){
            throw new BusinessException(ErrorCode.PARAMS_NULL,"参数为空");
        }
        if (userPassword.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码有误");
        }
        String md5Password=DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        User loginUser=null;
        if (!StringUtils.isEmpty(userAccount)) {
            if(userAccount.length() < 4 && RegexUtils.isAccountInvalid(userAccount)){
                 throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号错误");
            }
            loginUser = query()
                    .eq("userAccount", userAccount)
                    .eq("userPassword", md5Password).one();
        }else{
            loginUser = query()
                    .eq("email", email)
                    .eq("userPassword", md5Password).one();
        }
        if(loginUser==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        LoginUserResponse LoginUserResponse = getLoginUserVO(loginUser);
        //记录登录态
        servletRequest.getSession().setAttribute(USER_LOGIN_STATE,loginUser);
        return LoginUserResponse;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) attribute;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
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
    public boolean userLogout(HttpServletRequest servletRequest) {
        if (servletRequest.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }
        // 移除登录态
        servletRequest.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }


    @Override
    public Boolean updateUser(User user, User loginUser) {
        Long id = user.getId();
        if(id<=0){
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

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public Boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public Boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }


    @Override
    public LoginUserResponse getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserResponse loginUserResponse = new LoginUserResponse();
        BeanUtils.copyProperties(user, loginUserResponse);
        return loginUserResponse;
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




