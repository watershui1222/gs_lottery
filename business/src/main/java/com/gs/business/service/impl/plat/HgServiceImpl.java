package com.gs.business.service.impl.plat;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class HgServiceImpl implements PlatService {
    /**
     * 平台标识
     */
    @Value("${platform.owner}")
    public String owner;
    @Value("${platform.HuangGuan.agId}")
    public String agId;
    @Value("${platform.HuangGuan.agPassword}")
    public String agPassword;
    @Value("${platform.HuangGuan.agName}")
    public String agName;
    @Value("${platform.HuangGuan.secretKey}")
    public String secretKey;
    @Value("${platform.HuangGuan.apiUrl}")
    public String apiUrl;

    @Autowired
    private UserPlatService userPlatService;

    public static String platCode = "HG";

    @Autowired
    private StringRedisTemplate redisTemplate;

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
        String token = agLogin();
        if(StrUtil.isNotBlank(token)){
            //代理登录成功
            JSONObject param = new JSONObject();
            JSONObject request = new JSONObject();
            String memname = this.owner + userName;
            String password = RandomUtil.randomString(8);
            request.put("memname", memname);
            request.put("password", password);
            request.put("currency", "CNY");
            request.put("method", "CreateMember");
            request.put("token", token);
            request.put("timestamp", DateUtil.current());
            String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey, false);
            param.put("Request", requestStr);
            param.put("Method", "CreateMember");
            param.put("AGID", agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            log.info("皇冠 createMember接口 result = " + resStr);
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = AesUtils.AESDecrypt(resStr, this.secretKey);
                String respcode = JSONObject.parseObject(result).getString("respcode");
                if(!StrUtil.equals(respcode, "0000")){
                    log.error("皇冠 获取游戏URL失败 param= " + param + " result = " + result);
                    throw new Exception("皇冠注册失败");
                }
            }
            // 执行注册逻辑
            UserPlat save = new UserPlat();
            save.setUserName(userName);
            save.setPlatCode(platCode);
            save.setPlatUserName(memname);
            save.setPlatUserPassword(password);
            save.setStatus(0);
            save.setCreateTime(new Date());
            userPlatService.save(save);
            return save;
        }else{
            throw new Exception("皇冠注册失败,aglogin 获取token失败");
        }
    }

    public String agLogin() {
        String tokenKey = "HGtokenkey";
        if(redisTemplate.hasKey(tokenKey)){
            return redisTemplate.opsForValue().get(tokenKey);
        }
        String token = "";
        JSONObject param = new JSONObject();
        JSONObject request = new JSONObject();
        request.put("username", this.agName);
        request.put("password", this.agPassword);
        request.put("timestamp", DateUtil.current());
        String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey, false);
        param.put("Request", requestStr);
        param.put("Method", "AGLogin");
        param.put("AGID", this.agId);
        HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
        String encryptRes = res.body();
        String result = AesUtils.AESDecrypt(encryptRes, this.secretKey);
        log.info("皇冠 aglogin接口 result = " + result);
        JSONObject resultJS = JSONObject.parseObject(result);
        if(StrUtil.equals(resultJS.getString("respcode"), "0000")){
            token = resultJS.getString("token");
            redisTemplate.opsForValue().set(tokenKey, token, 36, TimeUnit.HOURS);
            return token;
        }
        return token;
    }

    @Override
    public BigDecimal queryBalance(UserPlat userPlat) {
        String token = agLogin();
        if(StrUtil.isNotBlank(token)){
            //代理登录成功
            JSONObject param = new JSONObject();
            JSONObject request = new JSONObject();
            String memname = userPlat.getPlatUserName();
            request.put("memname", memname);
            request.put("method", "chkMemberBalance");
            request.put("token", token);
            request.put("timestamp", DateUtil.current());
            String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey,false);
            param.put("Request", requestStr);
            param.put("Method", "chkMemberBalance");
            param.put("AGID", this.agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            log.info("皇冠 chkMemberBalance接口 result = " + resStr);
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = AesUtils.AESDecrypt(resStr, this.secretKey);
                JSONObject resultJson = JSONObject.parseObject(result);
                String respcode = resultJson.getString("respcode");
                if(StrUtil.equals(respcode, "0000")){
                    BigDecimal balance = resultJson.getBigDecimal("balance");
                    return balance == null ? BigDecimal.ZERO : balance;
                }
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getLoginUrl(PlatLoginUrlBO userPlat) throws Exception {
        String token = agLogin();
        if(StrUtil.isNotBlank(token)){
            //代理登录成功
            JSONObject param = new JSONObject();
            JSONObject request = new JSONObject();
            String memname = userPlat.getPlatUserName();
            String password = userPlat.getPlatUserPassword();
            request.put("memname", memname);
            request.put("password", password);
            request.put("currency", "CNY");
            request.put("method", "LaunchGame");
            request.put("token", token);
            String machine = userPlat.getDevice() == 1 ? "PC" : "MOBILE";
            request.put("machine", machine);
            request.put("langx", "zh-cn");
            request.put("remoteip", "127.0.0.1");
            request.put("timestamp", DateUtil.current());
            String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey, false);
            param.put("Request", requestStr);
            param.put("Method", "LaunchGame");
            param.put("AGID", this.agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            log.info("皇冠 launchGame接口 result = " + resStr);
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = AesUtils.AESDecrypt(resStr, this.secretKey);
                JSONObject resultJson = JSONObject.parseObject(result);
                String respcode = resultJson.getString("respcode");
                if(StrUtil.equals(respcode, "0000")){
                    return resultJson.getString("launchgameurl");
                }else{
                    log.error("皇冠 获取游戏URL失败 param= " + param + " result = " + result);
                    throw new Exception("皇冠登录失败");
                }
            }else{
                throw new Exception("皇冠登录失败");
            }
        }else{
            throw new Exception("皇冠 aglogin 获取token失败");
        }
    }

    @Override
    public boolean deposit(BigDecimal money, UserPlat userPlat, String platOrderNo) throws Exception {
        String token = agLogin();
        if(StrUtil.isNotBlank(token)){
            //代理登录成功
            JSONObject param = new JSONObject();
            JSONObject request = new JSONObject();
            String memname = userPlat.getPlatUserName();
            String amount = String.valueOf(money);
            String payno = platOrderNo;
            request.put("memname", memname);
            request.put("amount", amount);
            request.put("payno", payno);
            request.put("method", "Deposit");
            request.put("token", token);
            request.put("timestamp", DateUtil.current());
            String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey,false);
            param.put("Request", requestStr);
            param.put("Method", "Deposit");
            param.put("AGID", this.agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            log.info("皇冠 deposit result = " + resStr);
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = AesUtils.AESDecrypt(resStr, this.secretKey);
                JSONObject resultJson = JSONObject.parseObject(result);
                String respcode = resultJson.getString("respcode");
                if(StrUtil.equals(respcode, "0000")){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean withdraw(BigDecimal money, UserPlat userPlat, String platOrderNo) throws Exception {
        String token = agLogin();
        if(StrUtil.isNotBlank(token)){
            //代理登录成功
            JSONObject param = new JSONObject();
            JSONObject request = new JSONObject();
            String memname = userPlat.getPlatUserName();
            String amount = String.valueOf(money);
            String payno = platOrderNo;//我方单号
            request.put("memname", memname);
            request.put("amount", amount);
            request.put("payno", payno);
            request.put("method", "Withdraw");
            request.put("token", token);
            request.put("timestamp", DateUtil.current());
            String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey,false);
            param.put("Request", requestStr);
            param.put("Method", "Withdraw");
            param.put("AGID", agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            log.info("皇冠 withdraw接口 result = " + resStr);
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = AesUtils.AESDecrypt(resStr, this.secretKey);
                JSONObject resultJson = JSONObject.parseObject(result);
                String respcode = resultJson.getString("respcode");
                if(StrUtil.equals(respcode, "0000")){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getDepositOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        return "INHG" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + userPlat.getPlatUserName();
    }

    @Override
    public String getWithdrawOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        return "OUTHG" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") +userPlat.getPlatUserName();
    }

    @Override
    public boolean logout(UserPlat userPlat) {
        String token = agLogin();
        if(StrUtil.isNotBlank(token)){
            //代理登录成功
            JSONObject param = new JSONObject();
            JSONObject request = new JSONObject();
            String memname = userPlat.getPlatUserName();
            request.put("memname", memname);
            request.put("method", "KickOutMem");
            request.put("token", token);
            request.put("timestamp", DateUtil.current());
            String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey,false);
            param.put("Request", requestStr);
            param.put("Method", "KickOutMem");
            param.put("AGID", agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            log.info("皇冠 kickOutMem接口 result = " + resStr);
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = AesUtils.AESDecrypt(resStr, this.secretKey);
                System.out.println(result);
                JSONObject resultJson = JSONObject.parseObject(result);
                String respcode = resultJson.getString("respcode");
                if(StrUtil.equals(respcode, "0000")){
                    return true;
                }
            }
        }
        return false;
    }
}
