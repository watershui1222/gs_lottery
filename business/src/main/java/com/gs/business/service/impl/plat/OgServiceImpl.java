package com.gs.business.service.impl.plat;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
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
public class OgServiceImpl implements PlatService {
    /**
     * 平台标识
     */
    @Value("${platform.owner}")
    public String owner;
    @Value("${platform.OG.publicKey}")
    private String publicKey;
    @Value("${platform.OG.privateKey}")
    private String privateKey;
    @Value("${platform.OG.operator}")
    private String operator;
    @Value("${platform.OG.apiDomain}")
    private String apiDomain;
    @Value("${platform.OG.betLimit}")
    private String betLimit;
    @Value("${platform.OG.lobbyId}")
    private String lobbyId;

    @Autowired
    private UserPlatService userPlatService;

    public static String platCode = "OG";

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

        String player_id = this.owner + userName;;;
        String nickname = player_id + "nk";
        Long timestamp = DateUtil.current();
        JSONObject param = new JSONObject();
        param.put("player_id",player_id);
        param.put("nickname",nickname);
        param.put("timestamp",timestamp);
        String apiUrl = this.apiDomain + "/api/v2/platform/transfer-wallet/register";
        String result = apiRequestPost(apiUrl, param);
        log.info("OG登录返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        String rsCode = res.getString("rs_code");
        if(!StrUtil.equals(rsCode, "S-100")){
            log.error("OG 注册用户失败 param= " + param + " result = " + result);
            throw new Exception("OG注册失败");
        }

        // 执行注册逻辑
        UserPlat save = new UserPlat();
        save.setUserName(userName);
        save.setPlatCode(platCode);
        save.setPlatUserName(player_id);
        save.setPlatUserPassword(null);
        save.setStatus(0);
        save.setCreateTime(new Date());
        userPlatService.save(save);
        return save;
    }

    public String apiRequestPost(String apiUrl, JSONObject param){
        Map<String, String> header = new HashMap<>();
        header.put("key", this.publicKey);
        header.put("operator-name", this.operator);
        return HttpUtil.createPost(apiUrl).addHeaders(header).body(param.toJSONString()).timeout(10000).execute().body();
    }

    public String apiRequestGet(String apiUrl){
        Map<String, String> header = new HashMap<>();
        header.put("key", this.publicKey);
        header.put("operator-name", this.operator);
        return HttpUtil.createGet(apiUrl).addHeaders(header).timeout(10000).execute().body();
    }

    @Override
    public BigDecimal queryBalance(UserPlat userPlat) {
        String player_id = userPlat.getPlatUserName();
        JSONObject json = new JSONObject();
        json.put("player_id",player_id);
        String sortParmstr = SignUtils.sortParam(json);
        String apiUrl = this.apiDomain + "/api/v2/platform/transfer-wallet/get-balance?" + sortParmstr;
        String result = apiRequestGet(apiUrl);
        log.info("OG查询余额返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        String rsCode = res.getString("rs_code");
        if(StrUtil.equals(rsCode, "S-100")){
            BigDecimal balance = res.getBigDecimal("current_balance");
            return balance == null ? BigDecimal.ZERO : balance;
        }
        log.error("OG 获取用户余额失败 apiUrl:{}  result:{}", apiUrl, result);
        return BigDecimal.ZERO;
    }

    @Override
    public String getLoginUrl(PlatLoginUrlBO userPlat) throws Exception {
        String player_id = userPlat.getPlatUserName();
        String nickname = player_id + "nk";
        Long timestamp = DateUtil.current();
        String lang = "zh";
        String token = player_id + "tk";
        Integer game_id = Integer.valueOf(this.lobbyId);
        Integer betlimit = Integer.valueOf(this.betLimit);
        JSONObject json = new JSONObject();
        json.put("player_id",player_id);
        json.put("nickname",nickname);
        json.put("timestamp",timestamp);
        json.put("lang",lang);
        json.put("token",token);
        json.put("game_id",game_id);
        json.put("betlimit",betlimit);
        String sortParmstr = SignUtils.sortParam(json);
        String signature = DigestUtil.md5Hex(sortParmstr + this.privateKey);
        String param = sortParmstr + "&signature=" + signature;
        String apiUrl = this.apiDomain + "/api/v2/platform/games/launch?" + param;
        String result = apiRequestGet(apiUrl);
        log.info("OG登录返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        String rsCode = res.getString("rs_code");
        if(!StrUtil.equals(rsCode, "S-100")){
            log.error("OG 获取游戏URL失败 param:{}  result:{}", param, result);
            throw new Exception("OG登录失败");
        }
        return res.getString("game_link");
    }

    @Override
    public boolean deposit(BigDecimal money, UserPlat userPlat, String platOrderNo) throws Exception {
        String player_id = userPlat.getPlatUserName();
        String transaction_id = platOrderNo;
        BigDecimal transfer_amount = money;
        Long timestamp = DateUtil.current();
        JSONObject param = new JSONObject();
        param.put("player_id",player_id);
        param.put("transaction_id",transaction_id);
        param.put("transfer_amount",transfer_amount);
        param.put("timestamp",timestamp);
        String signature = DigestUtil.md5Hex(SignUtils.sortParam(param) + this.privateKey);
        param.put("signature",signature);
        String apiUrl = this.apiDomain + "/api/v2/platform/transfer-wallet/deposit";
        String result = apiRequestPost(apiUrl, param);
        log.info("OG 转入参数:{}", param);
        log.info("OG 转入返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        String rsCode = res.getString("rs_code");
        return StrUtil.equals(rsCode, "S-100");
    }

    @Override
    public boolean withdraw(BigDecimal amount, UserPlat userPlat, String platOrderNo) throws Exception {
        String player_id = userPlat.getPlatUserName();
        String transaction_id = platOrderNo;
        BigDecimal transfer_amount = amount;
        Long timestamp = DateUtil.current();
        JSONObject param = new JSONObject();
        param.put("player_id",player_id);
        param.put("transaction_id",transaction_id);
        param.put("transfer_amount",transfer_amount);
        param.put("timestamp",timestamp);
        String signature = DigestUtil.md5Hex(SignUtils.sortParam(param) + this.privateKey);
        param.put("signature",signature);
        String apiUrl = this.apiDomain + "/api/v2/platform/transfer-wallet/withdraw";
        String result = apiRequestPost(apiUrl, param);
        log.info("OG 转出返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        String rsCode = res.getString("rs_code");
        return StrUtil.equals(rsCode, "S-100");
    }

    @Override
    public String getDepositOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        return "inog" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + userPlat.getPlatUserName();//只能小写字母
    }

    @Override
    public String getWithdrawOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        return "outog" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + userPlat.getPlatUserName();
    }

    @Override
    public boolean logout(UserPlat userPlat) {
        log.error("OG 没有踢线接口");
        return false;
    }
}
