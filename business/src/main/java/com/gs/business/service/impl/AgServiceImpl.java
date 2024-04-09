package com.gs.business.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.business.pojo.PlatLoginUrlBO;
import com.gs.business.service.PlatService;
import com.gs.commons.entity.UserPlat;
import com.gs.commons.service.UserPlatService;
import com.gs.commons.utils.DesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
public class AgServiceImpl implements PlatService {
    /**
     * 平台标识
     */
    @Value("${platform.owner}")
    public String owner;

    @Value("${platform.AG.apiDomaingi}")
    public String apiDomaingi;//注册 转账
    @Value("${platform.AG.apiDomaingci}")
    public String apiDomaingci;//获取游戏URL
    @Value("${platform.AG.cagent}")
    public String cagent;
    @Value("${platform.AG.md5Key}")
    public String md5Key;
    @Value("${platform.AG.desKey}")
    public String desKey;

    @Autowired
    private UserPlatService userPlatService;

    @Override
    public UserPlat register(String userName) throws Exception {
        // 查询是否注册平台
        UserPlat userPlat = userPlatService.getOne(
                new LambdaQueryWrapper<UserPlat>()
                        .eq(UserPlat::getUserName, userName)
                        .eq(UserPlat::getPlatCode, "AG")
        );
        if (userPlat != null) {
            return userPlat;
        }

        // 注册三方
        String apiDomain = this.apiDomaingi + "/doBusiness.do";
        String cagent = this.cagent;
        String loginname = this.owner + userName;
        String method = "lg";
        String actype = "1";
        String password = RandomUtil.randomString(13);
        String oddtype = "A";//20-50000
        String cur = "CNY";
        StringBuilder paramSB = new StringBuilder();
        paramSB.append("cagent=").append(cagent).append("/\\\\\\\\/")
                .append("loginname=").append(loginname).append("/\\\\\\\\/")
                .append("method=").append(method).append("/\\\\\\\\/")
                .append("actype=").append(actype).append("/\\\\\\\\/")
                .append("password=").append(password).append("/\\\\\\\\/")
                .append("oddtype=").append(oddtype).append("/\\\\\\\\/")
                .append("cur=").append(cur);
        String params = DesUtils.DESEncrypt(paramSB.toString(), this.desKey, false);
        String key = DesUtils.MD5(params + this.md5Key).toLowerCase();
        String url = apiDomain + "?params=" + params + "&key=" + key;
        HttpResponse res = HttpUtil.createGet(url).header("User-Agent", "WEB_LIB_GI_" + cagent).timeout(30000).execute();
        String info = getResultInfo(res.body());
        if(!StrUtil.equals(info, "0")){
            log.error("AG 获取游戏URL失败 paramSB= " + paramSB + " result = " + res.body());
            throw new Exception("AG注册失败");
        }

        // 执行注册逻辑
        UserPlat save = new UserPlat();
        save.setUserName(userName);
        save.setPlatCode("AG");
        save.setPlatUserName(loginname);
        save.setPlatUserPassword(password);
        save.setStatus(0);
        save.setCreateTime(new Date());
        userPlatService.save(save);
        return save;
    }

    private String getResultInfo(String xmlStr){
        Document document = XmlUtil.parseXml(xmlStr);
        Element resultEl = XmlUtil.getRootElement(document);
        String info = resultEl.getAttribute("info");
        return info;
    }

    @Override
    public BigDecimal queryBalance(UserPlat userPlat) {
        String apiDomain = this.apiDomaingi + "/doBusiness.do";
        String cagent = this.cagent;
        String loginname = userPlat.getPlatUserName();
        String method = "gb";
        String actype = "1";
        String password = userPlat.getPlatUserPassword();
        String cur = "CNY";
        StringBuilder paramSB = new StringBuilder();
        paramSB.append("cagent=").append(cagent).append("/\\\\\\\\/")
                .append("loginname=").append(loginname).append("/\\\\\\\\/")
                .append("method=").append(method).append("/\\\\\\\\/")
                .append("actype=").append(actype).append("/\\\\\\\\/")
                .append("password=").append(password).append("/\\\\\\\\/")
                .append("cur=").append(cur);
        String params = DesUtils.DESEncrypt(paramSB.toString(), this.desKey, false);
        String key = DesUtils.MD5(params + this.md5Key).toLowerCase();
        String url = apiDomain + "?params=" + params + "&key=" + key;
        HttpResponse res = HttpUtil.createGet(url).header("User-Agent", "WEB_LIB_GI_" + cagent).timeout(30000).execute();
        log.info("AG查询余额返回:{}", res.body());
        String info = getResultInfo(res.body());
        if(NumberUtil.isNumber(info)){
            return new BigDecimal(info);
        }
        log.error("AG 获取用户余额失败 param= " + paramSB + " result = " + res.body());
        return BigDecimal.ZERO;
    }

