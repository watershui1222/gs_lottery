package com.gs.business.service;

import com.gs.commons.entity.PayOrder;

/**
 * 支付上分
 */
public interface PayDepositService {

    void deposit(PayOrder order);
}
