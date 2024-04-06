package com.gs.api.controller;

import cn.hutool.core.util.RandomUtil;
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

@Slf4j
@Api(value = "三方平台相关", tags = "三方平台相关")
@RequestMapping("/plat")
@RestController
public class PlatController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation(value = "获取三方平台余额")
    @GetMapping("/getBalancec/{platCode}")
    public R getAllPlay(@PathVariable("platCode") String platCode) {
        return R.ok().put("balance", RandomUtil.randomInt(1, 10000));
    }
}
