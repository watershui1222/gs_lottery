package com.gs.api.controller;

import cn.hutool.core.util.RandomUtil;
import com.gs.api.utils.JwtUtils;
import com.gs.business.client.PlatClient;
import com.gs.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Slf4j
@Api(value = "三方平台相关", tags = "三方平台相关")
@RequestMapping("/plat")
@RestController
public class PlatController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private PlatClient platClient;


    @ApiOperation(value = "获取三方平台余额")
    @GetMapping("/getBalancec/{platCode}")
    public R getBalancec(@PathVariable("platCode") String platCode, HttpServletRequest httpServletRequest) throws Exception {
        String userName = JwtUtils.getUserName(httpServletRequest);
        BigDecimal amount = platClient.queryBalance(platCode, userName);
        return R.ok().put("balance", amount);
    }

    @ApiOperation(value = "获取三方平台登录URL")
    @GetMapping("/login/{platCode}")
    public R login(@PathVariable("platCode") String platCode, HttpServletRequest httpServletRequest) throws Exception {
        String userName = JwtUtils.getUserName(httpServletRequest);
        String loginUrl = platClient.getLoginUrl(platCode, userName);
        return R.ok().put("loginUrl", loginUrl);
    }
}
