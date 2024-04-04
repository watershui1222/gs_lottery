package com.gs.api.platform;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gs.api.platform.platUtils.KaiYuanUtil;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 开源棋牌
 */
public class KaiYuanGame {

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

    /**
     * 登录
     * @return
     * @throws Exception
     */
    public String login() throws Exception {
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = "GSTestAccount";
        String orderid = agent + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + account;
        String lineCode = "GSKY";
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
        String param = KaiYuanUtil.AESEncrypt(paramSb.toString(), aesKey);
        String key = KaiYuanUtil.MD5(agent+timestamp+this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.prefixURL).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        return result;
    }

    /**
     * 查询余额
     * @return
     * @throws Exception
     */
    public BigDecimal queryBalance() throws Exception {
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = "GSTestAccount";
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=1&").append("account=").append(account);
        String param = KaiYuanUtil.AESEncrypt(paramSb.toString(), aesKey);
        String key = KaiYuanUtil.MD5(agent+timestamp+this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.prefixURL).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        JSONObject jsonResult = JSONObject.parseObject(result);
        JSONObject d = jsonResult.getJSONObject("d");
        BigDecimal money = BigDecimal.ZERO;
        if(d != null){
            money = d.getBigDecimal("money") == null ? BigDecimal.ZERO : d.getBigDecimal("money");
        }
        return money;
    }

    /**
     * 上分
     * @return
     */
    public String chargePoints() throws Exception {
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = "GSTestAccount";
        BigDecimal money = new BigDecimal("20");
        String orderid = agent + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + account;
        System.out.println(orderid);
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=2&")
                .append("account=").append(account)
                .append("&money=").append(money)
                .append("&orderid=").append(orderid);
        String param = KaiYuanUtil.AESEncrypt(paramSb.toString(), aesKey);
        String key = KaiYuanUtil.MD5(agent+timestamp+this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.prefixURL).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        System.out.println(result);
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
    public String refund() throws Exception {
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = "GSTestAccount";
        BigDecimal money = new BigDecimal("100");//这里如果要全部带出需要查询一遍
        String orderid = agent + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + account;
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=3&")
                .append("account=").append(account)
                .append("&money=").append(money)
                .append("&orderid=").append(orderid);
        String param = KaiYuanUtil.AESEncrypt(paramSb.toString(), aesKey);
        String key = KaiYuanUtil.MD5(agent+timestamp+this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.prefixURL).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        System.out.println(result);
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
    public int orderQuery() throws Exception {
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = "GSTestAccount";
        String orderid = "7341920240403172555542GSTestAccount";
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=4&").append("&orderid=").append(orderid);
        String param = KaiYuanUtil.AESEncrypt(paramSb.toString(), aesKey);
        String key = KaiYuanUtil.MD5(agent + timestamp + this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.prefixURL).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        System.out.println(result);
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
     * 提玩家下线KY
     * @return
     * @throws Exception
     */
    public String kickOff() throws Exception {
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String account = "GSTestAccount";
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=8&").append("account=").append(account);
        String param = KaiYuanUtil.AESEncrypt(paramSb.toString(), aesKey);
        String key = KaiYuanUtil.MD5(agent+timestamp+this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.prefixURL).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        System.out.println(result);
        return "OK";
    }

    public String getRecord() throws Exception {
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        Date now = new Date();
        Long endTime = DateUtil.offsetMinute(now, -80).getTime();
        Long startTime = DateUtil.offsetMinute(now, -105).getTime();
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=6&")
                .append("startTime=").append(startTime)
                .append("&endTime=").append(endTime);
        String param = KaiYuanUtil.AESEncrypt(paramSb.toString(), aesKey);
        String key = KaiYuanUtil.MD5(agent+timestamp+this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.recordURL).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        System.out.println(result);
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

    public static void main(String[] args) throws Exception {
        KaiYuanGame k = new KaiYuanGame();
        System.out.println(k.login());
    }
}
