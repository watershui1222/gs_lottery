package com.gs.business.service.impl;
import java.util.Date;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gs.business.service.LotteryBetService;
import com.gs.commons.entity.LotteryOrder;
import com.gs.commons.entity.TransactionRecord;
import com.gs.commons.entity.UserInfo;
import com.gs.commons.excption.BusinessException;
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

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void bet(UserInfo info, BigDecimal amount, List<LotteryOrder> orders) throws BusinessException {
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

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void cancel(LotteryOrder lotteryOrder) throws BusinessException {
        Date now = new Date();
        // 查询用户信息
        UserInfo user = userInfoService.getUserByName(lotteryOrder.getUserName());
        // 修改订单状态
        lotteryOrderService.update(
                new LambdaUpdateWrapper<LotteryOrder>()
                        .set(LotteryOrder::getSettleTime, now)
                        .set(LotteryOrder::getSettleStatus, 3)
                        .set(LotteryOrder::getOrderStatus, 3)
                        .set(LotteryOrder::getUpdateTime, now)
                        .eq(LotteryOrder::getUserName, lotteryOrder.getUserName())
                        .eq(LotteryOrder::getOrderNo, lotteryOrder.getOrderNo())
                        .eq(LotteryOrder::getSettleStatus, 0)
                        .eq(LotteryOrder::getOrderStatus, 0)
        );
        // 给用户加钱
        userInfoService.updateUserBalance(user.getUserName(), lotteryOrder.getBetAmount());
        // 添加流水记录
        TransactionRecord record = new TransactionRecord();
        record.setUserName(lotteryOrder.getUserName());
        record.setTrxId(IdUtils.getTransactionOrderNo());
        record.setAmount(lotteryOrder.getBetAmount());
        record.setBeforeAmount(user.getBalance());
        record.setAfterAmount(NumberUtil.add(user.getBalance(), lotteryOrder.getBetAmount()));
        record.setPayType(0);
        record.setBusinessType(4);
        record.setBusinessOrder(lotteryOrder.getOrderNo());
        record.setCreateTime(now);
        record.setRemark("彩票撤单,订单号[" + lotteryOrder.getOrderNo() +"]");
        record.setOperName(null);
        transactionRecordService.save(record);
    }
}
