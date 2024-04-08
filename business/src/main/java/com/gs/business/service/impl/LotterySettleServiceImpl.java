package com.gs.business.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gs.business.service.LotterySettleService;
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
import java.util.Date;

@Service
public class LotterySettleServiceImpl implements LotterySettleService {

    @Autowired
    private LotteryOrderService lotteryOrderService;
    @Autowired
    private TransactionRecordService transactionRecordService;
    @Autowired
    private UserInfoService userInfoService;

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void settle(LotteryOrder order) throws BusinessException {
        Date now = new Date();
        if (order.getOrderStatus().intValue() == 1) {
            // 中奖
            BigDecimal bounsAmount = NumberUtil.mul(order.getOdds(), order.getBetAmount());
            BigDecimal profitAmount = NumberUtil.sub(bounsAmount, order.getBetAmount());
            // 查询用户信息
            UserInfo user = userInfoService.getUserByName(order.getUserName());
            // 修改订单状态
            lotteryOrderService.update(
                    new LambdaUpdateWrapper<LotteryOrder>()
                            .set(LotteryOrder::getBonusAmount, bounsAmount)
                            .set(LotteryOrder::getProfitAmount, profitAmount)
                            .set(LotteryOrder::getSettleTime, now)
                            .set(LotteryOrder::getSettleStatus, 2)
                            .set(LotteryOrder::getOrderStatus, 1)
                            .set(LotteryOrder::getOpenResult, order.getOpenResult())
                            .set(LotteryOrder::getUpdateTime, now)
                            .eq(LotteryOrder::getId, order.getId())
                            .eq(LotteryOrder::getSettleStatus, 0)
                            .eq(LotteryOrder::getOrderStatus, 0));

            // 给用户加钱
            userInfoService.updateUserBalance(order.getUserName(), bounsAmount);
            // 添加流水记录
            TransactionRecord transactionRecord = new TransactionRecord();
            transactionRecord.setUserName(order.getUserName());
            transactionRecord.setTrxId(IdUtils.getTransactionOrderNo());
            transactionRecord.setAmount(bounsAmount);
            transactionRecord.setBeforeAmount(user.getBalance());
            transactionRecord.setAfterAmount(NumberUtil.add(user.getBalance(), bounsAmount));
            transactionRecord.setPayType(0);
            transactionRecord.setBusinessType(3);
            transactionRecord.setBusinessOrder(order.getOrderNo());
            transactionRecord.setCreateTime(now);
            transactionRecord.setRemark("彩票奖金,订单号[" + order.getOrderNo() + "]");
            transactionRecord.setOperName(null);
            transactionRecordService.save(transactionRecord);

        } else if (order.getOrderStatus().intValue() == 2) {
            // 未中奖
            BigDecimal profitAmount = order.getBetAmount().negate();
            lotteryOrderService.update(
                    new LambdaUpdateWrapper<LotteryOrder>()
                            .set(LotteryOrder::getProfitAmount, profitAmount)
                            .set(LotteryOrder::getSettleTime, now)
                            .set(LotteryOrder::getSettleStatus, 2)
                            .set(LotteryOrder::getOrderStatus, 2)
                            .set(LotteryOrder::getOpenResult, order.getOpenResult())
                            .set(LotteryOrder::getUpdateTime, now)
                            .eq(LotteryOrder::getId, order.getId())
                            .eq(LotteryOrder::getSettleStatus, 0)
                            .eq(LotteryOrder::getOrderStatus, 0)
            );
        } else if (order.getOrderStatus().intValue() == 4) {
            // 和局 退还本金
            // 查询用户信息
            UserInfo user = userInfoService.getUserByName(order.getUserName());
            // 修改订单状态
            lotteryOrderService.update(
                    new LambdaUpdateWrapper<LotteryOrder>()
                            .set(LotteryOrder::getSettleTime, now)
                            .set(LotteryOrder::getSettleStatus, 2)
                            .set(LotteryOrder::getOrderStatus, 4)
                            .set(LotteryOrder::getOpenResult, order.getOpenResult())
                            .set(LotteryOrder::getUpdateTime, now)
                            .eq(LotteryOrder::getId, order.getId())
                            .eq(LotteryOrder::getSettleStatus, 0)
                            .eq(LotteryOrder::getOrderStatus, 0));

            // 给用户加钱
            userInfoService.updateUserBalance(order.getUserName(), order.getBetAmount());
            // 添加流水记录
            TransactionRecord transactionRecord = new TransactionRecord();
            transactionRecord.setUserName(order.getUserName());
            transactionRecord.setTrxId(IdUtils.getTransactionOrderNo());
            transactionRecord.setAmount(order.getBetAmount());
            transactionRecord.setBeforeAmount(user.getBalance());
            transactionRecord.setAfterAmount(NumberUtil.add(user.getBalance(), order.getBetAmount()));
            transactionRecord.setPayType(0);
            transactionRecord.setBusinessType(11);
            transactionRecord.setBusinessOrder(order.getOrderNo());
            transactionRecord.setCreateTime(now);
            transactionRecord.setRemark("彩票和局退还,订单号[" + order.getOrderNo() + "]");
            transactionRecord.setOperName(null);
            transactionRecordService.save(transactionRecord);
        }
    }
}
