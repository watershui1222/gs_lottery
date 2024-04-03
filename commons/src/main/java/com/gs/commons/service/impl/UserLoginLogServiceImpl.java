package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.UserLoginLog;
import com.gs.commons.service.UserLoginLogService;
import com.gs.commons.mapper.UserLoginLogMapper;
import org.springframework.stereotype.Service;

/**
* @author 69000
* @description 针对表【t_user_login_log】的数据库操作Service实现
* @createDate 2024-04-03 17:30:33
*/
@Service
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLog>
    implements UserLoginLogService{

}




