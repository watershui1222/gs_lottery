package com.gs.business.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.business.pojo.PlatLoginUrlBO;
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
import java.util.TimeZone;

@Slf4j
@Service
public class BbinServiceImpl implements PlatService {
    /**
     * 平台标识
     */
    @Value("${platform.owner}")
    public String owner;
    @Value("${platform.BBIN.website}")
    public String website;
    @Value("${platform.BBIN.uppername}")
    public String uppername;
    @Value("${platform.BBIN.apiDomain}")
    public String apiDomain;

    @Autowired
    private UserPlatService userPlatService;

    @Override
    public UserPlat register(String userName) throws Exception {
        // 查询是否注册平台
        UserPlat userPlat = userPlatService.getOne(
                new LambdaQueryWrapper<UserPlat>()
                        .eq(UserPlat::getUserName, userName)
                        .eq(UserPlat::getPlatCode, "BBIN")
        );
        if (userPlat != null) {
            return userPlat;
        }

        // 注册三方
        String session = createSession(userName);
        if(StrUtil.isBlank(session)){
            throw new Exception("BBIN注册失败");
        }
        // 执行注册逻辑
        UserPlat save = new UserPlat();
        save.setUserName(userName);
        save.setPlatCode("BBIN");
        save.setPlatUserName(this.owner + userName);
        save.setPlatUserPassword(null);
        save.setStatus(0);
        save.setCreateTime(new Date());
        userPlatService.save(save);
        return save;
    }

    /**
     *
     * @param userName 我方平台的username
     * @return
     */
    private String createSession(String userName){
        //获得URL前需使用此接口获取用户session
        String apiUrl = this.apiDomain + "/CreateSession";
        String username = this.owner + userName;
        String website = this.website;
        String uppername = this.uppername;
        String lang = "zh-cn";
        String keyB = "sE3jbKtn";
        String strA = RandomUtil.randomString(4);
        DateTime mdTime = DateUtil.offsetHour(DateUtil.date(), -12);
        String strB = AesUtils.MD5(website + username + keyB + DateUtil.format(mdTime, "yyyyMMdd"));
        String strC = RandomUtil.randomString(3);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("username", username);
        param.put("uppername", uppername);
        param.put("lang", lang);
        param.put("key", key.toLowerCase());
        String result = HttpUtil.post(apiUrl, param);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONObject data = resJson.getJSONObject("data");
        if(data != null || StrUtil.isNotBlank(data.getString("sessionid"))){
            return data.getString("sessionid");
        }
        log.error("BBIN createSession失败 " + param + " result = " + result);
        return "";
    }

    @Override
    public BigDecimal queryBalance(UserPlat userPlat) {
        String apiUrl = this.apiDomain + "/CheckUsrBalance";
        String website = this.website;
        String username = userPlat.getPlatUserName();
        String uppername = this.uppername;
        String keyB = "bHqo11";
        String strA = RandomUtil.randomString(7);
        DateTime mdTime = DateUtil.offsetHour(DateUtil.date(), -12);
        String strB = AesUtils.MD5(website + username + keyB + DateUtil.format(mdTime, "yyyyMMdd"));
        String strC = RandomUtil.randomString(3);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("username", username);
        param.put("uppername", uppername);
        param.put("key", key.toLowerCase());
        String result = HttpUtil.post(apiUrl, param);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONArray data = resJson.getJSONArray("data");
        if(CollUtil.isNotEmpty(data)){
            JSONObject bobj = (JSONObject) data.get(0);
            BigDecimal balance = bobj.getBigDecimal("Balance");
            return balance == null ? BigDecimal.ZERO : balance;
        }
        log.error("BBIN 查询用户余额失败 param= " + param + " result = " + result);
        return BigDecimal.ZERO;
    }

