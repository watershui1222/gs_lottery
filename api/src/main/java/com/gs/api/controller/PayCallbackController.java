package com.gs.api.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.business.service.PayDepositService;
import com.gs.business.utils.pay.ObUtil;
import com.gs.commons.entity.PayChannel;
import com.gs.commons.entity.PayMerchant;
import com.gs.commons.entity.PayOrder;
import com.gs.commons.service.PayChannelService;
import com.gs.commons.service.PayMerchantService;
import com.gs.commons.service.PayOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Api(value = "三方支付回调相关", tags = "三方支付回调相关")
@RequestMapping("/callback")
@RestController
public class PayCallbackController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayDepositService payDepositService;

    @Autowired
    private PayChannelService payChannelService;

    @Autowired
    private PayMerchantService payMerchantService;

    @ApiOperation(value = "OB回调")
    @PostMapping("/ob")
    public String ob(HttpServletRequest httpServletRequest) throws Exception {
        String body = ServletUtil.getBody(httpServletRequest);
        JSONObject bodyObj = JSONObject.parseObject(body);
        Long amount = bodyObj.getLong("amount");
        String currency = bodyObj.getString("currency");
        String extend = bodyObj.getString("extend");
        String merchantNo = bodyObj.getString("merchantNo");
        String outTradeNo = bodyObj.getString("outTradeNo");
        String sign = bodyObj.getString("sign");
        Long status = bodyObj.getLong("status");
        String tradeNo = bodyObj.getString("tradeNo");

        if (status.intValue() != 1) {
            return "error";
        }


        PayOrder payOrder = payOrderService.getOne(
                new LambdaQueryWrapper<PayOrder>()
                        .eq(PayOrder::getOrderNo, outTradeNo)
                        .eq(PayOrder::getStatus, 0)
        );
        if (null == payOrder) {
            return "error";
        }


        // 查询商户
        PayMerchant payMerchant = payMerchantService.getOne(new LambdaQueryWrapper<PayMerchant>().eq(PayMerchant::getMerchantCode, payOrder.getMerchantCode()));
        String merchantDetail = payMerchant.getMerchantDetail();
        JSONObject object = JSON.parseObject(merchantDetail);
        String key = object.getString("key");
        Long merchantId = object.getLong("merchantId");

        // 校验加密规则
        Map<String, Object> treeMap = new TreeMap<>();
        for (Map.Entry<String, Object> stringObjectEntry : bodyObj.entrySet()) {
            treeMap.put(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        String sortData = ObUtil.sortData(treeMap, "&");
        String stringSignTemp = StringUtils.join(sortData, "&", key);
        String checkSign = SecureUtil.md5(stringSignTemp).toUpperCase();
        log.info("签名data:{}", JSON.toJSONString(treeMap));
        log.info("签名字符串:{}", stringSignTemp);
        log.info("签名:{}  ---  验签:{}", sign, checkSign);

        if (!StringUtils.equals(sign, checkSign)) {
            return "check sign error";
        }



        // 给用户加钱
        payDepositService.deposit(payOrder);
        return "success";
    }
}
