package com.gs.business.client;

import com.gs.business.pojo.bo.PlatLoginUrlBO;
import com.gs.business.service.PlatService;
import com.gs.business.utils.ReflectionUtil;
import com.gs.commons.entity.EduOrder;
import com.gs.commons.entity.UserPlat;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Component
public class PlatClient {
    @Resource
    private ApplicationContext applicationContext;

    /**
     * 注册
     * @param platCode
     * @param userName
     * @return
     * @throws Exception
     */
    public UserPlat register(String platCode, String userName) throws Exception {
        Class platImpl = ReflectionUtil.getPlatImplByPlatCode(platCode);
        PlatService platService = (PlatService) applicationContext.getBean(platImpl);
        return platService.register(userName);
    }


    /**
     * 查询余额
     * @param userPlat
     * @return
     * @throws Exception
     */
    public BigDecimal queryBalance(UserPlat userPlat) throws Exception {
        Class platImpl = ReflectionUtil.getPlatImplByPlatCode(userPlat.getPlatCode());
        PlatService platService = (PlatService) applicationContext.getBean(platImpl);
        return platService.queryBalance(userPlat);
    }

    /**
     * 获取登录游戏URL
     * @param
     * @return
     * @throws Exception
     */
    public String getLoginUrl(PlatLoginUrlBO request) throws Exception {
        Class platImpl = ReflectionUtil.getPlatImplByPlatCode(request.getPlatCode());
        PlatService platService = (PlatService) applicationContext.getBean(platImpl);
        return platService.getLoginUrl(request);
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
        Class platImpl = ReflectionUtil.getPlatImplByPlatCode(platCode);
        PlatService platService = (PlatService) applicationContext.getBean(platImpl);
        return platService.getDepositOrderNo(amount, userPlat);
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
        Class platImpl = ReflectionUtil.getPlatImplByPlatCode(platCode);
        PlatService platService = (PlatService) applicationContext.getBean(platImpl);
        return platService.getWithdrawOrderNo(amount, userPlat);
    }

    /**
     * 额度转入
     * @param userPlat
     * @param amount
     * @return
     * @throws Exception
     */
    public boolean deposit(UserPlat userPlat, BigDecimal amount, EduOrder eduOrder) throws Exception {
        Class platImpl = ReflectionUtil.getPlatImplByPlatCode(userPlat.getPlatCode());
        PlatService platService = (PlatService) applicationContext.getBean(platImpl);
        return platService.deposit(amount, userPlat, eduOrder.getPlatOrderNo());
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
        Class platImpl = ReflectionUtil.getPlatImplByPlatCode(userPlat.getPlatCode());
        PlatService platService = (PlatService) applicationContext.getBean(platImpl);
        return platService.withdraw(amount, userPlat, eduOrder.getPlatOrderNo());
    }
}
