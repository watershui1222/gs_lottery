package com.gs.api.platform;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gs.commons.utils.AesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 乐游
 */
@Slf4j
public class LeYouGame {

    @Value("${platform.LeYou.apiDomain}")
    public String apiDomain = "https://wc1-api.kewmn686.com/channelHandle";
    @Value("${platform.LeYou.agent}")
    public String agent = "201373";
    @Value("${platform.LeYou.aesKey}")
    public String aesKey = "4686951BAA2E5234";
    @Value("${platform.LeYou.md5Key}")
    public String md5Key = "D08C49D1629ECF26";
    @Value("${platform.LeYou.betRecordDomain}")
    public String betRecordDomain = "https://wc1-record.kewmn686.com/getRecordHandle";

    /**
     * 登录
     * @return
     * @throws Exception
     */
    public String login(){
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = "GSTestAccount1";
        String orderid = agent + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + account;
        String lineCode = "GSLY";
        String kindId = "220";
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
        String key = AesUtils.MD5(agent+timestamp+this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.apiDomain).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        log.info("乐游 login url= " + urlSB + " result = " + result);
        JSONObject resJSON = JSONObject.parseObject(result);
        JSONObject d = resJSON.getJSONObject("d");
        if(d.getIntValue("code") == 0){
            return d.getString("url");
        }
        return "";
    }

    /**
     * 查询余额
     * @return
     * @throws Exception
     */
    public BigDecimal queryBalance(){
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = "GSTestAccount1";
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=7&").append("account=").append(account);
        String param = AesUtils.AESEncrypt(paramSb.toString(), aesKey,true);
        String key = AesUtils.MD5(agent+timestamp+this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.apiDomain).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        log.info("乐游 queryBalance param= " + param + " result = " + result);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONObject d = resJson.getJSONObject("d");
        if(d.getIntValue("code") == 0){
            return d.getBigDecimal("freeMoney");
        }
        return BigDecimal.ZERO;
    }

    /**
     * 上分
     * @return
     */
    public String chargePoints(){
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = "GSTestAccount1";
        BigDecimal money = new BigDecimal("200");
        String orderid = agent + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + account;
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=2&")
                .append("account=").append(account)
                .append("&money=").append(money)
                .append("&orderid=").append(orderid);
        String param = AesUtils.AESEncrypt(paramSb.toString(), aesKey,true);
        String key = AesUtils.MD5(agent+timestamp+this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.apiDomain).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        log.info("乐游 chargePoints param= " + param + " result = " + result);
        JSONObject res = JSONObject.parseObject(result);
        JSONObject d = res.getJSONObject("d");
        if(d.getIntValue("code") == 0){
            return "OK";//转账成功
        }else{
            //需要调用订单确认接口
            return "needcheck";
        }
    }

    /**
     * 下分
     */
    public String refund(){
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = "GSTestAccount1";
        BigDecimal money = new BigDecimal("10");//这里如果要全部带出需要查询一遍
        String orderid = agent + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + account;
        System.out.println(orderid);
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
        log.info("乐游 refund param= " + param + " result = " + result);
        JSONObject res = JSONObject.parseObject(result);
        JSONObject d = res.getJSONObject("d");
        if(d.getIntValue("code") == 0){
            return "OK";//转账成功
        }else{
            //需要调用订单确认接口
            return "needcheck";
        }
    }

    /**
     * 查询订单
     * @return
     * @throws Exception
     */
    public int orderQuery(){
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = "GSTestAccount1";
        String orderid = "20137320240404191635549GSTestAccount1";
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=4&").append("&orderid=").append(orderid);
        String param = AesUtils.AESEncrypt(paramSb.toString(), aesKey,true);
        String key = AesUtils.MD5(agent + timestamp + this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.apiDomain).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        log.info("乐游 orderQuery param= " + param + " result = " + result);
        JSONObject jsonResult = JSONObject.parseObject(result);
        JSONObject d = jsonResult.getJSONObject("d");
        int code = d.getIntValue("code");
        if(code == 0){
            int status = d.getIntValue("status");//（-1:不存在、0:成功、2:失败、3:处理中）
            return status;
        }
        return -1;
    }

    /**
     * 踢玩家下线KY
     * @return
     * @throws Exception
     */
    public String kickOff(){
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = "GSTestAccount1";
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=8&").append("account=").append(account);
        String param = AesUtils.AESEncrypt(paramSb.toString(), aesKey,true);
        String key = AesUtils.MD5(agent+timestamp+this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.apiDomain).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        log.info("乐游 kickOff param= " + param + " result = " + result);
        return "OK";
    }

    /**
     * 拉取注单
     * @return
     */
    public String getRecord(){
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        Date now = new Date();
        Long endTime = DateUtil.offsetMinute(now, 0).getTime();
        Long startTime = DateUtil.offsetMinute(now, -5).getTime();
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=6&")
                .append("startTime=").append(startTime)
                .append("&endTime=").append(endTime);
        String param = AesUtils.AESEncrypt(paramSb.toString(), aesKey,true);
        String key = AesUtils.MD5(agent+timestamp+this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.betRecordDomain).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        JSONObject jsonResult = JSONObject.parseObject(result);
        JSONObject d = jsonResult.getJSONObject("d");
        int code = d.getIntValue("code");
        if(code == 0){
            JSONObject list = d.getJSONObject("list");
            int count = d.getIntValue("count");
            JSONArray gameIds = list.getJSONArray("GameID");
            JSONArray accounts = list.getJSONArray("Accounts");
            JSONArray serverID = list.getJSONArray("ServerID");//游戏房间代号
            JSONArray kindID = list.getJSONArray("KindID");//游戏代号
            JSONArray cellScore = list.getJSONArray("CellScore");//有效投注额
            JSONArray allBet = list.getJSONArray("AllBet");//总投注额
            JSONArray profit = list.getJSONArray("Profit");//输赢⾦额
            JSONArray revenue = list.getJSONArray("Revenue"); //反⽔⾦额
            JSONArray gameStartTime = list.getJSONArray("GameStartTime");//游戏开始时间
            JSONArray gameEndTime = list.getJSONArray("GameEndTime");//游戏结束时间
//            List<>
            for (int i = 0; i < count; i++){
                gameIds.getString(i);//封装进本项目三方主单类
                profit.getBigDecimal(i);
                System.out.println(gameIds.getString(i));
                System.out.println(profit.getBigDecimal(i));
            }
        }
        return "OK";
    }


    public static void main(String[] args){
        LeYouGame ly = new LeYouGame();
        System.out.println(ly.login());
    }

}
