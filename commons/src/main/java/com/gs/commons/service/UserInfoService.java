package com.gs.commons.service;

import com.gs.commons.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
* @author 69000
* @description 针对表【t_user_info】的数据库操作Service
* @createDate 2024-04-03 17:34:50
*/
public interface UserInfoService extends IService<UserInfo> {

    UserInfo getUserByName(String userName);

    /**
     * 修改用户余额
     * @param userName 用户名
     * @param balance 金额,扣除传负数
     * @return
     */
    void updateUserBalance(@Param("userName") String userName, @Param("balance") BigDecimal balance) throws Exception;
}