    @Override
    public String getLoginUrl(PlatLoginUrlBO userPlat) {
        String apiDomain = this.apiDomaingci + "/forwardGame.do";
        String cagent = this.cagent;
        String loginname = userPlat.getPlatUserName();
        String actype = "1";
        String password = userPlat.getPlatUserPassword();
        String dm = "NO_RETURN";
        String sid = cagent + RandomUtil.randomNumbers(13);
        String lang = "1";
        String gameType = userPlat.getGameCode();
        String cur = "CNY";
        StringBuilder paramSB = new StringBuilder();
        paramSB.append("cagent=").append(cagent).append("/\\\\\\\\/")
                .append("loginname=").append(loginname).append("/\\\\\\\\/")
                .append("actype=").append(actype).append("/\\\\\\\\/")
                .append("password=").append(password).append("/\\\\\\\\/")
                .append("dm=").append(dm).append("/\\\\\\\\/")
                .append("sid=").append(sid).append("/\\\\\\\\/")
                .append("lang=").append(lang).append("/\\\\\\\\/")
                .append("gameType=").append(gameType).append("/\\\\\\\\/")
                .append("cur=").append(cur);
        String params = DesUtils.DESEncrypt(paramSB.toString(), this.desKey, false);
        String key = DesUtils.MD5(params + this.md5Key).toLowerCase();
        String url = apiDomain + "?params=" + params + "&key=" + key;
        return url;
    }

    /**
     * 预备转账
     * @param type  IN  OUT
     * @return
     */
    private String prepareTransfreCredit(BigDecimal money, UserPlat userPlat, String platOrderNo, String type){
        String apiDomain = this.apiDomaingi + "/doBusiness.do";
        String cagent = this.cagent;
        String loginname = userPlat.getPlatUserName();
        String method = "tc";
        String billno = platOrderNo;
        BigDecimal credit = money;
        String actype = "1";
        String password = userPlat.getPlatUserPassword();
        String cur = "CNY";
        StringBuilder paramSB = new StringBuilder();
        paramSB.append("cagent=").append(cagent).append("/\\\\\\\\/")
                .append("loginname=").append(loginname).append("/\\\\\\\\/")
                .append("method=").append(method).append("/\\\\\\\\/")
                .append("billno=").append(billno).append("/\\\\\\\\/")
                .append("credit=").append(credit).append("/\\\\\\\\/")
                .append("type=").append(type).append("/\\\\\\\\/")
                .append("actype=").append(actype).append("/\\\\\\\\/")
                .append("password=").append(password).append("/\\\\\\\\/")
                .append("cur=").append(cur);
        String params = DesUtils.DESEncrypt(paramSB.toString(), this.desKey, false);
        String key = DesUtils.MD5(params + this.md5Key).toLowerCase();
        String url = apiDomain + "?params=" + params + "&key=" + key;
        HttpResponse res = HttpUtil.createGet(url).header("User-Agent", "WEB_LIB_GI_" + cagent).timeout(30000).execute();
        String info = getResultInfo(res.body());
        if(StrUtil.equals(info,"0")){
            //成功
            return "1";
        }
        log.error("AG预备转帐失败 paramSB = {} result = {}",paramSB, res.body());
        return "0";
    }

