package com.gs.business.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gs.business.service.EduService;
import com.gs.commons.entity.EduOrder;
import com.gs.commons.entity.TransactionRecord;
import com.gs.commons.entity.UserInfo;
import com.gs.commons.service.EduOrderService;
import com.gs.commons.service.TransactionRecordService;
import com.gs.commons.service.UserInfoService;
import com.gs.commons.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class EduServiceImpl implements EduService {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private TransactionRecordService transactionRecordService;

    @Autowired
    private EduOrderService eduOrderService;

    @Transactional
    @Override
    public EduOrder saveOrderAndSubAmount(String userName, BigDecimal amount, String platCode, String platOrderId) throws Exception {
        // 查询用户信息
        UserInfo userInfo = userInfoService.getUserByName(userName);
        // 扣除用户金额
        userInfoService.updateUserBalance(userName, amount.negate());

        Date now = new Date();
        String eduOrderNo = IdUtils.getPlatInOrderNo();

        // 添加流水记录
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setTrxId(IdUtils.getTransactionOrderNo());
        transactionRecord.setUserName(userName);
        transactionRecord.setAmount(amount.negate());
        transactionRecord.setBeforeAmount(userInfo.getBalance());
        transactionRecord.setAfterAmount(NumberUtil.sub(userInfo.getBalance(), amount));
        transactionRecord.setPayType(1);
        transactionRecord.setBusinessType(5);
        transactionRecord.setBusinessOrder(eduOrderNo);
        transactionRecord.setCreateTime(now);
        transactionRecord.setRemark("额度转入至[" + platCode + "]" + amount + "元");
        transactionRecord.setOperName(null);
        transactionRecordService.save(transactionRecord);

        // 添加转换记录
        EduOrder eduOrder = new EduOrder();
        eduOrder.setUserName(userName);
        eduOrder.setOrderNo(eduOrderNo);
        eduOrder.setPlatOrderNo(platOrderId);
        eduOrder.setAmount(amount.negate());
        eduOrder.setEduType(0);
        eduOrder.setPlatCode(platCode);
        eduOrder.setStatus(-1);
        eduOrder.setCreateTime(now);
        eduOrder.setUpdateTime(now);
        eduOrder.setRemark("额度转入至[" + platCode + "]" + amount + "元");
        eduOrderService.save(eduOrder);
        return eduOrder;
    }

    @Override
    public void AddMoneyAndTranscationRecord(String userName, BigDecimal amount, String platCode, String platOrderNo, String eduOrderNo) throws Exception {
        // 查询用户信息
        UserInfo userInfo = userInfoService.getUserByName(userName);
        // 给用户加钱
        userInfoService.updateUserBalance(userName, amount);
        // 添加流水记录
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setTrxId(IdUtils.getTransactionOrderNo());
        transactionRecord.setUserName(userName);
        transactionRecord.setAmount(amount);
        transactionRecord.setBeforeAmount(userInfo.getBalance());
        transactionRecord.setAfterAmount(NumberUtil.add(userInfo.getBalance(), amount));
        transactionRecord.setPayType(0);
        transactionRecord.setBusinessType(6);
        transactionRecord.setBusinessOrder(platOrderNo);
        transactionRecord.setCreateTime(new Date());
        transactionRecord.setRemark("[" + platCode + "]额度转出至平台:" + amount + "元");
        transactionRecord.setOperName(null);
        transactionRecordService.save(transactionRecord);
        // 调用三方成功,修改额度订单记录
        eduOrderService.update(
                new LambdaUpdateWrapper<EduOrder>()
                        .eq(EduOrder::getOrderNo, eduOrderNo)
                        .set(EduOrder::getStatus, 0)
                        .set(EduOrder::getUpdateTime, new Date())
        );
    }
}
