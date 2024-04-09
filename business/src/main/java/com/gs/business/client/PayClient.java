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
    @Autowired
    private PayService okPayService;
    @Autowired
    private PayService toPayService;
    @Autowired
    private PayService cbPayService;
    @Autowired
    private PayService jdPayService;
    @Autowired
    private PayService kdPayService;
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
        } else if (StringUtils.equals(merchant.getMerchantCode(), "OK")) {
            return okPayService.getPayUrl(merchant, order);
        } else if (StringUtils.equals(merchant.getMerchantCode(), "TO")) {
            return toPayService.getPayUrl(merchant, order);
        } else if (StringUtils.equals(merchant.getMerchantCode(), "CB")) {
            return cbPayService.getPayUrl(merchant, order);
        } else if (StringUtils.equals(merchant.getMerchantCode(), "JD")) {
            return jdPayService.getPayUrl(merchant, order);
        }  else if (StringUtils.equals(merchant.getMerchantCode(), "KD")) {
            return kdPayService.getPayUrl(merchant, order);
        }
        return null;
    }
}