    /**
     * 转账确认
     * @param type  IN  OUT
     * @return
     */
    private boolean transferCreditConfirm(BigDecimal money, UserPlat userPlat, String platOrderNo,String type, String flag){
        String apiDomain = this.apiDomaingi + "/doBusiness.do";
        String cagent = this.cagent;
        String loginname = userPlat.getPlatUserName();
        String method = "tcc";
        String billno = platOrderNo;
        BigDecimal credit = money;
        String actype = "1";
        String password = userPlat.getPlatUserPassword();
        String cur = "CNY";
        StringBuilder paramSB = new StringBuilder();
        paramSB.append("cagent=").append(cagent).append("/\\\\\\\\/")
                .append("loginname=").append(loginname).append("/\\\\\\\\/")
                .append("method=").append(method).append("/\\\\\\\\/")
                .append("billno=").append(billno).append("/\\\\\\\\/")
                .append("credit=").append(credit).append("/\\\\\\\\/")
                .append("type=").append(type).append("/\\\\\\\\/")
                .append("actype=").append(actype).append("/\\\\\\\\/")
                .append("flag=").append(flag).append("/\\\\\\\\/")
                .append("password=").append(password).append("/\\\\\\\\/")
                .append("cur=").append(cur);
        String params = DesUtils.DESEncrypt(paramSB.toString(), this.desKey, false);
        String key = DesUtils.MD5(params + this.md5Key).toLowerCase();
        String url = apiDomain + "?params=" + params + "&key=" + key;
        HttpResponse res = HttpUtil.createGet(url).header("User-Agent", "WEB_LIB_GI_" + cagent).timeout(30000).execute();
        String info = getResultInfo(res.body());
        if(StrUtil.equals(info,"0")){
            //成功
            return true;
        }
        log.error("AG转账确认失败 paramSB = {} result = {}",paramSB, res.body());
        return false;
    }

    /**
     * 查询订单状态
     * @param
     * @return
     */
    private boolean queryOrderStatus(String platOrderNo){
        String apiDomain = this.apiDomaingi + "/doBusiness.do";
        String cagent = this.cagent;
        String billno = platOrderNo;
        String method = "qos";
        String actype = "1";
        String cur = "CNY";
        StringBuilder paramSB = new StringBuilder();
        paramSB.append("cagent=").append(cagent).append("/\\\\\\\\/")
                .append("method=").append(method).append("/\\\\\\\\/")
                .append("billno=").append(billno).append("/\\\\\\\\/")
                .append("actype=").append(actype).append("/\\\\\\\\/")
                .append("cur=").append(cur);
        String params = DesUtils.DESEncrypt(paramSB.toString(), this.desKey, false);
        String key = DesUtils.MD5(params + this.md5Key).toLowerCase();
        String url = apiDomain + "?params=" + params + "&key=" + key;
        HttpResponse res = HttpUtil.createGet(url).header("User-Agent", "WEB_LIB_GI_" + cagent).timeout(30000).execute();
        String info = getResultInfo(res.body());
        if(StrUtil.equals(info,"0")){
            //成功
            return true;
        }
        log.error("AG查询订单失败 paramSB = {} result = {}",paramSB, res.body());
        return false;
    }


    @Override
    public boolean deposit(BigDecimal money, UserPlat userPlat, String platOrderNo) throws Exception {
        //先调用预备转账
        String flag = prepareTransfreCredit(money, userPlat, platOrderNo,"IN");
        //确认转账
        boolean result1 = transferCreditConfirm(money, userPlat, platOrderNo,"IN", flag);
        //查询订单
        boolean result2 = queryOrderStatus(platOrderNo);
        return result1 && result2;
    }

    @Override
    public boolean withdraw(BigDecimal amount, UserPlat userPlat, String platOrderNo) throws Exception {
        //先调用预备转账
        String flag = prepareTransfreCredit(amount, userPlat, platOrderNo,"OUT");
        //确认转账
        boolean result1 = transferCreditConfirm(amount, userPlat, platOrderNo,"OUT", flag);
        //查询订单
        boolean result2 = queryOrderStatus(platOrderNo);
        return result1 && result2;
    }

    @Override
    public String getDepositOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        return this.cagent + "_IN_" + RandomUtil.randomNumbers(13);
    }

    @Override
    public String getWithdrawOrderNo(BigDecimal amount, UserPlat userPlat) throws Exception {
        return this.cagent + "_OUT_" + RandomUtil.randomNumbers(13);
    }

    @Override
    public boolean logout(UserPlat userPlat) {
        //AG无此接口
        return true;
    }
}
