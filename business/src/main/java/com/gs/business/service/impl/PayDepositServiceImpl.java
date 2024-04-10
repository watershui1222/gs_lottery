package com.gs.business.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gs.business.service.PayDepositService;
import com.gs.commons.entity.Deposit;
import com.gs.commons.entity.PayOrder;
import com.gs.commons.entity.TransactionRecord;
import com.gs.commons.entity.UserInfo;
import com.gs.commons.excption.BusinessException;
import com.gs.commons.service.DepositService;
import com.gs.commons.service.PayOrderService;
import com.gs.commons.service.TransactionRecordService;
import com.gs.commons.service.UserInfoService;
import com.gs.commons.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PayDepositServiceImpl implements PayDepositService {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private DepositService depositService;

    @Autowired
    private TransactionRecordService transactionRecordService;

    @Autowired
    private PayOrderService payOrderService;

    @Transactional
    @Override
    public void deposit(PayOrder order) {
        UserInfo user = userInfoService.getUserByName(order.getUserName());
        // 给用户加钱
        userInfoService.updateUserBalance(order.getUserName(), order.getAmount());
        // 添加充值记录
        Deposit deposit = new Deposit();
        deposit.setUserName(order.getUserName());
        deposit.setOrderNo(IdUtils.getDepositOrderNo());
        deposit.setAmount(order.getAmount());
        deposit.setDepositType(5);
        deposit.setCreateTime(new Date());
        deposit.setCheckTime(new Date());
        deposit.setUpdateTime(new Date());
        deposit.setOperName(null);
        deposit.setRemark("在线充值:[" + order.getOrderNo() +"]");
        deposit.setAccountDetail(order.getMerchantName() + "|" + order.getChannelName());
        deposit.setDepositDetail(null);
        deposit.setStatus(1);
        depositService.save(deposit);
        // 添加流水记录
        TransactionRecord record = new TransactionRecord();
        record.setUserName(order.getUserName());
        record.setTrxId(IdUtils.getTransactionOrderNo());
        record.setAmount(order.getAmount());
        record.setBeforeAmount(user.getBalance());
        record.setAfterAmount(NumberUtil.add(order.getAmount(), user.getBalance()));
        record.setPayType(0);
        record.setBusinessType(0);
        record.setBusinessOrder(deposit.getOrderNo());
        record.setCreateTime(new Date());
        record.setRemark("在线充值");
        record.setOperName(null);
        transactionRecordService.save(record);
        // 修改三方订单状态
        boolean update = payOrderService.update(
                new LambdaUpdateWrapper<PayOrder>()
                        .set(PayOrder::getRemark, order.getRemark())
                        .set(PayOrder::getStatus, 1)
                        .eq(PayOrder::getId, order.getId())
                        .eq(PayOrder::getStatus, 0)
        );
        if (!update) {
            throw new BusinessException("修改订单状态失败");
        }
    }
}
