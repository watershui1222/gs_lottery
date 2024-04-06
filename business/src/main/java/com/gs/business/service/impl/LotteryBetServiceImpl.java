package com.gs.business.service.impl;
import java.util.Date;

import cn.hutool.core.util.NumberUtil;
import com.gs.business.service.LotteryBetService;
import com.gs.commons.entity.LotteryOrder;
import com.gs.commons.entity.TransactionRecord;
import com.gs.commons.entity.UserInfo;
import com.gs.commons.service.LotteryOrderService;
import com.gs.commons.service.TransactionRecordService;
import com.gs.commons.service.UserInfoService;
import com.gs.commons.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LotteryBetServiceImpl implements LotteryBetService {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private TransactionRecordService transactionRecordService;

    @Autowired
    private LotteryOrderService lotteryOrderService;

    @Transactional
    @Override
    public void bet(UserInfo info, BigDecimal amount, List<LotteryOrder> orders) throws Exception {
        // 扣除用户金额
        userInfoService.updateUserBalance(info.getUserName(), amount.negate());
        Date now = new Date();
        String businessOrder = "";
        for (LotteryOrder order : orders) {
            businessOrder += order.getOrderNo() + ",";
        }
        businessOrder = businessOrder.substring(0, businessOrder.length() - 1);
        // 添加流水记录
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setTrxId(IdUtils.getTransactionOrderNo());
        transactionRecord.setUserName(info.getUserName());
        transactionRecord.setAmount(amount);
        transactionRecord.setBeforeAmount(info.getBalance());
        transactionRecord.setAfterAmount(NumberUtil.sub(info.getBalance(), amount));
        transactionRecord.setPayType(1);
        transactionRecord.setBusinessType(2);
        transactionRecord.setBusinessOrder(businessOrder);
        transactionRecord.setCreateTime(now);
        transactionRecord.setRemark("彩票下注");
        transactionRecord.setOperName(null);
        transactionRecordService.save(transactionRecord);
        // 添加投注记录
        lotteryOrderService.saveBatch(orders);
    }
}
