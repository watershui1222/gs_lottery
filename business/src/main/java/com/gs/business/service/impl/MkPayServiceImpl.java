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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service("mkPayService")
public class MkPayServiceImpl implements PayService {
    @Override
    public String getPayUrl(PayMerchant merchant, PayOrder order, PayChannel payChannel) {
        String merchantDetail = merchant.getMerchantDetail();
        JSONObject object = JSON.parseObject(merchantDetail);
        String key = object.getString("key");
        String merchantId = object.getString("merchantId");

        Map<String, Object> treeMap = new TreeMap<>();

        treeMap.put("mchKey", merchantId);
        treeMap.put("product", payChannel.getChannelCode());
        treeMap.put("mchOrderNo", order.getOrderNo());
        treeMap.put("amount", NumberUtil.mul(order.getAmount(), 100).intValue());
        treeMap.put("nonce", RandomUtil.randomString(15));
        treeMap.put("timestamp", String.valueOf(DateUtil.date().getTime()));
        treeMap.put("notifyUrl", merchant.getCallbackUrl());


        String stringSignTemp = MkpayUtil.getNeedSignParamString(treeMap, key);
        String sign = SecureUtil.md5(stringSignTemp);
        treeMap.put("sign", sign);
        HttpRequest request = HttpUtil.createPost(merchant.getPayUrl() + "/api/v1/payment/init");
        request.header("Content-Type", "application/json; charset=utf-8");
        request.body(JSON.toJSONString(treeMap));
        HttpResponse response = request.execute();
        log.info("mk充值响应:{}", response.body());
        JSONObject responseObj = JSON.parseObject(response.body());
        if ("200".equals(responseObj.getString("code"))) {
            JSONObject data = responseObj.getJSONObject("data");
            // 设置三方订单号
            String tradeNo = data.getString("serialOrderNo");
            order.setPayOrderNo(tradeNo);
            JSONObject url = data.getJSONObject("url");
            return url.getString("payUrl");
        }
        return null;
    }
}
