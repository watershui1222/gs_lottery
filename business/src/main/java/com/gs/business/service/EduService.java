package com.gs.business.service;

import com.gs.commons.entity.EduOrder;
import com.gs.commons.excption.BusinessException;

import java.math.BigDecimal;

/**
 * 三方额度操作相关接口
 */
public interface EduService {

    /**
     *
     * @throws Exception
     */
    EduOrder saveOrderAndSubAmount(String userName, BigDecimal amount, String platCode, String platOrderId) throws BusinessException;

    void AddMoneyAndTranscationRecord(String userName, BigDecimal amount, String platCode, String platOrderNo, String eduOrderNo) throws BusinessException;
}
