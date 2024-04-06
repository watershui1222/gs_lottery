package com.gs.business.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.business.service.PlatService;
import com.gs.commons.entity.UserPlat;
import com.gs.commons.service.UserPlatService;
import com.gs.commons.utils.AesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
public class KyServiceImpl implements PlatService {
    /**
     * 平台标识
     */
    @Value("${platform.owner}")
    public String owner = "gs";
    @Value("${platform.KaiYuan.prefixURL}")
    public String prefixURL = "https://wc1-api.uaphl791.com/channelHandle";//接口地址
    @Value("${platform.KaiYuan.recordURL}")
    public String recordURL = "https://wc1-record.uaphl791.com/getRecordHandle";//拉单接口
    @Value("${platform.KaiYuan.agent}")
    public String agent = "73419";
    @Value("${platform.KaiYuan.aesKey}")
    public String aesKey = "77C3273B538BB6F9";
    @Value("${platform.KaiYuan.md5Key}")
    public String md5Key = "08A455C3E66DDF22";

    @Autowired
    private UserPlatService userPlatService;

    @Override
    public UserPlat registerBalance(String userName) throws Exception {
        // 查询是否注册平台
        UserPlat userPlat = userPlatService.getOne(
                new LambdaQueryWrapper<UserPlat>()
                        .eq(UserPlat::getUserName, userName)
                        .eq(UserPlat::getPlatCode, "ky")
        );
        if (userPlat != null) {
            return userPlat;
        }

        // 注册三方
        String timestamp = String.valueOf(DateUtil.current());
        String account = this.owner + userName;
        String orderid = this.agent + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + account;
        String lineCode = "GS";
        String kindId = "0";
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=0&")
                .append("account=").append(account)
                .append("&money=0")
                .append("&orderid=").append(orderid)
                .append("&ip=127.0.0.1")
                .append("&lineCode=").append(lineCode)
                .append("&KindID=").append(kindId);
        String param = AesUtils.AESEncrypt(paramSb.toString(), aesKey, true);
        String key = AesUtils.MD5(this.agent + timestamp + this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.prefixURL).append("?").append("agent=").append(this.agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        log.info("开元登录返回:{}", result);
        JSONObject resJSON = JSONObject.parseObject(result);
        JSONObject d = resJSON.getJSONObject("d");
        if (d.getIntValue("code") != 0) {
            log.error("开元 获取游戏URL失败 param= " + param + " result = " + result);
            throw new Exception("开元注册失败");
        }

        // 执行注册逻辑
        UserPlat save = new UserPlat();
        save.setUserName(userName);
        save.setPlatCode("ky");
        save.setPlatUserName(account);
        save.setPlatUserPassword(null);
        save.setStatus(0);
        save.setCreateTime(new Date());
        userPlatService.save(save);
        return save;
    }

    @Override
    public BigDecimal queryBalance(UserPlat userPlat) {
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = userPlat.getPlatUserName();
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=1&").append("account=").append(account);
        String param = AesUtils.AESEncrypt(paramSb.toString(), aesKey, true);
        String key = AesUtils.MD5(agent + timestamp + this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.prefixURL).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        log.info("开元查询余额返回:{}", result);
        JSONObject jsonResult = JSONObject.parseObject(result);
        JSONObject d = jsonResult.getJSONObject("d");
        BigDecimal money = BigDecimal.ZERO;
        if (d != null) {
            return d.getBigDecimal("money") == null ? BigDecimal.ZERO : d.getBigDecimal("money");
        }
        log.error("开元 获取用户余额失败 param= " + param + " result = " + result);
        return money;
    }

    @Override
    public String getLoginUrl(UserPlat userPlat) throws Exception {
        // 注册三方
        String timestamp = String.valueOf(DateUtil.current());
        String account = userPlat.getPlatUserName();
        String orderid = this.agent + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + account;
        String lineCode = "GS";
        String kindId = "0";
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=0&")
                .append("account=").append(account)
                .append("&money=0")
                .append("&orderid=").append(orderid)
                .append("&ip=127.0.0.1")
                .append("&lineCode=").append(lineCode)
                .append("&KindID=").append(kindId);
        String param = AesUtils.AESEncrypt(paramSb.toString(), aesKey, true);
        String key = AesUtils.MD5(this.agent + timestamp + this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.prefixURL).append("?").append("agent=").append(this.agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        log.info("开元登录返回:{}", result);
        JSONObject resJSON = JSONObject.parseObject(result);
        JSONObject d = resJSON.getJSONObject("d");
        if (d.getIntValue("code") != 0) {
            log.error("开元 获取游戏URL失败 param= " + param + " result = " + result);
            throw new Exception("开元登录失败");
        }
        return d.getString("url");
    }
}
