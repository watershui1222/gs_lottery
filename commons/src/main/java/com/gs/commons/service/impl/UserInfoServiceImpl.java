package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.UserInfo;
import com.gs.commons.service.UserInfoService;
import com.gs.commons.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 69000
* @description 针对表【t_user_info】的数据库操作Service实现
* @createDate 2024-04-03 17:30:27
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

}




