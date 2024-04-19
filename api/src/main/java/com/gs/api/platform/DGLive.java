package com.gs.api.platform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DGLive {

    @Value("${platform.DG.apiDomain}")
    private String apiDomain = "https://api.dg99web.com";
    @Value("${platform.DG.apiKey}")
    private String apiKey = "821e72efedc2468d985039b0db7bd7a4";
    @Value("${platform.DG.agent}")
    private String agent = "DGTE01070A";

    public boolean register(){
        String apiUrl = this.apiDomain + "/user/signup/" + this.agent;
        String random = RandomUtil.randomString(6);
        String token = DigestUtil.md5Hex(this.agent + this.apiKey + random);
        String data = "A";
        String username = "gsTestAccount1";
        String password = "testpassword112";
        String currencyName = "CNY";
        BigDecimal winLimit = BigDecimal.ZERO;
        JSONObject member = new JSONObject();
        member.put("username",username);
        member.put("password",password);
        member.put("currencyName",currencyName);
        member.put("winLimit",winLimit);

        JSONObject param = new JSONObject();
        param.put("token",token);
        param.put("random",random);
        param.put("data",data);
        param.put("member",member);
        String result = apiRequest(apiUrl, param);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        if (res.getInteger("codeId") == 0){
            return true;
        }
        return false;
    }

    public String apiRequest(String apiUrl, JSONObject param){
        return HttpUtil.createPost(apiUrl).header("Content-Type" , "application/json").body(param.toJSONString()).timeout(10000).execute().body();
    }

    public String login(){
        String apiUrl = this.apiDomain + "/user/login/" + this.agent;
        String random = RandomUtil.randomString(6);
        String token = DigestUtil.md5Hex(this.agent + this.apiKey + random);
        String username = "gsTestAccount1";
        JSONObject member = new JSONObject();
        member.put("username",username);

        JSONObject param = new JSONObject();
        param.put("token",token);
        param.put("random",random);
        param.put("member",member);
        String result = apiRequest(apiUrl, param);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        String finalUrl = "";
        if (res.getInteger("codeId") == 0){
            List<String> urlList = res.getList("list", String.class);
            finalUrl = urlList.get(0) + res.getString("token")  + "&language=cn&showapp=off";
        }
        return finalUrl;
    }

    public BigDecimal getBalance(){
        String apiUrl = this.apiDomain + "/user/getBalance/" + this.agent;
        String random = RandomUtil.randomString(6);
        String token = DigestUtil.md5Hex(this.agent + this.apiKey + random);
        String username = "gsTestAccount1";
        JSONObject member = new JSONObject();
        member.put("username",username);

        JSONObject param = new JSONObject();
        param.put("token",token);
        param.put("random",random);
        param.put("member",member);
        String result = apiRequest(apiUrl, param);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        BigDecimal balance = BigDecimal.ZERO;
        if (res.getInteger("codeId") == 0){
            JSONObject memberR = res.getJSONObject("member");
            balance = memberR.getBigDecimal("balance");
        }
        return balance;
    }

    /**
     * 正数转入  负数转出
     * @param amount
     * @return
     */
    public boolean transfer(BigDecimal amount){
        String apiUrl = this.apiDomain + "/account/transfer/" + this.agent;
        String random = RandomUtil.randomString(6);
        String token = DigestUtil.md5Hex(this.agent + this.apiKey + random);
        String data = "gsTestTransferOrder3";
        String username = "gsTestAccount1";
        JSONObject member = new JSONObject();
        member.put("username",username);
        member.put("amount",amount);

        JSONObject param = new JSONObject();
        param.put("token",token);
        param.put("random",random);
        param.put("data",data);
        param.put("member",member);
        String result = apiRequest(apiUrl, param);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        return res.getInteger("codeId") == 0;
    }

    /**
     * 踢人需要先调用此接口
     * @return
     */
    public List<JSONObject> onlineReport(){
        String apiUrl = this.apiDomain + "/user/onlineReport/" + this.agent;
        String random = RandomUtil.randomString(6);
        String token = DigestUtil.md5Hex(this.agent + this.apiKey + random);

        JSONObject param = new JSONObject();
        param.put("token",token);
        param.put("random",random);
        String result = apiRequest(apiUrl, param);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        List<JSONObject> list = new ArrayList<>();
        if(res.getInteger("codeId") == 0){
            list = res.getList("list", JSONObject.class);
        }
        return list;
    }

    public boolean kickOff(){
        String apiUrl = this.apiDomain + "/user/offline/" + this.agent;
        String random = RandomUtil.randomString(6);
        String token = DigestUtil.md5Hex(this.agent + this.apiKey + random);
        String username = "gsTestAccount1";
        List<JSONObject> memberList = onlineReport();
        if(CollUtil.isEmpty(memberList)){
            return true;
        }
        Optional<Integer> memberId = memberList.stream().filter(m -> m.getString("username").equalsIgnoreCase(username))
        .map(m -> m.getInteger("memberId")).findFirst();
        if(!memberId.isPresent()){
            return true;
        }
        List<Integer> list = new ArrayList<>();
        list.add(memberId.get());
        JSONObject param = new JSONObject();
        param.put("token",token);
        param.put("random",random);
        param.put("list",list);
        String result = apiRequest(apiUrl, param);
        JSONObject res = JSONObject.parseObject(result);
        return res.getInteger("codeId") == 0;
    }

    public void getRecord(){
        String recordUrl = this.apiDomain + "/game/getReport/" + this.agent;
        String random = RandomUtil.randomString(6);
        String token = DigestUtil.md5Hex(this.agent + this.apiKey + random);
        JSONObject param = new JSONObject();
        param.put("token",token);
        param.put("random",random);
        String result = apiRequest(recordUrl, param);
        JSONObject res = JSONObject.parseObject(result);
        List<JSONObject> recordList = new ArrayList<>();
        if(res.getInteger("codeId") == 0){
            recordList = res.getList("list", JSONObject.class);
        }
        List<Long> markList = new ArrayList<>();
        markList.add(recordList.get(0).getLong("id"));
        //入库后需要标记已入库的订单 不然会一直拉取24小时内前1000的订单
//                "id": 19983915397,
//                "tableId": 10101,
//                "shoeId": 34,
//                "playId": 15,
//                "lobbyId": 1,
//                "gameType": 1,
//                "gameId": 1,
//                "memberId": 140494654,
//                "parentId": 31756,
//                "betTime": "2024-04-18 19:59:16",
//                "calTime": "2024-04-18 19:59:36",
//                "winOrLoss": 0,
//                "balanceBefore": 200,
//                "betPoints": 25,
//                "betPointsz": 0,
//                "availableBet": 25,
//                "userName": "GSTESTACCOUNT1",
//                "result": "{\"result\":\"1,2,6\",\"poker\":{\"banker\":\"46-35-0\",\"player\":\"38-40-52\"}}",
//                "betDetail": "{\"player\":25.0}",
//                "ip": "175.100.24.122",
//                "ext": "240418B011761",
//                "isRevocation": 1,
//                "currencyId": 1,
//                "deviceType": 1,
//                "roadid": 0,
//                "pluginid": 0
        markReport(markList);
    }

    /**
     * 标记注单 使其不再被拉取过来  如需重新拉取需要联系他们客服
     */
    public boolean markReport(List<Long> list){
        String recordUrl = this.apiDomain + "/game/markReport/" + this.agent;
        String random = RandomUtil.randomString(6);
        String token = DigestUtil.md5Hex(this.agent + this.apiKey + random);
        JSONObject param = new JSONObject();
        param.put("token",token);
        param.put("random",random);
        param.put("list",list);
        String result = apiRequest(recordUrl, param);
        JSONObject res = JSONObject.parseObject(result);
        return res.getInteger("codeId") == 0;
    }



    public static void main(String[] args) {
        DGLive dg = new DGLive();
//        System.out.println(dg.transfer(new BigDecimal("-5")));
//        System.out.println(dg.login());
        dg.getRecord();
    }


}
