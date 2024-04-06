package com.gs.business.service;

import com.gs.commons.entity.EduOrder;

import java.math.BigDecimal;

/**
 * 三方额度操作相关接口
 */
public interface EduService {

    /**
     *
     * @throws Exception
     */
    EduOrder saveOrderAndSubAmount(String userName, BigDecimal amount, String platCode, String platOrderId) throws Exception;

    void AddMoneyAndTranscationRecord(String userName, BigDecimal amount, String platCode, String platOrderNo, String eduOrderNo) throws Exception;
}
