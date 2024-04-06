package com.gs.business.client;

import com.gs.business.service.PlatService;
import com.gs.commons.entity.EduOrder;
import com.gs.commons.entity.UserPlat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PlatClient {
    @Autowired
    private PlatService kyService;
    /**
     * 注册
     * @param platCode
     * @param userName
     * @return
     * @throws Exception
     */
    public UserPlat register(String platCode, String userName) throws Exception {
        if (StringUtils.equals(platCode, "ky")) {
            return kyService.register(userName);
        }
        return null;
    }


    /**
     * 查询余额
     * @param userPlat
     * @return
     * @throws Exception
     */
    public BigDecimal queryBalance(UserPlat userPlat) throws Exception {
        BigDecimal balance = new BigDecimal(0);
        if (StringUtils.equals(userPlat.getPlatCode(), "ky")) {
            balance =  kyService.queryBalance(userPlat);
        }
        return balance;
    }

    /**
     * 获取登录游戏URL
     * @param userPlat
     * @return
     * @throws Exception
     */
    public String getLoginUrl(UserPlat userPlat) throws Exception {
        if (StringUtils.equals(userPlat.getPlatCode(), "ky")) {
            return kyService.getLoginUrl(userPlat);
        }
        return null;
    }

    /**
     * 生成各个厂商额度转入订单号
     * @param platCode
     * @param amount
     * @param userPlat
     * @return
     * @throws Exception
     */
    public String getDepositOrderNo(String platCode, BigDecimal amount, UserPlat userPlat) throws Exception {
        if (StringUtils.equals(platCode, "ky")) {
            return kyService.getDepositOrderNo(amount, userPlat);
        }
        return null;
    }

    /**
     * 生成各个厂商额度转出订单号
     * @param platCode
     * @param amount
     * @param userPlat
     * @return
     * @throws Exception
     */
    public String getWithdrawOrderNo(String platCode, BigDecimal amount, UserPlat userPlat) throws Exception {
        if (StringUtils.equals(platCode, "ky")) {
            return kyService.getWithdrawOrderNo(amount, userPlat);
        }
        return null;
    }

    /**
     * 额度转入
     * @param userPlat
     * @param amount
     * @return
     * @throws Exception
     */
    public boolean deposit(UserPlat userPlat, BigDecimal amount, EduOrder eduOrder) throws Exception {
        boolean success = false;
        if (StringUtils.equals(userPlat.getPlatCode(), "ky")) {
            success = kyService.deposit(amount, userPlat, eduOrder.getPlatOrderNo());
        }
        return success;
    }


    /**
     * 额度转出
     * @param userPlat
     * @param amount
     * @param eduOrder
     * @return
     * @throws Exception
     */
    public boolean withdraw(UserPlat userPlat, BigDecimal amount, EduOrder eduOrder) throws Exception {
        // 调用三方额度转入接口
        boolean success = false;
        if (StringUtils.equals(userPlat.getPlatCode(), "ky")) {
            success = kyService.withdraw(amount, userPlat, eduOrder.getPlatOrderNo());
        }
        return success;
    }
}
