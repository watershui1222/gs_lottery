package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.UserInfo;
import com.gs.commons.service.UserInfoService;
import com.gs.commons.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 69000
* @description 针对表【t_user_info】的数据库操作Service实现
* @createDate 2024-04-03 17:34:50
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo getUserByName(String userName) {
        return getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUserName, userName));
    }

}




