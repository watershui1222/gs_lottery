package com.gs.business.service;

import com.gs.commons.entity.PayChannel;
import com.gs.commons.entity.PayMerchant;

import java.math.BigDecimal;

public interface PayService {

    String getPayUrl(String orderNo, BigDecimal amount, PayMerchant merchant, PayChannel channel);

    String genPayOrderNo();
}
