package com.gs.business.service.impl;

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
@Service("obPayService")
public class ObPayServiceImpl implements PayService {
    @Override
    public String getPayUrl(PayMerchant merchant, PayOrder order, PayChannel payChannel) {
        String merchantDetail = merchant.getMerchantDetail();
        JSONObject object = JSON.parseObject(merchantDetail);
        String key = object.getString("key");
        Long merchantId = object.getLong("merchantId");

        Map<String, Object> treeMap = new TreeMap<>();
        treeMap.put("merchantNo", merchantId);
        treeMap.put("outTradeNo", order.getOrderNo());
        treeMap.put("amount", NumberUtil.mul(order.getAmount(), 100).intValue());
        treeMap.put("currency", "OB");
        treeMap.put("notifyUrl", merchant.getCallbackUrl());

        String sortData = ObUtil.sortData(treeMap, "&");
        String stringSignTemp = StringUtils.join(sortData, "&", key);
        String sign = SecureUtil.md5(stringSignTemp).toUpperCase();
        treeMap.put("sign", sign);
        log.info("参数加密:{}", JSON.toJSONString(treeMap));
        HttpRequest request = HttpUtil.createPost(merchant.getPayUrl() + "/api/payment/create");
        request.header("Content-Type", "application/json; charset=utf-8");
        request.body(JSON.toJSONString(treeMap));
        HttpResponse response = request.execute();
        log.info("OB充值响应:{}", response.body());
        JSONObject responseObj = JSON.parseObject(response.body());
        if (0 == responseObj.getIntValue("code")) {
            // 设置三方订单号
            String tradeNo = responseObj.getJSONObject("data").getString("tradeNo");
            order.setPayOrderNo(tradeNo);
            return responseObj.getJSONObject("data").getString("payUrl");
        } else {
            String errmsg = responseObj.getString("msg");
            throw new BusinessException(errmsg);
        }
    }
}
