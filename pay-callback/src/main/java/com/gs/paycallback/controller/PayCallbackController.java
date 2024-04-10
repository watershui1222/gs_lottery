package com.gs.paycallback.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.business.service.PayDepositService;
import com.gs.business.utils.pay.MkpayUtil;
import com.gs.business.utils.pay.ObUtil;
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
import java.math.BigDecimal;
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
            if (!StringUtils.equals(stringObjectEntry.getKey(), "sign")) {
                treeMap.put(stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }
        }
        String sortData = ObUtil.sortData(treeMap, "&");
        String stringSignTemp = StringUtils.join(sortData, "&", key);
        String checkSign = SecureUtil.md5(stringSignTemp).toUpperCase();
        log.info("ob签名data:{}", JSON.toJSONString(treeMap));
        log.info("ob签名字符串:{}", stringSignTemp);
        log.info("ob签名:{}  ---  验签:{}", sign, checkSign);

        if (!StringUtils.equals(sign, checkSign)) {
            return "check sign error";
        }



        // 给用户加钱
        payDepositService.deposit(payOrder);
        return "success";
    }




    @ApiOperation(value = "OK回调")
    @PostMapping("/ok")
    public String ok(HttpServletRequest httpServletRequest) throws Exception {
        String body = ServletUtil.getBody(httpServletRequest);
        JSONObject bodyObj = JSONObject.parseObject(body);
        String outTradeNo = bodyObj.getString("orderid");
        String sign = bodyObj.getString("sign");
        String retsign = bodyObj.getString("retsign");
        String status = bodyObj.getString("state");

        if (!StringUtils.equals(status, "4")) {
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

        // 校验加密规则
        String stringSignTemp = StringUtils.join(sign, key);
        String checkSign = SecureUtil.md5(stringSignTemp);
        log.info("ok签名data:{}", JSON.toJSONString(bodyObj));
        log.info("ok签名字符串:{}", stringSignTemp);
        log.info("ok签名:{}  ---  验签:{}", retsign, checkSign);

        if (!StringUtils.equals(retsign, checkSign)) {
            return "check sign error";
        }



        // 给用户加钱
        payDepositService.deposit(payOrder);
        return "success";
    }


    @ApiOperation(value = "TO回调")
    @PostMapping("/to")
    public String to(HttpServletRequest httpServletRequest) throws Exception {
        String body = ServletUtil.getBody(httpServletRequest);
        JSONObject bodyObj = JSONObject.parseObject(body);
        String outTradeNo = bodyObj.getString("orderid");
        String sign = bodyObj.getString("sign");
        String retsign = bodyObj.getString("retsign");
        String status = bodyObj.getString("state");

        if (!StringUtils.equals(status, "4")) {
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

        // 校验加密规则
        String stringSignTemp = StringUtils.join(sign, key);
        String checkSign = SecureUtil.md5(stringSignTemp);
        log.info("to签名data:{}", JSON.toJSONString(bodyObj));
        log.info("to签名字符串:{}", stringSignTemp);
        log.info("to签名:{}  ---  验签:{}", retsign, checkSign);

        if (!StringUtils.equals(retsign, checkSign)) {
            return "check sign error";
        }



        // 给用户加钱
        payDepositService.deposit(payOrder);
        return "success";
    }



    @ApiOperation(value = "cb回调")
    @PostMapping("/cb")
    public String cb(HttpServletRequest httpServletRequest) throws Exception {
        Map<String, String> body = ServletUtil.getParamMap(httpServletRequest);

        String merchantId = body.get("userCode");
        String outTradeNo = body.get("orderCode");
        String amount = body.get("amount");
        String status = body.get("status");
        String sign = body.get("sign");

        if (!StringUtils.equals(status, "3")) {
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

        // 校验加密规则
        String stringSignTemp = StringUtils.join(outTradeNo
                , "&", amount
                , "&", merchantId
                , "&", status, "&", key);
        String checkSign = SecureUtil.md5(stringSignTemp).toUpperCase();

        log.info("CB签名data:{}", JSON.toJSONString(body));
        log.info("CB签名字符串:{}", stringSignTemp);
        log.info("CB签名:{}  ---  验签:{}", sign, checkSign);

        if (!StringUtils.equals(sign, checkSign)) {
            return "check sign error";
        }

        // 给用户加钱
        payDepositService.deposit(payOrder);
        return "success";
    }



    @ApiOperation(value = "JD回调")
    @PostMapping("/jd")
    public String jd(HttpServletRequest httpServletRequest) throws Exception {
        Map<String, String> body = ServletUtil.getParamMap(httpServletRequest);

        String merchantId = body.get("userCode");
        String outTradeNo = body.get("orderCode");
        String amount = body.get("amount");
        String status = body.get("status");
        String sign = body.get("sign");

        if (!StringUtils.equals(status, "3")) {
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

        // 校验加密规则
        String stringSignTemp = StringUtils.join(outTradeNo
                , "&", amount
                , "&", merchantId
                , "&", status
                , "&", key);
        String checkSign = SecureUtil.md5(stringSignTemp).toUpperCase();

        log.info("JD签名data:{}", JSON.toJSONString(body));
        log.info("JD签名字符串:{}", stringSignTemp);
        log.info("JD签名:{}  ---  验签:{}", sign, checkSign);

        if (!StringUtils.equals(sign, checkSign)) {
            return "check sign error";
        }

        // 给用户加钱
        payDepositService.deposit(payOrder);
        return "success";
    }


    @ApiOperation(value = "KD回调")
    @PostMapping("/kd")
    public String kd(HttpServletRequest httpServletRequest) throws Exception {
        Map<String, String> body = ServletUtil.getParamMap(httpServletRequest);

        String merchantId = body.get("userCode");
        String outTradeNo = body.get("orderCode");
        String status = body.get("status");
        String amount = body.get("amount");
        String sign = body.get("sign");

        if (!StringUtils.equals(status, "3")) {
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

        // 校验加密规则
        String stringSignTemp = StringUtils.join(outTradeNo
                , "&", amount
                , "&", merchantId
                , "&", status
                , "&", key);
        String checkSign = SecureUtil.md5(stringSignTemp).toUpperCase();

        log.info("KD签名data:{}", JSON.toJSONString(body));
        log.info("KD签名字符串:{}", stringSignTemp);
        log.info("KD签名:{}  ---  验签:{}", sign, checkSign);

        if (!StringUtils.equals(sign, checkSign)) {
            return "check sign error";
        }

        // 给用户加钱
        payDepositService.deposit(payOrder);
        return "success";
    }



    @ApiOperation(value = "MK回调")
    @PostMapping("/mk")
    public String mk(HttpServletRequest httpServletRequest) throws Exception {
        String body = ServletUtil.getBody(httpServletRequest);
        JSONObject jsonObject = JSON.parseObject(body);
        String merchantId = jsonObject.getString("mchKey");
        String outTradeNo = jsonObject.getString("mchOrderNo");
        String nonce = jsonObject.getString("nonce");
        String status = jsonObject.getString("payStatus");
        Integer amount = jsonObject.getIntValue("amount");
        Integer realAmount = jsonObject.getIntValue("realAmount");
        Long timestamp = jsonObject.getLong("timestamp");
        String sign = jsonObject.getString("sign");


//        if (!StringUtils.equals(status, "SUCCESS")) {
//            return "error";
//        }


        PayOrder payOrder = payOrderService.getOne(
                new LambdaQueryWrapper<PayOrder>()
                        .eq(PayOrder::getOrderNo, outTradeNo)
                        .eq(PayOrder::getStatus, 0)
        );
        if (null == payOrder) {
            return "error";
        }


        BigDecimal realPayAmount = (null != realAmount && realAmount != 0) ? NumberUtil.div(String.valueOf(realAmount), "100") : payOrder.getAmount();

        // 查询商户
        PayMerchant payMerchant = payMerchantService.getOne(new LambdaQueryWrapper<PayMerchant>().eq(PayMerchant::getMerchantCode, payOrder.getMerchantCode()));
        String merchantDetail = payMerchant.getMerchantDetail();
        JSONObject object = JSON.parseObject(merchantDetail);
        String key = object.getString("key");

        // 校验加密规则

        Map<String, Object> treeMap = new TreeMap<>();
        for (Map.Entry<String, Object> stringObjectEntry : jsonObject.entrySet()) {
            if (!StringUtils.equals(stringObjectEntry.getKey(), "sign")) {
                treeMap.put(stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }
        }
        String needSignParamString = MkpayUtil.getNeedSignParamString(treeMap, key);
        String checkSign = SecureUtil.md5(needSignParamString);

        log.info("MK签名data:{}", JSON.toJSONString(treeMap));
        log.info("MK签名字符串:{}", needSignParamString);
        log.info("MK签名:{}  ---  验签:{}", sign, checkSign);

        if (!StringUtils.equals(sign, checkSign)) {
            return "check sign error";
        }

        // 给用户加钱
        payOrder.setAmount(realPayAmount);
        payOrder.setRemark(StrUtil.format("拉单金额:{},支付金额:{}", amount, realPayAmount));
        payDepositService.deposit(payOrder);
        return "SUCCESS";
    }

    public static void main(String[] args) {
        int amount = 1000;
        BigDecimal div = NumberUtil.div(String.valueOf(amount), "100");
        System.out.println(div);
    }


}
