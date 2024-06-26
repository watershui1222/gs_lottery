package com.gs.business.service.impl.pay;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.gs.business.service.PayService;
import com.gs.business.utils.pay.ObUtil;
import com.gs.commons.entity.PayChannel;
import com.gs.commons.entity.PayMerchant;
import com.gs.commons.entity.PayOrder;
import com.gs.commons.excption.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service("cbPayService")
public class CBPayServiceImpl implements PayService {
    @Override
    public String getPayUrl(PayMerchant merchant, PayOrder order, PayChannel payChannel) {
        String key = merchant.getMerchantKey();
        Long merchantId = Long.valueOf(merchant.getMerchantId());

        Map<String, Object> treeMap = new TreeMap<>();
        treeMap.put("userCode", merchantId);
        treeMap.put("orderCode", order.getOrderNo());
        treeMap.put("amount", order.getAmount());
        treeMap.put("payType", "3");
        treeMap.put("callbackUrl", merchant.getCallbackUrl());

        String stringSignTemp = StringUtils.join(treeMap.get("orderCode")
                , "&", treeMap.get("amount")
                , "&", treeMap.get("payType")
                , "&", treeMap.get("userCode"), "&", key);
        String sign = SecureUtil.md5(stringSignTemp).toUpperCase();
        treeMap.put("sign", sign);
        HttpRequest request = HttpUtil.createPost(merchant.getPayUrl() + "/system/api/pay");
        request.contentType("application/x-www-form-urlencoded");
        request.form(treeMap);
        HttpResponse response = request.execute();
        log.info("CB充值响应:{}", response.body());
        JSONObject responseObj = JSON.parseObject(response.body());
        if (200 == responseObj.getIntValue("code")) {
            // 设置三方订单号
            String tradeNo = responseObj.getJSONObject("data").getString("orderNo");
            order.setPayOrderNo(tradeNo);
            return responseObj.getJSONObject("data").getString("url");
        } else {
            String errmsg = responseObj.getString("message");
            throw new BusinessException(errmsg);
        }
    }
}
