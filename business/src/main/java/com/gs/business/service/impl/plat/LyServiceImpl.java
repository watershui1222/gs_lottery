package com.gs.business.service.impl.plat;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.business.pojo.bo.PlatLoginUrlBO;
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
public class LyServiceImpl implements PlatService {
    /**
     * 平台标识
     */
    @Value("${platform.owner}")
    public String owner;
    @Value("${platform.LeYou.apiDomain}")
    public String apiDomain;
    @Value("${platform.LeYou.agent}")
    public String agent;
    @Value("${platform.LeYou.aesKey}")
    public String aesKey;
    @Value("${platform.LeYou.md5Key}")
    public String md5Key;
    @Value("${platform.LeYou.betRecordDomain}")
    public String betRecordDomain;

    @Autowired
    private UserPlatService userPlatService;

    public static String platCode = "LY";

    @Override
    public UserPlat register(String userName) throws Exception {
        // 查询是否注册平台
        UserPlat userPlat = userPlatService.getOne(
                new LambdaQueryWrapper<UserPlat>()
                        .eq(UserPlat::getUserName, userName)
                        .eq(UserPlat::getPlatCode, platCode)
        );
        if (userPlat != null) {
            return userPlat;
        }

        // 注册三方
        String timestamp = String.valueOf(DateUtil.current());
        String account = this.owner + userName;;
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
        String param = AesUtils.AESEncrypt(paramSb.toString(), aesKey,true);
        String key = AesUtils.MD5(this.agent + timestamp + this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.apiDomain).append("?").append("agent=").append(this.agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        log.info("乐游登录返回:{}", result);
        JSONObject resJSON = JSONObject.parseObject(result);
        JSONObject d = resJSON.getJSONObject("d");
        if (d.getIntValue("code") != 0) {
            log.error("乐游 获取游戏URL失败 param= " + param + " result = " + result);
            throw new Exception("乐游注册失败");
        }

        // 执行注册逻辑
        UserPlat save = new UserPlat();
        save.setUserName(userName);
        save.setPlatCode(platCode);
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
        paramSb.append("s=7&").append("account=").append(account);
        String param = AesUtils.AESEncrypt(paramSb.toString(), aesKey,true);
        String key = AesUtils.MD5(agent + timestamp + this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.apiDomain).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        log.info("乐游查询余额返回:{}", result);
        JSONObject jsonResult = JSONObject.parseObject(result);
        JSONObject d = jsonResult.getJSONObject("d");
        if (d != null && d.getIntValue("code") == 0) {
            return d.getBigDecimal("freeMoney") == null ? BigDecimal.ZERO : d.getBigDecimal("freeMoney");
        }
        log.error("乐游 获取用户余额失败 param= " + param + " result = " + result);
        return BigDecimal.ZERO;
    }

    @Override
    public String getLoginUrl(PlatLoginUrlBO userPlat) throws Exception {
        // 注册三方
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = userPlat.getPlatUserName();
        String orderid = agent + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + account;
        String lineCode = "GS";
        String kindId = userPlat.getGameCode();
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=0&")
                .append("account=").append(account)
                .append("&money=0")
                .append("&orderid=").append(orderid)
                .append("&ip=127.0.0.1")
                .append("&lineCode=").append(lineCode)
                .append("&KindID=").append(kindId);
        String param = AesUtils.AESEncrypt(paramSb.toString(), aesKey,true);
        String key = AesUtils.MD5(agent + timestamp + this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.apiDomain).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        log.info("乐游登录返回:{}", result);
        JSONObject resJSON = JSONObject.parseObject(result);
        JSONObject d = resJSON.getJSONObject("d");
        if (d.getIntValue("code") != 0) {
            log.error("乐游 获取游戏URL失败 param= " + param + " result = " + result);
            throw new Exception("乐游登录失败");
        }
        return d.getString("url");
    }

    @Override
    public boolean deposit(BigDecimal money, UserPlat userPlat, String platOrderNo) throws Exception {
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = userPlat.getPlatUserName();
        String orderid = platOrderNo;
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=2&")
                .append("account=").append(account)
                .append("&money=").append(money)
                .append("&orderid=").append(orderid);
        String param = AesUtils.AESEncrypt(paramSb.toString(), aesKey,true);
        String key = AesUtils.MD5(agent + timestamp + this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.apiDomain).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        log.info("乐游[{}]额度转入返回:{}", platOrderNo, result);
        JSONObject res = JSONObject.parseObject(result);
        JSONObject d = res.getJSONObject("d");
        return d.getIntValue("code") == 0;
    }

    @Override
    public boolean withdraw(BigDecimal amount, UserPlat userPlat, String platOrderNo) throws Exception {
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = userPlat.getPlatUserName();
        BigDecimal money = amount;
        String orderid = agent + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + account;
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=3&")
                .append("account=").append(account)
                .append("&money=").append(money)
                .append("&orderid=").append(orderid);
        String param = AesUtils.AESEncrypt(paramSb.toString(), aesKey,true);
        String key = AesUtils.MD5(agent+timestamp+this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.apiDomain).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        log.info("乐游额度转出返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        JSONObject d = res.getJSONObject("d");
        return d.getIntValue("code") == 0;
    }

    @Override
    public String getDepositOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        return this.agent + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + userPlat.getPlatUserName();
    }

    @Override
    public String getWithdrawOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        return this.agent + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + userPlat.getPlatUserName();
    }

    @Override
    public boolean logout(UserPlat userPlat) {
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = userPlat.getPlatUserName();
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=8&").append("account=").append(account);
        String param = AesUtils.AESEncrypt(paramSb.toString(), aesKey,true);
        String key = AesUtils.MD5(agent+timestamp+this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.apiDomain).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        HttpUtil.get(urlSB.toString());
        return true;
    }
}
