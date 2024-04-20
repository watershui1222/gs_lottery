package com.gs.business.service.impl.plat;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.business.pojo.bo.PlatLoginUrlBO;
import com.gs.business.service.PlatService;
import com.gs.business.utils.plat.SignUtils;
import com.gs.commons.entity.UserPlat;
import com.gs.commons.service.UserPlatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
@Component
public class WeServiceImpl implements PlatService {
    /**
     * 平台标识
     */
    @Value("${platform.owner}")
    public String owner;
    @Value("${platform.WE.apiDomain}")
    public String apiDomain;
    @Value("${platform.WE.channelId}")
    public String channelId;
    @Value("${platform.WE.publicKey}")
    public String publicKey;
    @Value("${platform.WE.privateKey}")
    public String privateKey;

    @Autowired
    private UserPlatService userPlatService;

    public static String platCode = "WE";

    public static WeServiceImpl weService;
    @PostConstruct
    public void init(){
        weService = this;
    }

    private String apiRequest(String apiUrl, JSONObject param) {
        log.info("apiUrl:{}  param:{}",apiUrl, param.toString());
        return HttpUtil.createPost(apiUrl).header("Content-Type" , "application/json").body(param.toJSONString()).timeout(10000).execute().body();
    }

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

        String channelId = this.channelId;
        String username = this.owner + userName;
        String signature = SignUtils.weSign(username, this.privateKey, this.publicKey);
        JSONObject param = new JSONObject();
        param.put("channelId", channelId);
        param.put("username", username);
        param.put("signature", signature);
        String apiUrl = this.apiDomain + "/api/syncuser";
        String result = apiRequest(apiUrl, param);
        log.info("WE登录返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        if(res.getInteger("status") != 200){
            log.error("WE 注册用户失败 param:{} result:{}",param ,result);
            throw new Exception("WE注册失败");
        }

        // 执行注册逻辑
        UserPlat save = new UserPlat();
        save.setUserName(userName);
        save.setPlatCode(platCode);
        save.setPlatUserName(username);
        save.setPlatUserPassword(null);
        save.setStatus(0);
        save.setCreateTime(new Date());
        userPlatService.save(save);
        return save;
    }

    @Override
    public BigDecimal queryBalance(UserPlat userPlat) {
        String channelId = this.channelId;
        String username = userPlat.getPlatUserName();
        String signature = SignUtils.weSign(username, this.privateKey, this.publicKey);
        JSONObject param = new JSONObject();
        param.put("channelId", channelId);
        param.put("username", username);
        param.put("signature", signature);
        String apiUrl = this.apiDomain + "/api/userinfo";
        String result = apiRequest(apiUrl, param);
        log.info("WE查询余额返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        if(res.getInteger("status") == 200){
            BigDecimal balance = res.getBigDecimal("money");
            return balance == null ? BigDecimal.ZERO : balance;
        }
        log.error("WE 获取用户余额失败 apiUrl:{}  result:{}", apiUrl, result);
        return BigDecimal.ZERO;
    }

    @Override
    public String getLoginUrl(PlatLoginUrlBO userPlat) throws Exception {
        String channelId = this.channelId;
        Long timestamp = DateUtil.current();
        String signature = SignUtils.weSign(channelId + timestamp, this.privateKey, this.publicKey);
        String currency = "CNY";
        JSONObject param = new JSONObject();
        param.put("channelId", channelId);
        param.put("timestamp", timestamp);
        param.put("signature", signature);
        param.put("currency", currency);
        param.put("china", 1);
        String apiUrl = this.apiDomain + "/api/launchUrl";
        String result = apiRequest(apiUrl, param);
        log.info("WE登录返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        StringBuilder url = new StringBuilder();
        if(res.getInteger("status") != 200){
            log.error("WE 获取游戏URL失败 param:{}  result:{}", param, result);
            throw new Exception("WE登录失败");
        }
        String username = userPlat.getPlatUserName();
        String accessToken = RandomUtil.randomString(11);
        url.append(res.getString("launchUrl"))
                .append("?username=").append(username)
                .append("&accessToken=").append(accessToken)
                .append("&language=zh_cn");
        return url.toString();
    }

    @Override
    public boolean deposit(BigDecimal money, UserPlat userPlat, String platOrderNo) throws Exception {
        return transfer(money, userPlat.getPlatUserName(), platOrderNo);
    }

    /**
     * 正数转入  负数转出
     * @param amount
     * @return
     */
    public boolean transfer(BigDecimal amount, String platUserName, String platOrderNo){
        String channelId = this.channelId;
        String username = platUserName;
        Long timestamp = DateUtil.current();
        String signature = SignUtils.weSign(username + timestamp, this.privateKey, this.publicKey);
        String rechargeReqId = platOrderNo;
        JSONObject param = new JSONObject();
        param.put("channelId", channelId);
        param.put("username", username);
        param.put("timestamp", timestamp);
        param.put("signature", signature);
        param.put("money", amount);
        param.put("rechargeReqId", rechargeReqId);
        String apiUrl = this.apiDomain + "/api/recharge";
        String result = apiRequest(apiUrl, param);
        JSONObject res = JSONObject.parseObject(result);
        if(res.getInteger("status") == 200){
            //不管成功与否都必须请求确认接口
            return checkTransfer(rechargeReqId);
        }
        return false;
    }

    public boolean checkTransfer(String rechargeReqId){
        String channelId = this.channelId;
        String signature = SignUtils.weSign(rechargeReqId, this.privateKey, this.publicKey);
        JSONObject param = new JSONObject();
        param.put("channelId", channelId);
        param.put("signature", signature);
        param.put("rechargeReqId", rechargeReqId);
        String apiUrl = this.apiDomain + "/api/rechargestatus";
        String result = apiRequest(apiUrl, param);
        JSONObject res = JSONObject.parseObject(result);
        if(res.getInteger("status") == 200){
            return true;
        }
        return false;
    }

    @Override
    public boolean withdraw(BigDecimal amount, UserPlat userPlat, String platOrderNo) throws Exception {
        return transfer(amount.negate(), userPlat.getPlatUserName(), platOrderNo);
    }

    @Override
    public String getDepositOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        return "INWE" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + userPlat.getPlatUserName();
    }

    @Override
    public String getWithdrawOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        return "OUTWE" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + userPlat.getPlatUserName();
    }

    @Override
    public boolean logout(UserPlat userPlat) {
        String channelId = this.channelId;
        String username = userPlat.getPlatUserName();
        Long timestamp = DateUtil.current();
        String signature = SignUtils.weSign(username + channelId + timestamp, this.privateKey, this.publicKey);
        JSONObject param = new JSONObject();
        param.put("username", username);
        param.put("channelId", channelId);
        param.put("timestamp", timestamp);
        param.put("signature", signature);
        String apiUrl = this.apiDomain + "/api/logout";
        String result = apiRequest(apiUrl, param);
        log.info("WE 踢线返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        return res.getInteger("status") == 200;
    }
}
