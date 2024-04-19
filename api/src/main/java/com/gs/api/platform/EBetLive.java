package com.gs.api.platform;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.gs.business.utils.plat.SignUtils;
import com.gs.commons.utils.AesUtils;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EBetLive {

    @Value("${platform.EBet.apiDomain}")
    public String apiDomain = "https://uat-nc-ugs-ebetapi.ufweg.com";
    @Value("${platform.EBet.channelId}")
    public String channelId = "971";
    @Value("${platform.EBet.publicKey}")
    public String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAOPuF1FxpmHQiqT8m5hueTv1K9EDTLlw" +
            "CTzQ4M3udqk+3oJhCg1jzFRavtOROlNwGoTxjjsg+fytySTJ0xxFJKECAwEAAQ==";
    @Value("${platform.EBet.privateKey}")
    public String privateKey = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEA4+4XUXGmYdCKpPyb" +
            "mG55O/Ur0QNMuXAJPNDgze52qT7egmEKDWPMVFq+05E6U3AahPGOOyD5/K3JJMnT" +
            "HEUkoQIDAQABAkEAkFiYK9vtosSPCS1w3HgaDv6VYSjVzhpFv14JAGGkhEk6UEi/" +
            "Vc5b+p016+iygmL9ORGXLA4Y3Tly8CM3LVKqgQIhAPgSusBteehhY6/Oc1Ef9r5u" +
            "Q274LH2b27X4DVgQvPpjAiEA6zaWmS1ZfzsM3Z59DqJ3MOMSr1zHdAS6rz34CzoN" +
            "cisCIQCDRaX9XfE8vwx5Y84yB7ASZVbZygoBDhDd4j1tdK4L8QIgAmITNIoZFgjr" +
            "IHOyoKtUudITJmOyQaKqoYvnL3XvHPECIQDpEG9sRo0H6TqiHWifttCOhUjykWTz" +
            "ldsJTtB2eFVILA==";

    public boolean createUser(){
        String channelId = this.channelId;
        String username = "gsTestAccount2";
        String signature = SignUtils.eBetSign(username, this.privateKey, this.publicKey);
        JSONObject param = new JSONObject();
        param.put("channelId", channelId);
        param.put("username", username);
        param.put("signature", signature);
        String apiUrl = this.apiDomain + "/api/syncuser";
        String result = apiRequest(apiUrl, param);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        if(res.getInteger("status") == 200){
            return true;
        }
        return false;
    }

    private String apiRequest(String apiUrl, JSONObject param) {
        return HttpUtil.createPost(apiUrl).header("Content-Type" , "application/json").body(param.toJSONString()).timeout(10000).execute().body();
    }

    /**
     * 正数转入  负数转出
     * @param amount
     * @return
     */
    public boolean transfer(BigDecimal amount){
        String channelId = this.channelId;
        String username = "gsTestAccount1";
        Long timestamp = DateUtil.current();
        String signature = SignUtils.eBetSign(username + timestamp, this.privateKey, this.publicKey);
        String rechargeReqId = "gsTestTransfer3";
        JSONObject param = new JSONObject();
        param.put("channelId", channelId);
        param.put("username", username);
        param.put("timestamp", timestamp);
        param.put("signature", signature);
        param.put("money", amount);
        param.put("rechargeReqId", rechargeReqId);
        String apiUrl = this.apiDomain + "/api/recharge";
        String result = apiRequest(apiUrl, param);
        JSONObject res = JSONObject.parseObject(result);
        if(res.getInteger("status") == 200){
            //不管成功与否都必须请求确认接口
            return checkTransfer(rechargeReqId);
        }
        return false;
    }

    public boolean checkTransfer(String rechargeReqId){
        String channelId = this.channelId;
        String signature = SignUtils.eBetSign(rechargeReqId, this.privateKey, this.publicKey);
        JSONObject param = new JSONObject();
        param.put("channelId", channelId);
        param.put("signature", signature);
        param.put("rechargeReqId", rechargeReqId);
        String apiUrl = this.apiDomain + "/api/rechargestatus";
        String result = apiRequest(apiUrl, param);
        JSONObject res = JSONObject.parseObject(result);
        if(res.getInteger("status") == 200){
            return true;
        }
        return false;
    }

    public boolean transferIn(){
        BigDecimal amount = new BigDecimal("152.12");
        return transfer(amount);
    }

    public boolean transferOut(){
        BigDecimal amount = new BigDecimal("-5.16");
        return transfer(amount);
    }

    public BigDecimal getBalance(){
        BigDecimal balance = BigDecimal.ZERO;
        String channelId = this.channelId;
        String username = "gsTestAccount1";
        String signature = SignUtils.eBetSign(username, this.privateKey, this.publicKey);
        JSONObject param = new JSONObject();
        param.put("channelId", channelId);
        param.put("username", username);
        param.put("signature", signature);
        String apiUrl = this.apiDomain + "/api/userinfo";
        String result = apiRequest(apiUrl, param);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        if(res.getInteger("status") == 200){
            balance = res.getBigDecimal("money");
        }
        return balance;
    }

    public String launchUrl(){
        String channelId = this.channelId;
        Long timestamp = DateUtil.current();
        String signature = SignUtils.eBetSign(channelId + timestamp, this.privateKey, this.publicKey);
        String currency = "CNY";
        JSONObject param = new JSONObject();
        param.put("channelId", channelId);
        param.put("timestamp", timestamp);
        param.put("signature", signature);
        param.put("currency", currency);
        param.put("china", 1);
        String apiUrl = this.apiDomain + "/api/launchUrl";
        String result = apiRequest(apiUrl, param);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        StringBuilder url = new StringBuilder();
        if(res.getInteger("status") == 200){
            String username = "gsTestAccount1";
            String accessToken = RandomUtil.randomString(11);
//            ?username=testmember&accessToken=testaccesstoken
            url.append(res.getString("launchUrl"))
                    .append("?username=").append(username)
                    .append("&accessToken=").append(accessToken);
        }
        return url.toString();
    }

    public boolean kickOff(){
        String channelId = this.channelId;
        String username = "gsTestAccount1";
        Long timestamp = DateUtil.current();
        String signature = SignUtils.eBetSign(username + channelId + timestamp, this.privateKey, this.publicKey);
        JSONObject param = new JSONObject();
        param.put("username", username);
        param.put("channelId", channelId);
        param.put("timestamp", timestamp);
        param.put("signature", signature);
        String apiUrl = this.apiDomain + "/api/logout";
        String result = apiRequest(apiUrl, param);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        return res.getInteger("status") == 200;
    }

    public void getRecord(){
        String channelId = this.channelId;
        Long timestamp = DateUtil.current();
        String signature = SignUtils.eBetSign(timestamp.toString(), this.privateKey, this.publicKey);
        Integer betStatus = 1;//0：仅查询失败的记录    1：仅查询成功的记录
        Date endTime = DateUtil.date();
        Date startTime = DateUtil.offsetHour(endTime, -1);
        String endTimeStr = DateUtil.formatDateTime(endTime);
        String startTimeStr = DateUtil.formatDateTime(startTime);
        List<JSONObject> list = new ArrayList<>();
        int pageNum = 1;
        int pageSize = 5000;//最大5000
        int totalPages = 1;

        do{
            JSONObject param = new JSONObject();
            param.put("channelId", channelId);
            param.put("timestamp", timestamp);
            param.put("signature", signature);
            param.put("betStatus", betStatus);
            param.put("pageNum", pageNum);
            param.put("pageSize", pageSize);
            param.put("startTimeStr", startTimeStr);
            param.put("endTimeStr", endTimeStr);
            String apiUrl = this.apiDomain + "/api/userbethistory";
            String result = apiRequest(apiUrl, param);
            System.out.println(result);
            JSONObject res = JSONObject.parseObject(result);
            if(res.getInteger("status") == 200){
                List<JSONObject> betHistories = res.getList("betHistories", JSONObject.class);
                list.addAll(betHistories);
                Integer count = res.getIntValue("count");
                System.out.println("betHistories.size() = " + betHistories.size());
                System.out.println("count = " + count);
                totalPages = (int) Math.ceil((double) count / pageSize);
            }
            pageNum++;
        }while(pageNum <= totalPages);
        System.out.println(list);
    }


    public static void main(String[] args) {
        EBetLive eb = new EBetLive();
//        System.out.println(eb.createUser());
        System.out.println(eb.launchUrl());
    }
}
