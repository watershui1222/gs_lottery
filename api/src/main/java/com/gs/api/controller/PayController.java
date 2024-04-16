package com.gs.api.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.api.utils.JwtUtils;
import com.gs.business.client.PayClient;
import com.gs.business.pojo.dto.PayUrlRequest;
import com.gs.commons.entity.PayChannel;
import com.gs.commons.entity.PayMerchant;
import com.gs.commons.entity.PayOrder;
import com.gs.commons.entity.UserInfo;
import com.gs.commons.service.PayChannelService;
import com.gs.commons.service.PayMerchantService;
import com.gs.commons.service.PayOrderService;
import com.gs.commons.service.UserInfoService;
import com.gs.commons.utils.IdUtils;
import com.gs.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(value = "三方支付相关", tags = "三方支付相关")
@RequestMapping("/pay")
@RestController
public class PayController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayMerchantService payMerchantService;

    @Autowired
    private PayChannelService payChannelService;

    @Autowired
    private PayClient payClient;

    @ApiOperation(value = "获取三方支付通道")
    @GetMapping("/getPayChannel")
    public R getPayChannel(HttpServletRequest httpServletRequest) throws Exception {
        List<PayChannel> list = payChannelService.list(
                new LambdaQueryWrapper<PayChannel>()
                        .eq(PayChannel::getStatus, 0)
                        .orderByDesc(PayChannel::getPxh)
        );
        JSONArray jsonArray = new JSONArray();
        for (PayChannel payChannel : list) {
            JSONObject obj = new JSONObject();
            obj.put("id", payChannel.getId());
            obj.put("channelName", payChannel.getChannelName());
            obj.put("minAmount", payChannel.getMinAmount());
            obj.put("maxAmount", payChannel.getMaxAmount());
            obj.put("remark", payChannel.getRemark());
            jsonArray.add(obj);
        }
        return R.ok().put("list", jsonArray);
    }


    @ApiOperation(value = "获取三方支付URL")
    @GetMapping("/getPayUrl")
    public R getPayUrl(@Validated PayUrlRequest request, HttpServletRequest httpServletRequest) throws Exception {
        String userName = JwtUtils.getUserName(httpServletRequest);

        // 获取用户信息
        UserInfo user = userInfoService.getUserByName(userName);

        // 获取支付通道
        PayChannel payChannel = payChannelService.getById(request.getChannelId());
        if (payChannel == null || payChannel.getStatus().intValue() == 1) {
            return R.error("通道维护中...");
        }
        // 获取商户信息
        PayMerchant payMerchant = payMerchantService.getOne(
                new LambdaQueryWrapper<PayMerchant>()
                        .eq(PayMerchant::getStatus, 0)
                        .eq(PayMerchant::getMerchantCode, payChannel.getMerchantCode())
        );
        if (payMerchant == null || payMerchant.getStatus().intValue() == 1) {
            return R.error("通道维护中...");
        }
        BigDecimal amount = new BigDecimal(request.getAmount());
        if (amount.doubleValue() < payChannel.getMinAmount().doubleValue() || amount.doubleValue() > payChannel.getMaxAmount().doubleValue()) {
            return R.error(StrUtil.format("通道支持金额:{}-{}", payChannel.getMinAmount(), payChannel.getMaxAmount()));
        }

        String clientIP = ServletUtil.getClientIPByHeader(httpServletRequest, "x-original-forwarded-for");

        // 组装订单信息
        Date now = new Date();
        PayOrder payOrder = new PayOrder();
        payOrder.setOrderNo(IdUtils.getPayOrderNo());
        payOrder.setAmount(amount);
        payOrder.setUserName(userName);
        payOrder.setOrderNo(IdUtils.getPayOrderNo());
        payOrder.setCreateTime(now);
        payOrder.setUpdateTime(now);
        payOrder.setStatus(0);
        payOrder.setRemark(null);
        payOrder.setErrorMsg(null);
        payOrder.setMerchantCode(payMerchant.getMerchantCode());
        payOrder.setChannelCode(payChannel.getChannelCode());
        payOrder.setMerchantName(payMerchant.getMerchantName());
        payOrder.setChannelName(payChannel.getChannelName());
        payOrder.setUserIp(clientIP);
        payOrder.setIpDetail(clientIP);
        // 调用获取URL接口
        String url = payClient.getUrl(payMerchant, payOrder, payChannel);
        if (StringUtils.isNotBlank(url)) {
            payOrderService.save(payOrder);
            return R.ok().put("url", url);
        }
        return R.error();
    }
}
