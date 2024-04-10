package com.gs.business.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.gs.business.service.PayService;
import com.gs.commons.entity.PayChannel;
import com.gs.commons.entity.PayMerchant;
import com.gs.commons.entity.PayOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service("okPayService")
public class OkPayServiceImpl implements PayService {
    @Override
    public String getPayUrl(PayMerchant merchant, PayOrder order, PayChannel payChannel) {
        String merchantDetail = merchant.getMerchantDetail();
        JSONObject object = JSON.parseObject(merchantDetail);
        String key = object.getString("key");
        String merchantId = object.getString("merchantId");

        Map<String, Object> treeMap = new TreeMap<>();
        treeMap.put("recvid", merchantId);
        treeMap.put("orderid", order.getOrderNo());
        treeMap.put("amount", order.getAmount());
        treeMap.put("notifyurl", merchant.getCallbackUrl());

        String stringSignTemp = treeMap.get("recvid").toString() + treeMap.get("orderid").toString() + treeMap.get("amount").toString() + key;
        String sign = SecureUtil.md5(stringSignTemp);
        treeMap.put("sign", sign);
        HttpRequest request = HttpUtil.createPost(merchant.getPayUrl() + "/createpay");
        request.header("Content-Type", "application/json; charset=utf-8");
        request.body(JSON.toJSONString(treeMap));
        HttpResponse response = request.execute();
        log.info("充值响应:{}", response.body());
        JSONObject responseObj = JSON.parseObject(response.body());
        if (1 == responseObj.getIntValue("code")) {
            String dataString = responseObj.getString("data");
            if (StringUtils.isNotEmpty(dataString)) {
                // 设置三方订单号
                JSONObject dataJSON = JSON.parseObject(dataString);
                String tradeNo = dataJSON.getString("orderid");
                order.setPayOrderNo(tradeNo);
                return dataJSON.getString("navurl");
            }

        }
        return null;
    }
}
