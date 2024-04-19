package com.gs.api.platform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gs.business.utils.plat.SignUtils;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FBSport {

    @Value("${platform.FB.apiDomain}")
    private String apiDomain = "https://sptapi.server.st-newsports.com";
    @Value("${platform.FB.channelId}")
    private String channelId = "1780837968700043266";
    @Value("${platform.FB.channelSecret}")
    private String channelSecret = "hYJ13kJ1F3vnymwL3zj5Nxu1kpI0mIPL";

    public boolean create(){
        String apiUrl = this.apiDomain + "/fb/data/api/v2/new/user/create";
        String merchantUserId = "gsTestAccount1";
        JSONObject param = new JSONObject();
        param.put("merchantUserId", merchantUserId);
        String result = apiRequest(param, apiUrl);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        return res.getBoolean("success");
    }

    public BigDecimal getBalance(){
        String apiUrl = this.apiDomain + "/fb/data/api/v2/user/detail";
        String merchantUserId = "gsTestAccount1";
        JSONObject param = new JSONObject();
        param.put("merchantUserId", merchantUserId);
        String result = apiRequest(param, apiUrl);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        BigDecimal balance = BigDecimal.ZERO;
        if(res.getBoolean("success")){
            JSONObject data = res.getJSONObject("data");
            balance = data.getBigDecimal("balance");
        }
        return balance == null ? BigDecimal.ZERO : balance;
    }

    public boolean transferIn(){
        String apiUrl = this.apiDomain + "/fb/data/api/v2/new/transfer/in";
        String merchantUserId = "gsTestAccount1";
        BigDecimal amount = new BigDecimal("200.5");
        String businessId = "gsTestTransferIn1";
        JSONObject param = new JSONObject();
        param.put("amount", amount);
        param.put("businessId", businessId);
        param.put("merchantUserId", merchantUserId);
        String result = apiRequest(param, apiUrl);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        return res.getBoolean("success");
    }

    public boolean transferOut(){
        String apiUrl = this.apiDomain + "/fb/data/api/v2/new/transfer/out";
        String merchantUserId = "gsTestAccount1";
        BigDecimal amount = new BigDecimal("10.9");
        String businessId = "gsTestTransferOut1";
        JSONObject param = new JSONObject();
        param.put("amount", amount);
        param.put("businessId", businessId);
        param.put("merchantUserId", merchantUserId);
        String result = apiRequest(param, apiUrl);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        return res.getBoolean("success");
    }

    public String getTokenUrl(){
        String apiUrl = this.apiDomain + "/fb/data/api/v2/token/get";
        String merchantUserId = "gsTestAccount1";
        String platForm = "h5";//pc，h5, mobile
        JSONObject param = new JSONObject();
        param.put("platForm", platForm);
        param.put("merchantUserId", merchantUserId);
        String result = apiRequest(param, apiUrl);
        JSONObject res = JSONObject.parseObject(result);
        StringBuilder tokenUrl = new StringBuilder();
        if(res.getBoolean("success")){
            JSONObject data = res.getJSONObject("data");
            String token = data.getString("token");
            JSONObject serverInfo = data.getJSONObject("serverInfo");
            String h5Address = serverInfo.getString("h5Address");
            String apiServerAddress = serverInfo.getString("apiServerAddress");
            String pushServerAddress = serverInfo.getString("pushServerAddress");
            String virtualAddress = serverInfo.getString("virtualAddress");
            //拼接URL
            tokenUrl.append(h5Address).append("/index.html#/?")
                    .append("token=").append(token)
                    .append("&nickname=").append(merchantUserId)
                    .append("&apiSrc=").append(apiServerAddress)
                    .append("&pushSrc=").append(pushServerAddress)
                    .append("&virtualSrc=").append(virtualAddress);
        }
        return tokenUrl.toString();
    }

    public List<Long> getFileIds(){
        String apiUrl = this.apiDomain + "/fb/data/api/v2/order/file/ids";
        Long endTime = DateUtil.current();
        Long startTime = endTime - 600000;//十分钟前
        JSONObject param = new JSONObject();
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        String result = apiRequest(param, apiUrl);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        List<Long> ids = new ArrayList<>();
        if(res.getBoolean("success")){
            List<JSONObject> fileIdJSON = res.getList("data", JSONObject.class);
            for (Object obj:fileIdJSON) {
                JSONObject json = (JSONObject) obj;
                ids.add(json.getLong("fileId"));
            }
        }
        return ids;
    }

    public void getRecord(){
        String apiUrl = this.apiDomain + "/fb/data/api/v2/order/list";
        List<Long> ids = getFileIds();
        if(CollUtil.isEmpty(ids)){
            return;
        }
        for (Long fileId:ids) {
            JSONObject param = new JSONObject();
            param.put("fileId", fileId);
            String result = apiRequest(param, apiUrl);
            System.out.println(result);
            JSONObject res = JSONObject.parseObject(result);
            if(res.getBoolean("success")){
                JSONArray data = res.getJSONArray("data");
//                        "id": "1165648035481387073",
//                        "userId": "226881",
//                        "merchantId": "1780837968700043266",
//                        "merchantUserId": "gsTestAccount1",
//                        "currency": 1,
//                        "exchangeRate": "1",
//                        "seriesType": 0,
//                        "betType": "1x1*1",
//                        "allUp": 1,
//                        "allUpAlive": 1,
//                        "stakeAmount": "4",
//                        "liabilityStake": "4",
//                        "settleAmount": "0",
//                        "orderStatus": 4,
//                        "payStatus": 1,
//                        "oddsChange": 1,
//                        "device": "h5",
//                        "ip": "175.100.24.122",
//                        "createTime": "1713507291955",
//                        "modifyTime": "1713507292326",
//                        "maxWinAmount": "3.04",
//                        "loseAmount": "7.04",
//                        "rollBackCount": 0,
//                        "itemCount": 1,
//                        "seriesValue": 1,
//                        "betNum": 1,
//                        "liabilityCashoutStake": "0",
//                        "unitStake": "4",
//                        "betList": [{
//                            "id": "1165648035481387329",
//                            "orderId": "1165648035481387073",
//                            "sportId": 1,
//                            "matchId": "965045",
//                            "matchName": "热那亚 vs. 拉齐奥",
//                            "period": 1001,
//                            "marketId": "5696458",
//                            "marketType": 1000,
//                            "optionType": 1,
//                            "optionName": "热那亚 +0/0.5",
//                            "marketName": "让球",
//                            "tournamentId": "11018",
//                            "tournamentName": "意大利甲级联赛",
//                            "odds": "1.76",
//                            "oddsFormat": 1,
//                            "betOdds": "1.76",
//                            "settleStatus": 3,
//                            "isInplay": false,
//                            "p1": 0.25,
//                            "extendedParameter": "0.25",
//                            "matchType": 2,
//                            "matchTime": "1713544200000"}],
//                        "maxStake": "200000",
//                        "validSettleStakeAmount": "0",
//                        "validSettleAmount": "0",
//                        "cashOutCancelStake": "0",
//                        "walletType": 1,
//                        "version": 4
            }
        }
    }

    private String apiRequest(JSONObject param, String apiUrl){
        Long timestamp = DateUtil.current();
        String sign = SignUtils.fbSportSign(param, this.channelId, timestamp, this.channelSecret);
        Map<String, String> header = new HashMap<>();
        header.put("sign", sign);
        header.put("timestamp", timestamp.toString());
        header.put("merchantId", this.channelId);
        return HttpUtil.createPost(apiUrl).addHeaders(header).body(param.toJSONString()).timeout(10000).execute().body();
    }

    public static void main(String[] args) {
        FBSport fb = new FBSport();
//        System.out.println(fb.getTokenUrl());
        fb.getRecord();
    }
}
