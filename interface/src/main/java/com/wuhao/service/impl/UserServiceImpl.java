package com.wuhao.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.mapper.UserMapper;
import com.wuhao.model.entity.User;
import com.wuhao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


/**
 *
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    private static String SALT="wuhao";


    @Override
    public User userLogin(String userAccount, String userPassword) {
        if(StringUtils.isAllEmpty(userAccount,userPassword)){
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 4) {
            return null;
        }
        //校验账号中是否含有特殊字符
//        if (RegexUtils.isAccountInvalid(userAccount)) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号错误");
//        }
        String md5Password=DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        User loginUser = query()
                .eq("userAccount", userAccount)
                .eq("userPassword", md5Password).one();
        if(loginUser==null){
            return null;
        }
        //记录登录态
        return loginUser;
    }


}