    @Override
    public String getLoginUrl(PlatLoginUrlBO userPlat) throws Exception {
        //获得URL前需使用此接口获取用户session
        String website = this.website;
        String sessionid = createSession(userPlat.getUserName());
        if(StrUtil.isBlank(sessionid)){
            return "获取sessionid失败";
        }
        String lang = "zh-cn";
        String tag = "global";
        String keyB = "2dQX6z";
        String strA = RandomUtil.randomString(5);
        DateTime mdTime = DateUtil.offsetHour(DateUtil.date(), -12);
        String strB = AesUtils.MD5(website + keyB + DateUtil.format(mdTime, "yyyyMMdd"));
        String strC = RandomUtil.randomString(6);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("lang", lang);
        param.put("sessionid", sessionid);

        param.put("key", key.toLowerCase());
        String apiUrl = "";
        if(StrUtil.equals(userPlat.getPlatSubCode(), "BBINLIVE")){
            apiUrl = this.apiDomain + "/GameUrlBy3";
            param.put("tag", tag);
        }else if(StrUtil.equals(userPlat.getPlatSubCode(), "BBINELE")){
            apiUrl = this.apiDomain + "/GameUrlBy5";
            String gametype = userPlat.getGameCode();//游戏ID
            String exit_option = "1";
            param.put("gametype", gametype);
            param.put("exit_option", exit_option);
        }else if (StrUtil.equals(userPlat.getPlatSubCode(), "BBINFISH")){
            String gametype = userPlat.getGameCode();//38001-捕鱼大师    38002-富贵渔场
            String exit_option = "1";
            apiUrl = this.apiDomain + "/GameUrlBy38";
            param.put("gametype", gametype);
            param.put("exit_option", exit_option);
        }
        String result = HttpUtil.post(apiUrl, param);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONArray data = resJson.getJSONArray("data");
        if(!resJson.getBoolean("result") || CollUtil.isEmpty(data)){
            log.error("BBIN getLoginUrl失败 param= " + param + " result = " + result);
            throw new Exception("BBIN登录失败");
        }
        JSONObject urlObj = (JSONObject) data.get(0);
        String url = "";
        if(StrUtil.equals(userPlat.getPlatSubCode(), "BBINLIVE")){
            url = urlObj.getString("rwd");
        }else {
            url = urlObj.getString("html5");
        }
        return url;
    }

    @Override
    public boolean deposit(BigDecimal money, UserPlat userPlat, String platOrderNo) throws Exception {
        //获得URL前需使用此接口获取用户session
        String apiUrl = this.apiDomain + "/Transfer";
        String website = this.website;
        String username = userPlat.getPlatUserName();
        String uppername = this.uppername;
        String remitno = RandomUtil.randomNumbers(11);
        String action = "IN";
        BigDecimal remit = money;
        String keyB = "upVqp41";
        String strA = RandomUtil.randomString(6);
        DateTime mdTime = DateUtil.offsetHour(DateUtil.date(), -12);
        String strB = AesUtils.MD5(website + username + remitno + keyB + DateUtil.format(mdTime, "yyyyMMdd"));
        String strC = RandomUtil.randomString(7);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("username", username);
        param.put("uppername", uppername);
        param.put("remitno", remitno);
        param.put("action", action);
        param.put("remit", remit);
        param.put("key", key.toLowerCase());
        String result = HttpUtil.post(apiUrl, param);
        log.info("BBIN transferIn param= " + param + " result = " + result);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONObject data = resJson.getJSONObject("data");
        if(data != null && StrUtil.equals(data.getString("Code"), "11100")){
            //转账成功
            return true;
        }
        return false;
    }

    @Override
    public boolean withdraw(BigDecimal amount, UserPlat userPlat, String platOrderNo) throws Exception {
        //获得URL前需使用此接口获取用户session
        String apiUrl = this.apiDomain + "/Transfer";
        String website = this.website;
        String username = userPlat.getPlatUserName();
        String uppername = this.uppername;
        String remitno = RandomUtil.randomNumbers(11);
        String action = "OUT";
        BigDecimal remit = amount;
        String keyB = "upVqp41";
        String strA = RandomUtil.randomString(6);
        DateTime mdTime = DateUtil.offsetHour(DateUtil.date(), -12);
        String strB = AesUtils.MD5(website + username + remitno + keyB + DateUtil.format(mdTime, "yyyyMMdd"));
        String strC = RandomUtil.randomString(7);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("username", username);
        param.put("uppername", uppername);
        param.put("remitno", remitno);
        param.put("action", action);
        param.put("remit", remit);
        param.put("key", key.toLowerCase());
        String result = HttpUtil.post(apiUrl, param);
        log.info("BBIN transferOut param= " + param + " result = " + result);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONObject data = resJson.getJSONObject("data");
        if(data != null && StrUtil.equals(data.getString("Code"), "11100")){
            //转账成功
            return true;
        }
        return false;
    }

    @Override
    public String getDepositOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        //只能整形数字式的订单
        return RandomUtil.randomNumbers(11);
    }

    @Override
    public String getWithdrawOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        //只能整形数字式的订单
        return RandomUtil.randomNumbers(11);
    }

    @Override
    public boolean logout(UserPlat userPlat) {
        String apiUrl = this.apiDomain + "/Logout";
        String website = this.website;
        String username = userPlat.getPlatUserName();
        String strA = RandomUtil.randomString(7);
        DateTime mdTime = DateUtil.offsetHour(DateUtil.date(), -12);
        String keyB = "dhoOr";
        String strB = AesUtils.MD5(website + username + keyB + DateUtil.format(mdTime, "yyyyMMdd"));
        String strC = RandomUtil.randomString(4);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("username", username);
        param.put("key", key.toLowerCase());
        String result = HttpUtil.post(apiUrl, param);
        log.info("BBIN logout param= " + param + " result = " + result);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONObject data = resJson.getJSONObject("data");
        if(data != null && StrUtil.equals(data.getString("Code"), "22001")){
            return true;
        }
        return false;
    }
}
