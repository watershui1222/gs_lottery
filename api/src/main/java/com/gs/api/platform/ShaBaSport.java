package com.gs.api.platform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 沙巴体育
 */
@Slf4j
public class ShaBaSport {

    @Value("${platform.ShaBa.operatorID}")
    public String operatorID = "ZhiFeng946";
    @Value("${platform.ShaBa.apiUrl}")
    public String apiUrl = "http://c0m1tsa.bw6688.com/api";
    @Value("${platform.ShaBa.vendorID}")
    public String vendorID = "i96zwegizd";
    @Value("${platform.ShaBa.currencyID}")
    public String currencyID = "20";//20=测试货币 13=CNY

    /**
     * 创建账号
     * @return
     */
    public String createMember(){
        String apiUrlStr = this.apiUrl + "/CreateMember";
        String vendor_id = this.vendorID;//厂商标识符
        String vendor_member_id = "GSSBTESTACOUNT1";
        String operatorId = this.operatorID;
        String username = vendor_member_id;
        int oddstype = 2;
        int currency = Integer.valueOf(this.currencyID);
        BigDecimal mintransfer = BigDecimal.ZERO;
        BigDecimal maxtransfer = new BigDecimal("10000000");
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("vendor_member_id", vendor_member_id);
        param.put("operatorId", operatorId);
        param.put("username", username);
        param.put("oddstype", oddstype);
        param.put("currency", currency);
        param.put("mintransfer", mintransfer);
        param.put("maxtransfer", maxtransfer);
        String result = HttpUtil.post(apiUrlStr, param);
        log.info("沙巴 createMember param= " + param + " result = " + result);
        JSONObject resJSON = JSONObject.parseObject(result);
        if(resJSON.getIntValue("error_code") == 0){
            //成功
            return "OK";
        }
        System.out.println(result);
        return "FAIL";
    }

    /**
     * 获得登录链接
     * @return
     */
    public String getSabaUrl(){
        String apiUrlStr = this.apiUrl + "/GetSabaUrl";
        String vendor_id = this.vendorID;//厂商标识符
        String vendor_member_id = "GSSBTESTACOUNT1";
        int platform = 2;//1: 桌机pc  2: 手机 h5
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("vendor_member_id", vendor_member_id);
        param.put("platform", platform);
        String result = HttpUtil.post(apiUrlStr, param);
        log.info("沙巴 getSabaUrl param= " + param + " result = " + result);
        JSONObject resJSON = JSONObject.parseObject(result);
        if(resJSON.getIntValue("error_code") == 0){
            //成功
            String data = resJSON.getString("Data");
            String url = data + "&lang=cs&OType=2";
            return url;
        }
        return "FAIL";
    }

