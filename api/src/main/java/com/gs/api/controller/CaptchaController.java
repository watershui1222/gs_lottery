package com.gs.api.controller;


import cn.hutool.captcha.GifCaptcha;
import com.gs.business.pojo.dto.CaptchaRequest;
import com.gs.commons.utils.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "图形验证码")
@RequestMapping("/captcha")
@Slf4j
public class CaptchaController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/getCaptcha")
    public R captcha(@Validated CaptchaRequest request) {
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("1", "register:");
        typeMap.put("2", "login:");

        String business = typeMap.get(request.getType());
        if (StringUtils.isBlank(business)) {
            return R.error();
        }

        // 设置请求头为输出图片类型png
        GifCaptcha specCaptcha = new GifCaptcha(85, 45, 4);
        String key = UUID.randomUUID().toString();
        // 存入redis并设置过期时间为3分钟
        redisTemplate.opsForValue().set(business + key, specCaptcha.getCode(), 3, TimeUnit.MINUTES);

        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("image", specCaptcha.getImageBase64Data());
        return R.ok().put("key", key).put("image", specCaptcha.getImageBase64Data());
    }
}
