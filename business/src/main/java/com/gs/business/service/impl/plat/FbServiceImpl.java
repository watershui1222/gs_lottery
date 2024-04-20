package com.gs.business.service.impl.plat;

import cn.hutool.core.date.DateUtil;
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FbServiceImpl implements PlatService {
    /**
     * 平台标识
     */
    @Value("${platform.owner}")
    public String owner;
    @Value("${platform.FB.apiDomain}")
    private String apiDomain;
    @Value("${platform.FB.channelId}")
    private String channelId;
    @Value("${platform.FB.channelSecret}")
    private String channelSecret;

    @Autowired
    private UserPlatService userPlatService;

    public static String platCode = "FB";

    private String apiRequest(JSONObject param, String apiUrl){
        Long timestamp = DateUtil.current();
        String sign = SignUtils.fbSportSign(param, this.channelId, timestamp, this.channelSecret);
        Map<String, String> header = new HashMap<>();
        header.put("sign", sign);
        header.put("timestamp", timestamp.toString());
        header.put("merchantId", this.channelId);
        return HttpUtil.createPost(apiUrl).addHeaders(header).body(param.toJSONString()).timeout(10000).execute().body();
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

        String apiUrl = this.apiDomain + "/fb/data/api/v2/new/user/create";
        String merchantUserId = this.owner + userName;
        JSONObject param = new JSONObject();
        param.put("merchantUserId", merchantUserId);
        String result = apiRequest(param, apiUrl);
        log.info("FB登录返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        if(!res.getBoolean("success")){
            log.error("FB 注册用户失败 param:{} result:{}", param ,result);
            throw new Exception("FB注册失败");
        }

        // 执行注册逻辑
        UserPlat save = new UserPlat();
        save.setUserName(userName);
        save.setPlatCode(platCode);
        save.setPlatUserName(merchantUserId);
        save.setPlatUserPassword(null);
        save.setStatus(0);
        save.setCreateTime(new Date());
        userPlatService.save(save);
        return save;
    }

    @Override
    public BigDecimal queryBalance(UserPlat userPlat) {
        String apiUrl = this.apiDomain + "/fb/data/api/v2/user/detail";
        String merchantUserId = userPlat.getPlatUserName();
        JSONObject param = new JSONObject();
        param.put("merchantUserId", merchantUserId);
        String result = apiRequest(param, apiUrl);
        log.info("FB查询余额返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        if(res.getBoolean("success")){
            JSONObject data = res.getJSONObject("data");
            BigDecimal balance = data.getBigDecimal("balance");
            return balance == null ? BigDecimal.ZERO : balance;
        }
        log.error("FB 获取用户余额失败 apiUrl:{}  result:{}", apiUrl, result);
        return BigDecimal.ZERO;
    }

    @Override
    public String getLoginUrl(PlatLoginUrlBO userPlat) throws Exception {
        String apiUrl = this.apiDomain + "/fb/data/api/v2/token/get";
        String merchantUserId = userPlat.getPlatUserName();
        String platForm = "h5";//pc，h5, mobile
        JSONObject param = new JSONObject();
        param.put("platForm", platForm);
        param.put("merchantUserId", merchantUserId);
        String result = apiRequest(param, apiUrl);
        log.info("FB登录返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        StringBuilder tokenUrl = new StringBuilder();
        if(!res.getBoolean("success")){
            log.error("FB 获取游戏URL失败 param:{}  result:{}", param, result);
            throw new Exception("FB登录失败");
        }
        JSONObject data = res.getJSONObject("data");
        String token = data.getString("token");
        JSONObject serverInfo = data.getJSONObject("serverInfo");
        String h5Address = serverInfo.getString("h5Address");
        String apiServerAddress = serverInfo.getString("apiServerAddress");
        String pushServerAddress = serverInfo.getString("pushServerAddress");
        String virtualAddress = serverInfo.getString("virtualAddress");
        //拼接URL
        tokenUrl.append(h5Address).append("/index.html#/?")
                .append("token=").append(token)
                .append("&nickname=").append(merchantUserId)
                .append("&apiSrc=").append(apiServerAddress)
                .append("&pushSrc=").append(pushServerAddress)
                .append("&virtualSrc=").append(virtualAddress);
        return tokenUrl.toString();
    }

    @Override
    public boolean deposit(BigDecimal amount, UserPlat userPlat, String platOrderNo) throws Exception {
        String apiUrl = this.apiDomain + "/fb/data/api/v2/new/transfer/in";
        String merchantUserId = userPlat.getPlatUserName();
        String businessId = platOrderNo;
        JSONObject param = new JSONObject();
        param.put("amount", amount);
        param.put("businessId", businessId);
        param.put("merchantUserId", merchantUserId);
        String result = apiRequest(param, apiUrl);
        log.info("FB 转入返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        return res.getBoolean("success");
    }

    @Override
    public boolean withdraw(BigDecimal amount, UserPlat userPlat, String platOrderNo) throws Exception {
        String apiUrl = this.apiDomain + "/fb/data/api/v2/new/transfer/out";
        String merchantUserId = userPlat.getPlatUserName();
        String businessId = platOrderNo;
        JSONObject param = new JSONObject();
        param.put("amount", amount);
        param.put("businessId", businessId);
        param.put("merchantUserId", merchantUserId);
        String result = apiRequest(param, apiUrl);
        log.info("FB 转出返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        return res.getBoolean("success");
    }

    @Override
    public String getDepositOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        return "INFB" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + userPlat.getPlatUserName();
    }

    @Override
    public String getWithdrawOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        return "OUTFB" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + userPlat.getPlatUserName();
    }

    @Override
    public boolean logout(UserPlat userPlat) {
        log.error("FB 没有踢线接口");
        return false;
    }
}