    /**
     * 查询余额
     * @return
     */
    public BigDecimal getBalance(){
        String apiUrlStr = this.apiUrl + "/CheckUserBalance";
        String vendor_id = this.vendorID;//厂商标识符
        String vendor_member_ids = "GSSBTESTACOUNT1";
        int wallet_id = 1;
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("vendor_member_ids", vendor_member_ids);
        param.put("wallet_id", wallet_id);
        String result = HttpUtil.post(apiUrlStr, param);
        log.info("沙巴 getBalance param= " + param + " result = " + result);
        JSONObject resJSON = JSONObject.parseObject(result);
        if(resJSON.getIntValue("error_code") == 0){
            //成功
            JSONArray data = resJSON.getJSONArray("Data");
            if(CollUtil.isNotEmpty(data)){
                JSONObject obj = (JSONObject) data.get(0);
                return obj.getBigDecimal("balance");
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * 额度转入
     * @return
     */
    public String transferIn(){
        String apiUrlStr = this.apiUrl + "/FundTransfer";
        String vendor_id = this.vendorID;//厂商标识符
        String vendor_member_id = "GSSBTESTACOUNT1";
        String vendor_trans_id = "GSSBTESTORDERNO2";//我方单号
        BigDecimal amount = new BigDecimal("1000");
        int currency = Integer.valueOf(this.currencyID);
        int direction = 1;
        int wallet_id = 1;
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("vendor_member_id", vendor_member_id);
        param.put("vendor_trans_id", vendor_trans_id);
        param.put("amount", amount);
        param.put("currency", currency);
        param.put("direction", direction);
        param.put("wallet_id", wallet_id);
        String result = HttpUtil.post(apiUrlStr, param);
        log.info("沙巴 transferIn param= " + param + " result = " + result);
        JSONObject resJSON = JSONObject.parseObject(result);
        int error_code = resJSON.getIntValue("error_code");
        if(error_code == 0){
            //成功
            return "OK";
        }else if(error_code == 1){
            //需确认 status code  1失败  2 代表状态未知，请呼叫 API“CheckFundTransfer”方法确认状态
            JSONObject data = resJSON.getJSONObject("Data");
            int status = data.getIntValue("status");
            if(status == 1){
                return "FAIL";
            }else if(status == 2){
                return "还需调用确认订单接口";
            }
        }
        return "FAIL";
    }

    /**
     * 额度转出
     * @return
     */
    public String transferOut(){
        String apiUrlStr = this.apiUrl + "/FundTransfer";
        String vendor_id = this.vendorID;//厂商标识符
        String vendor_member_id = "GSSBTESTACOUNT1";
        String vendor_trans_id = "GSSBTESTORDERNO3";//我方单号
        BigDecimal amount = new BigDecimal("100");
        int currency = Integer.valueOf(this.currencyID);
        int direction = 0;
        int wallet_id = 1;
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("vendor_member_id", vendor_member_id);
        param.put("vendor_trans_id", vendor_trans_id);
        param.put("amount", amount);
        param.put("currency", currency);
        param.put("direction", direction);
        param.put("wallet_id", wallet_id);
        String result = HttpUtil.post(apiUrlStr, param);
        log.info("沙巴 transferOut param= " + param + " result = " + result);
        JSONObject resJSON = JSONObject.parseObject(result);
        int error_code = resJSON.getIntValue("error_code");
        if(error_code == 0){
            //成功
            return "OK";
        }else if(error_code == 1){
            //需确认 status code  1失败  2 代表状态未知，请呼叫 API“CheckFundTransfer”方法确认状态
            JSONObject data = resJSON.getJSONObject("Data");
            int status = data.getIntValue("status");
            if(status == 1){
                return "FAIL";
            }else if(status == 2){
                return "还需调用确认订单接口";
            }
        }
        return "FAIL";
    }

    /**
     * 转账确认
     * @return
     */
    public String checkFundTransfer(){
        String apiUrlStr = this.apiUrl + "/CheckFundTransfer";
        String vendor_id = this.vendorID;//厂商标识符
        String vendor_trans_id = "GSSBTESTORDERNO2";//我方单号
        int wallet_id = 1;
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("vendor_trans_id", vendor_trans_id);
        param.put("wallet_id", wallet_id);
        String result = HttpUtil.post(apiUrlStr, param);
        log.info("沙巴 checkFundTransfer param= " + param + " result = " + result);
        JSONObject resJSON = JSONObject.parseObject(result);
        int error_code = resJSON.getIntValue("error_code");
        if(error_code == 0){
            JSONObject data = resJSON.getJSONObject("Data");
            int status = data.getIntValue("status");
            if(status == 0){
                //转账成功
                return "OK";
            }else if(status == 1){
                //转账失败
                return "FAIL";
            }
        }else if(error_code == 1){
            JSONObject data = resJSON.getJSONObject("Data");
            int status = data.getIntValue("status");
            if(status == 2){
                return "还需调用确认订单接口";
            }
        }
        return "FAIL";
    }

    /**
     * 踢人
     * @return
     */
    public String kickUser(){
        String apiUrlStr = this.apiUrl + "/KickUser";
        String vendor_id = this.vendorID;//厂商标识符
        String vendor_member_id = "GSSBTESTACOUNT1";//我方单号
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("vendor_member_id", vendor_member_id);
        String result = HttpUtil.post(apiUrlStr, param);
        log.info("沙巴 kickUser param= " + param + " result = " + result);
        JSONObject resJSON = JSONObject.parseObject(result);
        int error_code = resJSON.getIntValue("error_code");
        if(error_code == 0){
            return "OK";
        }
        return "FAIL";
    }

    /**
     * 拉取注单
     * @return
     */
    public String getRecord(){
        String apiUrlStr = this.apiUrl + "/GetBetDetailByTimeframe";
        String vendor_id = this.vendorID;//厂商标识符
        Date yesterday = DateUtil.yesterday();
        String end_date = DateUtil.format(DateUtil.endOfDay(new Date()), "yyyy-MM-dd'T'HH:mm:ss");
        String start_date = DateUtil.format(DateUtil.beginOfDay(new Date()), "yyyy-MM-dd'T'HH:mm:ss");
        int time_type = 2;
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("start_date", start_date);
        param.put("end_date", end_date);
        param.put("time_type", time_type);
        String result = HttpUtil.post(apiUrlStr, param);
        System.out.println(result);
        JSONObject resJSON = JSONObject.parseObject(result);
        int error_code = resJSON.getIntValue("error_code");
        if(error_code == 0){
            JSONObject data = resJSON.getJSONObject("Data");
            JSONArray betDetails = data.getJSONArray("BetDetails");
//                "trans_id": 240152894496571421,
//                    "vendor_member_id": "GSSBTESTACOUNT1",
//                    "operator_id": "ZhiFeng946",
//                    "league_id": 42037,
//                    "leaguename": [
//                {
//                    "lang": "en",
//                        "name": "TESTING (Betting Prohibited Area!!) - Betradar Test - SOCCER"
//                },
//                {
//                    "lang": "cs",
//                        "name": "測試 (Betting Prohibited Area!!) - Betradar Test - SOCCER"
//                }
//                ],
//                "match_id": 83522645,
//                    "home_id": 89063,
//                    "hometeamname": [
//                {
//                    "lang": "en",
//                        "name": "Test E"
//                },
//                {
//                    "lang": "cs",
//                        "name": "试 E"
//                }
//                ],
//                "away_id": 15700,
//                    "awayteamname": [
//                {
//                    "lang": "en",
//                        "name": "Test 1"
//                },
//                {
//                    "lang": "cs",
//                        "name": "试 1"
//                }
//                ],
//                "match_datetime": "2024-04-07T22:29:00",
//                    "sport_type": 1,
//                    "sportname": [
//                {
//                    "lang": "en",
//                        "name": "Soccer"
//                },
//                {
//                    "lang": "cs",
//                        "name": "足球"
//                }
//                ],
//                "bet_type": 1,
//                    "bettypename": [
//                {
//                    "lang": "en",
//                        "name": "Handicap"
//                },
//                {
//                    "lang": "cs",
//                        "name": "让球"
//                }
//                ],
//                "parlay_ref_no": 0,
//                    "odds": 0.98,
//                    "stake": 2,
//                    "transaction_time": "2024-04-07T23:55:53.747",
//                    "ticket_status": "running",
//                    "winlost_amount": -2,
//                    "after_amount": 998,
//                    "currency": 20,
//                    "winlost_datetime": "2024-04-08T00:00:00",
//                    "odds_type": 2,
//                    "bet_team": "h",
//                    "isLucky": "False",
//                    "home_hdp": 1,
//                    "away_hdp": 0,
//                    "hdp": -1,
//                    "betfrom": "W002",
//                    "islive": "1",
//                    "home_score": 0,
//                    "away_score": 0,
//                    "settlement_time": null,
//                    "customInfo1": "",
//                    "customInfo2": "",
//                    "customInfo3": "",
//                    "customInfo4": "",
//                    "customInfo5": "",
//                    "ba_status": "0",
//                    "version_key": 34001,
//                    "ParlayData": null,
//                    "risklevelname": "New",
//                    "risklevelnamecs": "新玩家"
            return "OK";
        }
        return "FAIL";
    }

    public static void main(String[] args) {
        ShaBaSport sb = new ShaBaSport();
        System.out.println(sb.getRecord());
    }
}
