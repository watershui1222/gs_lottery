package com.gs.business.service;

import com.gs.commons.entity.PayMerchant;
import com.gs.commons.entity.PayOrder;

public interface PayService {

    String getPayUrl(PayMerchant merchant, PayOrder order);

    String genPayOrderNo();
}
