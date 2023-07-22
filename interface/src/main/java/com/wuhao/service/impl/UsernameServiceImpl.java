package com.wuhao.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuhao.mapper.UsernameMapper;
import com.wuhao.mode.domain.Username;
import com.wuhao.service.UsernameService;
import org.springframework.stereotype.Service;

/**
*
*/
@Service
public class UsernameServiceImpl extends ServiceImpl<UsernameMapper, Username>
implements UsernameService {

}
