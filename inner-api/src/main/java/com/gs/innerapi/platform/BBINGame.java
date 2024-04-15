package com.gs.innerapi.platform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gs.commons.utils.AesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.Date;
import java.util.TimeZone;

/**
 * BBIN
 */
@Slf4j
public class BBINGame {

    @Value("${platform.BBIN.website}")
    public String website = "overspeed";
    @Value("${platform.BBIN.uppername}")
    public String uppername = "dzf946cny";
    @Value("${platform.BBIN.apiDomain}")
    public String apiDomain = "http://linkapi.kniddk22.com/app/WebService/JSON/display.php";

    /**
     * 创建用户session
     * @return
     */
    public String createSession(){
        //获得URL前需使用此接口获取用户session
        String apiUrl = this.apiDomain + "/CreateSession";
        String username = "GSBBINTESTACCOUNT1";
        String website = this.website;
        String uppername = this.uppername;
        String lang = "zh-cn";
        String keyB = "sE3jbKtn";
        String strA = RandomUtil.randomString(4);
        DateTime now = DateUtil.date();
        DateTime mdTime = now.setTimeZone(TimeZone.getTimeZone("GMT-4"));
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
        if(data != null && StrUtil.isNotBlank(data.getString("sessionid"))){
            return data.getString("sessionid");
        }
        log.error("BBIN用户" + username + "获取sessionid失败  result=" + result);
        return "";
    }

