package com.gs.business.service.impl.plat;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.business.pojo.bo.PlatLoginUrlBO;
import com.gs.business.service.PlatService;
import com.gs.commons.entity.UserPlat;
import com.gs.commons.service.UserPlatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class DgServiceImpl implements PlatService {
    /**
     * 平台标识
     */
    @Value("${platform.owner}")
    public String owner;
    @Value("${platform.DG.apiDomain}")
    private String apiDomain;
    @Value("${platform.DG.apiKey}")
    private String apiKey;
    @Value("${platform.DG.agent}")
    private String agent;

    public static String platCode = "DG";

    @Autowired
    private UserPlatService userPlatService;

    public String apiRequest(String apiUrl, JSONObject param){
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

        String apiUrl = this.apiDomain + "/user/signup/" + this.agent;
        String random = RandomUtil.randomString(6);
        String token = DigestUtil.md5Hex(this.agent + this.apiKey + random);
        String data = "A";
        String username = this.owner + userName;
        String password = RandomUtil.randomString(11);
        String currencyName = "CNY";
        BigDecimal winLimit = BigDecimal.ZERO;
        JSONObject member = new JSONObject();
        member.put("username",username);
        member.put("password",password);
        member.put("currencyName",currencyName);
        member.put("winLimit",winLimit);

        JSONObject param = new JSONObject();
        param.put("token",token);
        param.put("random",random);
        param.put("data",data);
        param.put("member",member);
        String result = apiRequest(apiUrl, param);
        log.info("DG登录返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);

        if(res.getInteger("codeId") != 0){
            log.error("DG 注册用户失败 param:{} result:{}", param, result);
            throw new Exception("DG注册失败");
        }

        // 执行注册逻辑
        UserPlat save = new UserPlat();
        save.setUserName(userName);
        save.setPlatCode(platCode);
        save.setPlatUserName(username);
        save.setPlatUserPassword(password);
        save.setStatus(0);
        save.setCreateTime(new Date());
        userPlatService.save(save);
        return save;
    }

    @Override
    public BigDecimal queryBalance(UserPlat userPlat) {
        String apiUrl = this.apiDomain + "/user/getBalance/" + this.agent;
        String random = RandomUtil.randomString(6);
        String token = DigestUtil.md5Hex(this.agent + this.apiKey + random);
        String username = userPlat.getPlatUserName();
        JSONObject member = new JSONObject();
        member.put("username",username);

        JSONObject param = new JSONObject();
        param.put("token",token);
        param.put("random",random);
        param.put("member",member);
        String result = apiRequest(apiUrl, param);
        log.info("DG查询余额返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        if (res.getInteger("codeId") == 0){
            JSONObject memberR = res.getJSONObject("member");
            BigDecimal balance = memberR.getBigDecimal("balance");
            return balance == null ? BigDecimal.ZERO : balance;
        }
        log.error("DG 获取用户余额失败 apiUrl:{}  result:{}", apiUrl, result);
        return BigDecimal.ZERO;
    }

    @Override
    public String getLoginUrl(PlatLoginUrlBO userPlat) throws Exception {
        String apiUrl = this.apiDomain + "/user/login/" + this.agent;
        String random = RandomUtil.randomString(6);
        String token = DigestUtil.md5Hex(this.agent + this.apiKey + random);
        String username = userPlat.getPlatUserName();
        JSONObject member = new JSONObject();
        member.put("username",username);

        JSONObject param = new JSONObject();
        param.put("token",token);
        param.put("random",random);
        param.put("member",member);
        String result = apiRequest(apiUrl, param);
        log.info("DG登录返回:{}", result);
        JSONObject res = JSONObject.parseObject(result);
        List<String> urlList = res.getList("list", String.class);
        if (res.getInteger("codeId") != 0 || CollUtil.isEmpty(urlList)){
            log.error("DG 获取游戏URL失败 param:{}  result:{}", param, result);
            throw new Exception("DG登录失败");
        }
        String finalUrl = urlList.get(0) + res.getString("token")  + "&language=cn&showapp=off";
        return finalUrl;
    }

    @Override
    public boolean deposit(BigDecimal money, UserPlat userPlat, String platOrderNo) throws Exception {
        return transfer(money, platOrderNo, userPlat.getPlatUserName());
    }

    /**
     * 正数转入  负数转出
     * @param amount
     * @return
     */
    public boolean transfer(BigDecimal amount, String platOrderNo, String platUserName){
        String apiUrl = this.apiDomain + "/account/transfer/" + this.agent;
        String random = RandomUtil.randomString(6);
        String token = DigestUtil.md5Hex(this.agent + this.apiKey + random);
        String data = platOrderNo;
        String username = platUserName;
        JSONObject member = new JSONObject();
        member.put("username",username);
        member.put("amount",amount);

        JSONObject param = new JSONObject();
        param.put("token",token);
        param.put("random",random);
        param.put("data",data);
        param.put("member",member);
        String result = apiRequest(apiUrl, param);
        log.info("DG 额度转换platOrderNo{}返回:{}",platOrderNo, result);
        JSONObject res = JSONObject.parseObject(result);
        return res.getInteger("codeId") == 0;
    }

    @Override
    public boolean withdraw(BigDecimal amount, UserPlat userPlat, String platOrderNo) throws Exception {
        return transfer(amount.negate(), platOrderNo, userPlat.getPlatUserName());
    }

    @Override
    public String getDepositOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        return "INDG" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + userPlat.getPlatUserName();
    }

    @Override
    public String getWithdrawOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        return "OUTDG" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + userPlat.getPlatUserName();
    }

    /**
     * 踢人需要先调用此接口
     * @return
     */
    public List<JSONObject> onlineReport(){
        String apiUrl = this.apiDomain + "/user/onlineReport/" + this.agent;
        String random = RandomUtil.randomString(6);
        String token = DigestUtil.md5Hex(this.agent + this.apiKey + random);

        JSONObject param = new JSONObject();
        param.put("token",token);
        param.put("random",random);
        String result = apiRequest(apiUrl, param);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        List<JSONObject> list = new ArrayList<>();
        if(res.getInteger("codeId") == 0){
            list = res.getList("list", JSONObject.class);
        }
        return list;
    }

    @Override
    public boolean logout(UserPlat userPlat) {
        String apiUrl = this.apiDomain + "/user/offline/" + this.agent;
        String random = RandomUtil.randomString(6);
        String token = DigestUtil.md5Hex(this.agent + this.apiKey + random);
        String username = userPlat.getPlatUserName();
        List<JSONObject> memberList = onlineReport();
        if(CollUtil.isEmpty(memberList)){
            return true;
        }
        Optional<Integer> memberId = memberList.stream().filter(m -> m.getString("username").equalsIgnoreCase(username))
                .map(m -> m.getInteger("memberId")).findFirst();
        if(!memberId.isPresent()){
            return true;
        }
        List<Integer> list = new ArrayList<>();
        list.add(memberId.get());
        JSONObject param = new JSONObject();
        param.put("token",token);
        param.put("random",random);
        param.put("list",list);
        String result = apiRequest(apiUrl, param);
        JSONObject res = JSONObject.parseObject(result);
        return res.getInteger("codeId") == 0;
    }
}
