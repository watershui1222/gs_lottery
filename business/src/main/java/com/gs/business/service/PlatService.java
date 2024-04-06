package com.gs.business.service;

import com.gs.commons.entity.UserPlat;
import com.gs.commons.utils.R;

import java.math.BigDecimal;

/**
 * 三方平台三方接口
 */
public interface PlatService {

    /**
     * 注册平台
     * @param userName
     */
    UserPlat registerBalance(String userName) throws Exception;

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
}
