package com.gs.commons.mapper;

import com.gs.commons.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
* @author 69000
* @description 针对表【t_user_info】的数据库操作Mapper
* @createDate 2024-04-03 17:34:50
* @Entity com.gs.commons.entity.UserInfo
*/
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 修改用户余额
     * @param userName 用户名
     * @param balance 金额,扣除传负数
     * @return
     */
    int updateUserBalance(@Param("userName") String userName, @Param("balance") BigDecimal balance);
}




