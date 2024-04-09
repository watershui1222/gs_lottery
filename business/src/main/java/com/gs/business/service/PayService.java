package com.gs.business.service;

import com.gs.commons.entity.PayChannel;
import com.gs.commons.entity.PayMerchant;
import com.gs.commons.entity.PayOrder;

import java.math.BigDecimal;

public interface PayService {

    String getPayUrl(PayMerchant merchant, PayOrder order);

    String genPayOrderNo();
}