    /**
     * 转入
     * @return
     */
    public String transferIn(){
        //获得URL前需使用此接口获取用户session
        String apiUrl = this.apiDomain + "/Transfer";
        String website = this.website;
        String username = "GSBBINTESTACCOUNT1";
        String uppername = this.uppername;
        String remitno = RandomUtil.randomNumbers(11);
        String action = "IN";
        BigDecimal remit = new BigDecimal("1000");
        String keyB = "upVqp41";
        String strA = RandomUtil.randomString(6);
        DateTime now = DateUtil.date();
        DateTime mdTime = now.setTimeZone(TimeZone.getTimeZone("GMT-4"));
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
            return "OK";
        }
        return "FAIL";
    }

    /**
     * 转出
     * @return
     */
    public String transferOut(){
        //获得URL前需使用此接口获取用户session
        String apiUrl = this.apiDomain + "/Transfer";
        String website = this.website;
        String username = "GSBBINTESTACCOUNT1";
        String uppername = this.uppername;
        String remitno = RandomUtil.randomNumbers(11);
        System.out.println(remitno);
        String action = "OUT";
        BigDecimal remit = new BigDecimal("10");
        String keyB = "upVqp41";
        String strA = RandomUtil.randomString(6);
        DateTime now = DateUtil.date();
        DateTime mdTime = now.setTimeZone(TimeZone.getTimeZone("GMT-4"));
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
            return "OK";
        }
        return "FAIL";
    }

    /**
     * 转账确认
     * @return
     */
    public String checkTransfer(){
        String apiUrl = this.apiDomain + "/CheckTransfer";
        String website = this.website;
        String transid = "38492040105";
        String keyB = "oOCi3";
        String strA = RandomUtil.randomString(5);
        DateTime now = DateUtil.date();
        DateTime mdTime = now.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String strB = AesUtils.MD5(website + keyB + DateUtil.format(mdTime, "yyyyMMdd"));
        String strC = RandomUtil.randomString(8);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("transid", transid);
        param.put("key", key.toLowerCase());
        String result = HttpUtil.post(apiUrl, param);
        log.info("BBIN checkTransfer param= " + param + " result = " + result);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONObject data = resJson.getJSONObject("data");
        if(data != null){
            int status = data.getIntValue("Status");
            if(status == 1){
                //该订单转账成功
                return "OK";
            } else if (status == -1) {
                return "FAIL";
            } else if (status == -2) {
                return "处理中";
            }
        }
        return "处理中";
    }

    /**
     * 踢出用户
     * @return
     */
    public String logout(){
        String apiUrl = this.apiDomain + "/Logout";
        String website = this.website;
        String username = "GSBBINTESTACCOUNT1";
        String strA = RandomUtil.randomString(7);
        DateTime now = DateUtil.date();
        DateTime mdTime = now.setTimeZone(TimeZone.getTimeZone("GMT-4"));
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
            return "OK";
        }
        return "FAIL";
    }

    /**
     * 查询余额
     * @return
     */
    public BigDecimal checkUsrBalance(){
        String apiUrl = this.apiDomain + "/CheckUsrBalance";
        String website = this.website;
        String username = "GSBBINTESTACCOUNT1";
        String uppername = this.uppername;
        String keyB = "bHqo11";
        String strA = RandomUtil.randomString(7);
        DateTime now = DateUtil.date();
        DateTime mdTime = now.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String strB = AesUtils.MD5(website + username + keyB + DateUtil.format(mdTime, "yyyyMMdd"));
        String strC = RandomUtil.randomString(3);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("username", username);
        param.put("uppername", uppername);
        param.put("key", key.toLowerCase());
        String result = HttpUtil.post(apiUrl, param);
        log.info("BBIN checkUsrBalance param= " + param + " result = " + result);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONArray data = resJson.getJSONArray("data");
        if(CollUtil.isNotEmpty(data)){
            JSONObject bobj = (JSONObject) data.get(0);
            return bobj.getBigDecimal("Balance");
        }
        return BigDecimal.ZERO;
    }

    /**
     * 获取视讯链接
     * @return
     */
    public String loginLive(){
        //获得URL前需使用此接口获取用户session
        String apiUrl = this.apiDomain + "/GameUrlBy3";
        String website = this.website;
        String sessionid = createSession();
        if(StrUtil.isBlank(sessionid)){
            return "获取sessionid失败";
        }
        String lang = "zh-cn";
        String tag = "global";
        String keyB = "2dQX6z";
        String strA = RandomUtil.randomString(5);
        DateTime now = DateUtil.date();
        DateTime mdTime = now.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String strB = AesUtils.MD5(website + keyB + DateUtil.format(mdTime, "yyyyMMdd"));
        String strC = RandomUtil.randomString(6);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("lang", lang);
        param.put("sessionid", sessionid);
        param.put("tag", tag);
        param.put("key", key.toLowerCase());
        String result = HttpUtil.post(apiUrl, param);
        log.info("BBIN loginLive param= " + param + " result = " + result);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONArray data = resJson.getJSONArray("data");
        if(CollUtil.isNotEmpty(data)){
            JSONObject urlObj = (JSONObject) data.get(0);
            return urlObj.getString("rwd");
        }
        return "";
    }

    /**
     * 获取电子游戏链接
     * @return
     */
    public String loginEleGame(){
        //获得URL前需使用此接口获取用户session
        String apiUrl = this.apiDomain + "/GameUrlBy5";
        String website = this.website;
        String sessionid = createSession();
        String gametype = "5902";//游戏ID
        String exit_option = "1";
        if(StrUtil.isBlank(sessionid)){
            return "获取sessionid失败";
        }
        String lang = "zh-cn";
        String keyB = "2dQX6z";
        String strA = RandomUtil.randomString(5);
        DateTime now = DateUtil.date();
        DateTime mdTime = now.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String strB = AesUtils.MD5(website + keyB + DateUtil.format(mdTime, "yyyyMMdd"));
        String strC = RandomUtil.randomString(6);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("lang", lang);
        param.put("sessionid", sessionid);
        param.put("gametype", gametype);
        param.put("exit_option", exit_option);
        param.put("key", key.toLowerCase());
        String result = HttpUtil.post(apiUrl, param);
        log.info("BBIN loginEleGame param= " + param + " result = " + result);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONArray data = resJson.getJSONArray("data");
        if(CollUtil.isNotEmpty(data)){
            JSONObject urlObj = (JSONObject) data.get(0);
            return urlObj.getString("html5");
        }
        return "";
    }

    /**
     * 获取捕鱼大师链接
     * @return
     */
    public String loginFish(){
        //获得URL前需使用此接口获取用户session
        String apiUrl = this.apiDomain + "/GameUrlBy38";
        String website = this.website;
        String sessionid = createSession();
        String gametype = "38001";//38001-捕鱼大师    38002-富贵渔场
        String exit_option = "1";
        if(StrUtil.isBlank(sessionid)){
            return "获取sessionid失败";
        }
        String lang = "zh-cn";
        String keyB = "2dQX6z";
        String strA = RandomUtil.randomString(5);
        DateTime now = DateUtil.date();
        DateTime mdTime = now.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String strB = AesUtils.MD5(website + keyB + DateUtil.format(mdTime, "yyyyMMdd"));
        String strC = RandomUtil.randomString(6);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("lang", lang);
        param.put("sessionid", sessionid);
        param.put("gametype", gametype);
        param.put("exit_option", exit_option);
        param.put("key", key.toLowerCase());
        String result = HttpUtil.post(apiUrl, param);
        log.info("BBIN loginFish param= " + param + " result = " + result);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONArray data = resJson.getJSONArray("data");
        if(CollUtil.isNotEmpty(data)){
            JSONObject urlObj = (JSONObject) data.get(0);
            return urlObj.getString("html5");
        }
        return "";
    }

    /**
     * 拉取视讯注单
     * @return
     */
    public String getLiveRecord(){
        //获得URL前需使用此接口获取用户session
        String apiUrl = this.apiDomain + "/WagersRecordBy3";
        String website = this.website;
        String action = "ModifiedTime";
        String uppername = this.uppername;
        String keyB = "U5fBm";
        String strA = RandomUtil.randomString(3);
        DateTime now = DateUtil.date();
        DateTime mdTime = now.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        DateTime endDate = DateUtil.offsetMinute(mdTime, -2);
        DateTime startDate = DateUtil.offsetMinute(mdTime, -7);
        String date = DateUtil.format(endDate, "yyyy-MM-dd");
        String endtime = DateUtil.format(endDate, "HH:mm:ss");//当action为ModifiedTime，无法捞取最近2分钟内的下注记录。
        String starttime = DateUtil.format(startDate, "HH:mm:ss");//当action为ModifiedTime，是捞取带入的区间内被异动的纪录(区间限定5分钟且无法捞取7天前)。
        String strB = AesUtils.MD5(website + keyB + DateUtil.format(mdTime, "yyyyMMdd"));
        String strC = RandomUtil.randomString(7);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("action", action);
        param.put("uppername", uppername);
        param.put("date", date);
        param.put("starttime", starttime);
        param.put("endtime", endtime);
        param.put("key", key.toLowerCase());
        String result = HttpUtil.post(apiUrl, param);
        System.out.println(result);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONArray data = resJson.getJSONArray("data");
        if(CollUtil.isNotEmpty(data)){
            for (Object obj : data) {
                JSONObject recordJson = (JSONObject) obj;
//                    "UserName": "gsbbintestaccount1", 账号
//                    "WagersID": "521486770945",  账单号(三方)
//                    "WagersDate": "2024-04-05 01:58:28", 下注时间(美东)
//                    "SerialID": "518810511", 局号
//                    "RoundNo": "3-41",  场次
//                    "GameType": "3001",  游戏种类
//                    "WagerDetail": "2,1:1,20.00,-20.00",  玩法
//                    "GameCode": "83",  桌号
//                    "Result": "L",  注单结果(C:注销,X:未结算,W:赢,L:输,D:和局)
//                    "Card": "C.1,D.9,C.8*H.10,D.10,C.7",  结果牌
//                    "BetAmount": "20.00",  下注金额
//                    "Origin": "P",
//                    "Client": "3",
//                    "Portal": "0",
//                    "Commissionable": "20.00",  会员有效投注额
//                    "Payoff": "-20.0000",  派彩金额(不包含本金)
//                    "Currency": "RMB",
//                    "ExchangeRate": "1.000000",
//                    "ResultType": "8,7",
//                    "ModifiedDate": "2024-04-05 01:58:58"   注单变更时间
            }
        }
        return "";
    }

    /**
     * 拉取电子游戏注单
     * @return
     */
    public String getEleGameRecord(){
        //获得URL前需使用此接口获取用户session
        //(需强制带入，值:1、2、3、5)
        JSONArray data1 = eleGameRecordApi("1");
        JSONArray data2 = eleGameRecordApi("2");
        JSONArray data3 = eleGameRecordApi("3");
        JSONArray data5 = eleGameRecordApi("5");
        data1.addAll(data2);
        data1.addAll(data3);
        data1.addAll(data5);
        //需要将这几个结果集拼接成一个
        if(CollUtil.isNotEmpty(data1)){
            for (Object obj : data1) {
                JSONObject recordJson = (JSONObject) obj;
//                     "UserName": "gsbbintestaccount1",
        //            "WagersID": "5056136127428",  单号
        //            "WagersDate": "2024-04-05 03:03:02", 下注时间
        //            "GameType": "5094",  游戏代码
        //            "Result": "L",   注单结果(C:注销,X:未结算,W:赢,L:输)
        //            "BetAmount": "5.4000",  下注金额
        //            "Commissionable": "5.39784000",  会员有效投注额
        //            "Payoff": "-5.4000",  派彩金额(不包含本金)
        //            "Currency": "RMB",
        //            "ExchangeRate": "1.000000",
        //            "ModifiedDate": "2024-04-05 03:03:06",
        //            "Origin": "P",
        //            "Client": "3",
        //            "Portal": "0",
        //            "Star":
            }
        }
        return "";
    }

    /**
     * (需强制带入，值:1、2、3、5)
     * @param subgamekind
     * @return
     */
    public JSONArray eleGameRecordApi(String subgamekind){
        //获得URL前需使用此接口获取用户session
        String apiUrl = this.apiDomain + "/WagersRecordBy5";
        String website = this.website;
        String action = "ModifiedTime";
        String uppername = this.uppername;
        String keyB = "U5fBm";
        String strA = RandomUtil.randomString(3);
        Date nowMd = DateUtil.offsetHour(DateUtil.date(), -12);
        String date = DateUtil.format(nowMd, "yyyy-MM-dd");//这里的date参数必须用endTime 日期串
        String endtime = DateUtil.format(DateUtil.offsetMinute(nowMd, -12), "HH:mm:ss");//当action为ModifiedTime，无法捞取最近2分钟内的下注记录。
        String starttime = DateUtil.format(DateUtil.offsetMinute(nowMd, -17), "HH:mm:ss");//当action为ModifiedTime，是捞取带入的区间内被异动的纪录(区间限定5分钟且无法捞取7天前)。

        String strB = AesUtils.MD5(website + keyB + DateUtil.format(nowMd, "yyyyMMdd"));
        String strC = RandomUtil.randomString(7);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("action", action);
        param.put("uppername", uppername);
        param.put("date", date);
        param.put("starttime", starttime);
        param.put("endtime", endtime);
        param.put("subgamekind", subgamekind);
        param.put("key", key.toLowerCase());
        String result = HttpUtil.post(apiUrl, param);
        System.out.println(subgamekind + "===" + result);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONArray data = resJson.getJSONArray("data");
        return CollUtil.isNotEmpty(data) ? data : new JSONArray();
    }

    /**
     * 拉取捕鱼大师注单
     * @return
     */
    public String getFishRecord(){
        //获得URL前需使用此接口获取用户session
        String apiUrl = this.apiDomain + "/WagersRecordBy38";
        String website = this.website;
        String action = "ModifiedTime";
        String uppername = this.uppername;
        String keyB = "U5fBm";
        String strA = RandomUtil.randomString(3);
        DateTime now = DateUtil.date();
        DateTime mdTime = now.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        DateTime endDate = DateUtil.offsetMinute(mdTime, -2);
        DateTime startDate = DateUtil.offsetMinute(mdTime, -7);
        String date = DateUtil.format(endDate, "yyyy-MM-dd");
        String endtime = DateUtil.format(endDate, "HH:mm:ss");//当action为ModifiedTime，无法捞取最近2分钟内的下注记录。
        String starttime = DateUtil.format(startDate, "HH:mm:ss");//当action为ModifiedTime，是捞取带入的区间内被异动的纪录(区间限定5分钟且无法捞取7天前)。
        String strB = AesUtils.MD5(website + keyB + DateUtil.format(mdTime, "yyyyMMdd"));
        String strC = RandomUtil.randomString(7);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("action", action);
        param.put("uppername", uppername);
        param.put("date", date);
        param.put("starttime", starttime);
        param.put("endtime", endtime);
        param.put("key", key.toLowerCase());
        String result = HttpUtil.post(apiUrl, param);
        System.out.println("getFishRecord result = " + result);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONArray data = resJson.getJSONArray("data");
        if(CollUtil.isNotEmpty(data)){
            for (Object obj : data) {
                JSONObject recordJson = (JSONObject) obj;
//                    "UserName": "gsbbintestaccount1", 账号
//                    "WagersID": "521486770945",  账单号(三方)
//                    "WagersDate": "2024-04-05 01:58:28", 下注时间(美东)
//                    "SerialID": "518810511", 局号
//                    "RoundNo": "3-41",  场次
//                    "GameType": "3001",  游戏种类
//                    "WagerDetail": "2,1:1,20.00,-20.00",  玩法
//                    "GameCode": "83",  桌号
//                    "Result": "L",  注单结果(C:注销,W:赢,L:输)
//                    "Card": "C.1,D.9,C.8*H.10,D.10,C.7",  结果牌
//                    "BetAmount": "20.00",  下注金额
//                    "Origin": "P",
//                    "Client": "3",
//                    "Portal": "0",
//                    "Commissionable": "20.00",  会员有效投注额
//                    "Payoff": "-20.0000",  派彩金额(不包含本金)
//                    "Currency": "RMB",
//                    "ExchangeRate": "1.000000",
//                    "ResultType": "8,7",
//                    "ModifiedDate": "2024-04-05 01:58:58"   注单变更时间
            }
        }
        log.error("BBIN 拉取捕鱼大师注单失败 param= " + param + " result = " + result);
        return "";
    }

//    public static void main(String[] args) {
//        BBINGame bg = new BBINGame();
//        System.out.println(bg.getFishRecord());
//    }

}
