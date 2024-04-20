package com.gs.paycallback.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson2.JSONObject;
import com.gs.business.utils.plat.SignUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Slf4j
@Api(value = "三方平台回调相关", tags = "三方平台回调相关")
@RequestMapping("/platCallback")
@RestController
public class PlatCallbackController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${platform.WE.publicKey}")
    public String wePublicKey;
    @Value("${platform.WE.privateKey}")
    public String wePrivateKey;

    @ApiOperation(value = "we回调")
    @PostMapping("/we")
    public JSONObject we(HttpServletRequest httpServletRequest) throws Exception {
        String body = ServletUtil.getBody(httpServletRequest);
        JSONObject bodyObj = JSONObject.parseObject(body);
        log.info(bodyObj.toJSONString());
        Long timestamp = bodyObj.getLong("timestamp");
        String accessToken = bodyObj.getString("accessToken");
        String username = bodyObj.getString("username");
        String cmd = bodyObj.getString("RegisterOrLoginReq");
        Integer eventType = bodyObj.getInteger("eventType");
        Integer channelId = bodyObj.getInteger("channelId");
        String password = bodyObj.getString("password");
        String ip = bodyObj.getString("ip");
        String signature = bodyObj.getString("signature");

        JSONObject res = new JSONObject();
        //验签
        boolean isOK = SignUtils.weVerify(signature, timestamp + accessToken, this.wePrivateKey, this.wePublicKey);
        if(isOK){
            res.put("accessToken", accessToken);
            res.put("subChannelId", 0);
            res.put("username", username);
            res.put("status", 200);
        }
        return res;
    }

    public static void main(String[] args) {
        int amount = 1000;
        BigDecimal div = NumberUtil.div(String.valueOf(amount), "100");
        System.out.println(div);
    }


}
