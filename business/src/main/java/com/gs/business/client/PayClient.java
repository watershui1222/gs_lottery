package com.gs.business.client;

import com.gs.business.service.PayService;
import com.gs.commons.entity.PayMerchant;
import com.gs.commons.entity.PayOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayClient {
    @Autowired
    private PayService obPayService;

    /**
     * 获取支付URL
     * @param merchant
     * @param order
     * @return
     * @throws Exception
     */
    public String getUrl(PayMerchant merchant, PayOrder order) throws Exception {
        if (StringUtils.equals(merchant.getMerchantCode(), "OB")) {
            return obPayService.getPayUrl(merchant, order);
        }
        return null;
    }
}
