package com.gs.api.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.api.controller.request.PayUrlRequest;
import com.gs.api.utils.JwtUtils;
import com.gs.business.client.PayClient;
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
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Slf4j
@Api(value = "三方支付回调相关", tags = "三方支付回调相关")
@RequestMapping("/callback")
@RestController
public class PayCallbackController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation(value = "OB回调")
    @GetMapping("/ob")
    public String ob(HttpServletRequest httpServletRequest) throws Exception {
        String body = ServletUtil.getBody(httpServletRequest);
        System.out.println("body:" + body);
        Map<String, String> paramMap = ServletUtil.getParamMap(httpServletRequest);
        System.out.println("param:" + JSON.toJSONString(paramMap));
        return "success";
    }
}
