package com.gs.api.platform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 沙巴体育
 */
public class SABA {

    public String operatorID = "ZhiFeng946";
    public String apiUrl = "http://c0m1tsa.bw6688.com/api";
    public String vendorID = "i96zwegizd";
    public int currencyID = 20;//20=测试货币 13=CNY

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
        int currency = this.currencyID;
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
        JSONObject resJSON = JSONObject.parseObject(result);
        if(resJSON.getIntValue("error_code") == 0){
            //成功
            return resJSON.getString("Data");
        }
        System.out.println(result);
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
        JSONObject resJSON = JSONObject.parseObject(result);
        if(resJSON.getIntValue("error_code") == 0){
            //成功
            JSONArray data = resJSON.getJSONArray("Data");
            if(CollUtil.isNotEmpty(data)){
                JSONObject obj = (JSONObject) data.get(0);
                return obj.getBigDecimal("balance");
            }
        }
        System.out.println(result);
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
        String vendor_trans_id = "GSSBTESTORDERNO1";//我方单号
        BigDecimal amount = new BigDecimal("100");
        int currency = this.currencyID;
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
        System.out.println(result);
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
        String vendor_trans_id = "GSSBTESTORDERNO1";//我方单号
        BigDecimal amount = new BigDecimal("100");
        int currency = this.currencyID;
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
        System.out.println(result);
        return "FAIL";
    }

    /**
     * 转账确认
     * @return
     */
    public String checkFundTransfer(){
        String apiUrlStr = this.apiUrl + "/CheckFundTransfer";
        String vendor_id = this.vendorID;//厂商标识符
        String vendor_trans_id = "GSSBTESTORDERNO1";//我方单号
        int wallet_id = 1;
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("vendor_trans_id", vendor_trans_id);
        param.put("wallet_id", wallet_id);
        String result = HttpUtil.post(apiUrlStr, param);
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
        System.out.println(result);
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
        JSONObject resJSON = JSONObject.parseObject(result);
        int error_code = resJSON.getIntValue("error_code");
        if(error_code == 0){
            return "OK";
        }
        System.out.println(result);
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
        String end_date = DateUtil.format(DateUtil.endOfDay(yesterday), "yyyy-MM-dd'T'HH:mm:ss");
        String start_date = DateUtil.format(DateUtil.beginOfDay(yesterday), "yyyy-MM-dd'T'HH:mm:ss");
        int time_type = 2;
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("start_date", start_date);
        param.put("end_date", end_date);
        param.put("time_type", time_type);
        String result = HttpUtil.post(apiUrlStr, param);
        JSONObject resJSON = JSONObject.parseObject(result);
        int error_code = resJSON.getIntValue("error_code");
        if(error_code == 0){
            JSONObject data = resJSON.getJSONObject("data");
            JSONArray betDetails = data.getJSONArray("betDetails");
            return "OK";
        }
        return "FAIL";
    }

    public static void main(String[] args) {
//        SABA sb = new SABA();
//        System.out.println(sb.getSabaUrl());
    }
}
