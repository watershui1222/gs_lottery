package com.gs.business.service;

import com.gs.commons.entity.UserPlat;

import java.math.BigDecimal;

/**
 * 三方平台三方接口
 */
public interface PlatService {

    /**
     * 注册平台
     * @param userName
     */
    UserPlat register(String userName) throws Exception;

    /**
     * 获取指定平台余额
     * @param userPlat
     * @return
     */
    BigDecimal queryBalance(UserPlat userPlat);

    /**
     * 登录指定平台
     * @param userPlat
     * @return
     */
    String getLoginUrl(UserPlat userPlat) throws Exception;

    /**
     * 指定平台额度转入
     * @param userPlat
     * @return
     */
    boolean deposit(BigDecimal amount, UserPlat userPlat, String platOrderNo) throws Exception;

    /**
     * 生产转入注单ID
     * @param userPlat
     * @return
     */
    String getDepositOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception;

    /**
     * 指定平台额度转出
     * @param userPlat
     * @return
     */
    boolean withdraw(BigDecimal amount, UserPlat userPlat, String platOrderNo) throws Exception;

    /**
     * 生产转出注单ID
     * @param userPlat
     * @return
     */
    String getWithdrawOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception;

    /**
     * 踢出用户
     * @param userPlat
     * @return
     */
    boolean logout(UserPlat userPlat);
}
