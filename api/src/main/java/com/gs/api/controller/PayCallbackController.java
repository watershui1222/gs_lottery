package com.gs.api.controller;

import cn.hutool.extra.servlet.ServletUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Api(value = "三方支付回调相关", tags = "三方支付回调相关")
@RequestMapping("/callback")
@RestController
public class PayCallbackController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation(value = "OB回调")
    @PostMapping("/ob")
    public String ob(HttpServletRequest httpServletRequest) throws Exception {
        String body = ServletUtil.getBody(httpServletRequest);
        return "success";
    }
}
