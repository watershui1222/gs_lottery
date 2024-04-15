package com.gs.business.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.gs.business.service.PayService;
import com.gs.business.utils.pay.MkpayUtil;
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
@Service("hxfPayService")
public class HxfPayServiceImpl implements PayService {
    @Override
    public String getPayUrl(PayMerchant merchant, PayOrder order, PayChannel payChannel) {
        String key = merchant.getMerchantKey();
        Long merchantId = Long.valueOf(merchant.getMerchantId());

        Map<String, Object> treeMap = new TreeMap<>();

        treeMap.put("mchId", merchantId);
        treeMap.put("productId", Integer.valueOf(payChannel.getChannelCode()));
        treeMap.put("mchOrderNo", order.getOrderNo());
        treeMap.put("amount", NumberUtil.mul(order.getAmount(), 100).intValue());
        treeMap.put("notifyUrl", merchant.getCallbackUrl());
        treeMap.put("subject", "网络购物");
        treeMap.put("body", "网络购物");
        treeMap.put("extra", "1");

        String stringSignTemp = StringUtils.join(ObUtil.sortData(treeMap, "&"), "&key=", key);

        String sign = SecureUtil.md5(stringSignTemp).toUpperCase();
        treeMap.put("sign", sign);
        HttpRequest request = HttpUtil.createPost(merchant.getPayUrl() + "/api/pay/create_order");
        request.contentType("application/x-www-form-urlencoded");
        request.form(treeMap);
        HttpResponse response = request.execute();
        log.info("hxf充值响应:{}", response.body());
        JSONObject responseObj = JSON.parseObject(response.body());
        if ("SUCCESS".equals(responseObj.getString("retCode"))) {
            JSONObject payParams = responseObj.getJSONObject("payParams");
            // 设置三方订单号
            order.setPayOrderNo(order.getOrderNo());
            return payParams.getString("payUrl");
        } else {
            String errmsg = responseObj.getString("retMsg");
            throw new BusinessException(errmsg);
        }
    }
}
