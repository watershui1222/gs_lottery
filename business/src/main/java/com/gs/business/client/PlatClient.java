package com.gs.business.client;

import com.gs.business.pojo.bo.PlatLoginUrlBO;
import com.gs.business.service.PlatService;
import com.gs.commons.entity.EduOrder;
import com.gs.commons.entity.UserPlat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Component
public class PlatClient {
    @Resource(name = "kyServiceImpl")
    private PlatService kyService;

    @Resource(name = "lyServiceImpl")
    private PlatService lyService;

    @Resource(name = "hgServiceImpl")
    private PlatService hgService;

    @Resource(name = "bbinServiceImpl")
    private PlatService bbinService;

    @Resource(name = "agServiceImpl")
    private PlatService agService;

    @Resource(name = "sbServiceImpl")
    private PlatService sbService;
    /**
     * 注册
     * @param platCode
     * @param userName
     * @return
     * @throws Exception
     */
    public UserPlat register(String platCode, String userName) throws Exception {
        if (StringUtils.equals(platCode, "KY")) {
            return kyService.register(userName);
        }
        if (StringUtils.equals(platCode, "LY")) {
            return lyService.register(userName);
        }
        if (StringUtils.equals(platCode, "HG")) {
            return hgService.register(userName);
        }
        if (StringUtils.equals(platCode, "BBIN")) {
            return bbinService.register(userName);
        }
        if (StringUtils.equals(platCode, "AG")) {
            return agService.register(userName);
        }
        if (StringUtils.equals(platCode, "SB")) {
            return sbService.register(userName);
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
        if (StringUtils.equals(userPlat.getPlatCode(), "KY")) {
            balance = kyService.queryBalance(userPlat);
        }
        if (StringUtils.equals(userPlat.getPlatCode(), "LY")) {
            balance = lyService.queryBalance(userPlat);
        }
        if (StringUtils.equals(userPlat.getPlatCode(), "HG")) {
            balance = hgService.queryBalance(userPlat);
        }
        if (StringUtils.equals(userPlat.getPlatCode(), "BBIN")) {
            balance = bbinService.queryBalance(userPlat);
        }
        if (StringUtils.equals(userPlat.getPlatCode(), "AG")) {
            balance = agService.queryBalance(userPlat);
        }
        if (StringUtils.equals(userPlat.getPlatCode(), "SB")) {
            balance = sbService.queryBalance(userPlat);
        }
        return balance;
    }

    /**
     * 获取登录游戏URL
     * @param
     * @return
     * @throws Exception
     */
    public String getLoginUrl(PlatLoginUrlBO request) throws Exception {
        if (StringUtils.equals(request.getPlatCode(), "KY")) {
            return kyService.getLoginUrl(request);
        } else if (StringUtils.equals(request.getPlatCode(), "LY")) {
            return lyService.getLoginUrl(request);
        } else if (StringUtils.equals(request.getPlatCode(), "HG")) {
            return hgService.getLoginUrl(request);
        } else if (StringUtils.equals(request.getPlatCode(), "BBIN")) {
            return bbinService.getLoginUrl(request);
        } else if (StringUtils.equals(request.getPlatCode(), "AG")) {
            return agService.getLoginUrl(request);
        } else if (StringUtils.equals(request.getPlatCode(), "SB")) {
            return sbService.getLoginUrl(request);
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
        if (StringUtils.equals(platCode, "KY")) {
            return kyService.getDepositOrderNo(amount, userPlat);
        }
        if (StringUtils.equals(platCode, "LY")) {
            return lyService.getDepositOrderNo(amount, userPlat);
        }
        if (StringUtils.equals(platCode, "HG")) {
            return hgService.getDepositOrderNo(amount, userPlat);
        }
        if (StringUtils.equals(platCode, "BBIN")) {
            return bbinService.getDepositOrderNo(amount, userPlat);
        }
        if (StringUtils.equals(platCode, "AG")) {
            return agService.getDepositOrderNo(amount, userPlat);
        }
        if (StringUtils.equals(platCode, "SB")) {
            return sbService.getDepositOrderNo(amount, userPlat);
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
        if (StringUtils.equals(platCode, "KY")) {
            return kyService.getWithdrawOrderNo(amount, userPlat);
        }
        if (StringUtils.equals(platCode, "LY")) {
            return lyService.getWithdrawOrderNo(amount, userPlat);
        }
        if (StringUtils.equals(platCode, "HG")) {
            return hgService.getWithdrawOrderNo(amount, userPlat);
        }
        if (StringUtils.equals(platCode, "BBIN")) {
            return bbinService.getWithdrawOrderNo(amount, userPlat);
        }
        if (StringUtils.equals(platCode, "AG")) {
            return agService.getWithdrawOrderNo(amount, userPlat);
        }
        if (StringUtils.equals(platCode, "SB")) {
            return sbService.getWithdrawOrderNo(amount, userPlat);
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
        if (StringUtils.equals(userPlat.getPlatCode(), "KY")) {
            success = kyService.deposit(amount, userPlat, eduOrder.getPlatOrderNo());
        }
        if (StringUtils.equals(userPlat.getPlatCode(), "LY")) {
            success = lyService.deposit(amount, userPlat, eduOrder.getPlatOrderNo());
        }
        if (StringUtils.equals(userPlat.getPlatCode(), "HG")) {
            success = hgService.deposit(amount, userPlat, eduOrder.getPlatOrderNo());
        }
        if (StringUtils.equals(userPlat.getPlatCode(), "BBIN")) {
            success = bbinService.deposit(amount, userPlat, eduOrder.getPlatOrderNo());
        }
        if (StringUtils.equals(userPlat.getPlatCode(), "AG")) {
            success = agService.deposit(amount, userPlat, eduOrder.getPlatOrderNo());
        }
        if (StringUtils.equals(userPlat.getPlatCode(), "SB")) {
            success = sbService.deposit(amount, userPlat, eduOrder.getPlatOrderNo());
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
        if (StringUtils.equals(userPlat.getPlatCode(), "KY")) {
            success = kyService.withdraw(amount, userPlat, eduOrder.getPlatOrderNo());
        }
        if (StringUtils.equals(userPlat.getPlatCode(), "LY")) {
            success = lyService.withdraw(amount, userPlat, eduOrder.getPlatOrderNo());
        }
        if (StringUtils.equals(userPlat.getPlatCode(), "HG")) {
            success = hgService.withdraw(amount, userPlat, eduOrder.getPlatOrderNo());
        }
        if (StringUtils.equals(userPlat.getPlatCode(), "BBIN")) {
            success = bbinService.withdraw(amount, userPlat, eduOrder.getPlatOrderNo());
        }
        if (StringUtils.equals(userPlat.getPlatCode(), "AG")) {
            success = agService.withdraw(amount, userPlat, eduOrder.getPlatOrderNo());
        }
        if (StringUtils.equals(userPlat.getPlatCode(), "SB")) {
            success = sbService.withdraw(amount, userPlat, eduOrder.getPlatOrderNo());
        }
        return success;
    }
}
