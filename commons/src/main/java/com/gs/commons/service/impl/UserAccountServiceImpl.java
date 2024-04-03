package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.UserAccount;
import com.gs.commons.service.UserAccountService;
import com.gs.commons.mapper.UserAccountMapper;
import org.springframework.stereotype.Service;

/**
* @author 69000
* @description 针对表【t_user_account】的数据库操作Service实现
* @createDate 2024-04-03 17:30:24
*/
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount>
    implements UserAccountService{

}




