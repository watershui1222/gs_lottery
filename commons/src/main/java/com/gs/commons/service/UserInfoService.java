package com.gs.commons.service;

import com.gs.commons.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 69000
* @description 针对表【t_user_info】的数据库操作Service
* @createDate 2024-04-03 17:34:50
*/
public interface UserInfoService extends IService<UserInfo> {

    UserInfo getUserByName(String userName);
}
