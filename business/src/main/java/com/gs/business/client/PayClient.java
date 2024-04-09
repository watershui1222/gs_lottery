package com.gs.business.client;

import com.gs.business.service.PayService;
import com.gs.commons.entity.PayChannel;
import com.gs.commons.entity.PayMerchant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PayClient {
    @Autowired
    private PayService obPayService;

    /**
     * 获取支付URL
     * @param orderNo
     * @param merchant
     * @param channel
     * @return
     * @throws Exception
     */
    public String getUrl(String orderNo, BigDecimal amount, PayMerchant merchant, PayChannel channel) throws Exception {
        if (StringUtils.equals(merchant.getMerchantCode(), "OB")) {
            return obPayService.getPayUrl(orderNo, amount, merchant, channel);
        }
        return null;
    }

    /**
     * 获取支付URL
     * @param merchant
     * @return
     * @throws Exception
     */
    public String genOrderNo(PayMerchant merchant) throws Exception {
        if (StringUtils.equals(merchant.getMerchantCode(), "OB")) {
            return obPayService.genPayOrderNo();
        }
        return null;
    }
}
