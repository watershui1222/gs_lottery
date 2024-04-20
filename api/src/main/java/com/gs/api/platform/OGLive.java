package com.gs.api.platform;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gs.business.utils.plat.SignUtils;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.*;

public class OGLive {

    @Value("${platform.OG.publicKey}")
    private String publicKey = "JZFZrerilrHs0Atx05MFdNre9whPuPuf";
    @Value("${platform.OG.privateKey}")
    private String privateKey = "U30XdkBiPClDzxe64iedQy0258rzAkns";
    @Value("${platform.OG.operator}")
    private String operator = "ogptestcny";
    @Value("${platform.OG.apiDomain}")
    private String apiDomain = "http://xyz.pwqr820.com:9003";
    @Value("${platform.OG.betLimit}")
    private String betLimit = "159";
    @Value("${platform.OG.lobbyId}")
    private String lobbyId = "1";

    public boolean register(){
        String player_id = "gsTestAccount2";
        String nickname = player_id + "nk";
        Long timestamp = DateUtil.current();
        JSONObject param = new JSONObject();
        param.put("player_id",player_id);
        param.put("nickname",nickname);
        param.put("timestamp",timestamp);
        String apiUrl = this.apiDomain + "/api/v2/platform/transfer-wallet/register";
        String result = apiRequestPost(apiUrl, param);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        String rsCode = res.getString("rs_code");
        if(StrUtil.equals(rsCode, "S-100")){
            return true;
        }
        return false;
    }

    public String apiRequestPost(String apiUrl, JSONObject param){
        Map<String, String> header = new HashMap<>();
        header.put("key", this.publicKey);
        header.put("operator-name", this.operator);
        return HttpUtil.createPost(apiUrl).addHeaders(header).body(param.toJSONString()).timeout(10000).execute().body();
    }

    public String apiRequestGet(String apiUrl){
        Map<String, String> header = new HashMap<>();
        header.put("key", this.publicKey);
        header.put("operator-name", this.operator);
        return HttpUtil.createGet(apiUrl).addHeaders(header).timeout(10000).execute().body();
    }

    public boolean deposit(){
        String player_id = "gslichade123";
        String transaction_id = "dsfsdfsd234";
        BigDecimal transfer_amount = new BigDecimal("100");
        Long timestamp = DateUtil.current();
//        Long timestamp = 1713584456742L;
        JSONObject param = new JSONObject();
        param.put("player_id",player_id);
        param.put("transaction_id",transaction_id);
        param.put("transfer_amount",transfer_amount);
        param.put("timestamp",timestamp);
        String signature = DigestUtil.md5Hex(SignUtils.sortParam(param) + this.privateKey);
        param.put("signature",signature);
        String apiUrl = this.apiDomain + "/api/v2/platform/transfer-wallet/deposit";
        String result = apiRequestPost(apiUrl, param);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        String rsCode = res.getString("rs_code");
        if(StrUtil.equals(rsCode, "S-100")){
            return true;
        }
        return false;
    }

    public boolean withdraw(){
        String player_id = "gsTestAccount1";
        String transaction_id = "gstestorderno5";
        BigDecimal transfer_amount = new BigDecimal("50");
        Long timestamp = DateUtil.current();
        JSONObject param = new JSONObject();
        param.put("player_id",player_id);
        param.put("transaction_id",transaction_id);
        param.put("transfer_amount",transfer_amount);
        param.put("timestamp",timestamp);
        String signature = DigestUtil.md5Hex(SignUtils.sortParam(param) + this.privateKey);
        param.put("signature",signature);
        String apiUrl = this.apiDomain + "/api/v2/platform/transfer-wallet/withdraw";
        String result = apiRequestPost(apiUrl, param);
        System.out.println(param);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        String rsCode = res.getString("rs_code");
        if(StrUtil.equals(rsCode, "S-100")){
            return true;
        }
        return false;
    }

    public String launch(){
        String player_id = "gsTestAccount1";
        String nickname = player_id + "nk";
        Long timestamp = DateUtil.current();
        String lang = "zh";
        String token = player_id + "tk";
        Integer game_id = Integer.valueOf(this.lobbyId);
        Integer betlimit = Integer.valueOf(this.betLimit);
        JSONObject json = new JSONObject();
        json.put("player_id",player_id);
        json.put("nickname",nickname);
        json.put("timestamp",timestamp);
        json.put("lang",lang);
        json.put("token",token);
        json.put("game_id",game_id);
        json.put("betlimit",betlimit);
        String sortParmstr = SignUtils.sortParam(json);
        String signature = DigestUtil.md5Hex(sortParmstr + this.privateKey);
        String param = sortParmstr + "&signature=" + signature;
        String apiUrl = this.apiDomain + "/api/v2/platform/games/launch?" + param;
        String result = apiRequestGet(apiUrl);
        System.out.println(apiUrl);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        String rsCode = res.getString("rs_code");
        String gameUrl = "";
        if(StrUtil.equals(rsCode, "S-100")){
            gameUrl = res.getString("game_link");
        }
        return gameUrl;
    }

    public void gameList(){
        String apiUrl = this.apiDomain + "/api/v2/platform/game/game-list";
        String result = apiRequestGet(apiUrl);
        System.out.println(apiUrl);
        System.out.println(result);
    }

    public void betlimit(){
        String apiUrl = this.apiDomain + "/api/v2/platform/operator/betlimit";
        String result = apiRequestGet(apiUrl);
        System.out.println(apiUrl);
        System.out.println(result);
    }

    public BigDecimal getBalance(){
        String player_id = "gsTestAccount1";
        JSONObject json = new JSONObject();
        json.put("player_id",player_id);
        String sortParmstr = SignUtils.sortParam(json);
        String apiUrl = this.apiDomain + "/api/v2/platform/transfer-wallet/get-balance?" + sortParmstr;
        String result = apiRequestGet(apiUrl);
        System.out.println(apiUrl);
        System.out.println(result);
        JSONObject res = JSONObject.parseObject(result);
        String rsCode = res.getString("rs_code");
        BigDecimal balance = BigDecimal.ZERO;
        if(StrUtil.equals(rsCode, "S-100")){
            balance = res.getBigDecimal("current_balance");
        }
        return balance;
    }

    public String getRecord(){
        Integer fetch_id = 12;//应当用redis记录此数据作为下次拉取的数据
        Integer limit = 10000;
        JSONObject json = new JSONObject();
        json.put("fetch_id",fetch_id);
        json.put("limit",limit);
        String sortParmstr = SignUtils.sortParam(json);
        String apiUrl = this.apiDomain + "/api/v2/platform/transaction/history?" + sortParmstr;
        String result = apiRequestGet(apiUrl);
        System.out.println(apiUrl);
        JSONObject res = JSONObject.parseObject(result);
        if(StrUtil.equals(res.getString("rs_code"), "S-100")){
            Integer last_fetch_id = res.getInteger("last_fetch_id");
            JSONArray records = res.getJSONArray("records");
//            {
//                "fetch_id": 12,
//                    "player_id": "gsTestAccount1", 账号
//                    "transaction_id": "1001421720000007834620_41", 单号
//                    "game_id": 55, 游戏id
//                    "round_id": 142172580,  台号
//                    "bet_place": "p",
//                    "result_url": "https:\/\/og-plus.oss-accelerate.aliyuncs.com\/results\/test\/20240418\/M120240418142172580.html",
//                    "debit_amount": 10,
//                    "credit_amount": 0,
//                    "winlose_amount": -10,
//                    "currency": "CNY",
//                    "secondary_info": {
//                "balance": 210,
//                        "bet_amount": 10, 投注金额
//                        "detain_amount": 0
//            },
//                "other_info": {
//                "ip": "175.100.24.122",
//                        "result": "banker,small,banker_bonus",
//                        "browser": "Chrome",
//                        "roundno": "18-18",
//                        "game_name": "baccarat",
//                        "deviceType": "desktop",
//                        "game_information": {
//                    "bankerCards": "QD,9H,",
//                            "playerCards": "4S,7C,"
//                }
//            },
//                "debit_at": 1713432871,  下注时间
//                    "rollback_at": "",
//                    "credit_at": 1713432891,  结算时间
//                    "cancel_at": "",
//                    "resettled_at": "",
//                    "transaction_type": "credit",
//                    "remark": {
//
//            },
//                "effective_amount": 10 有效投注
//            }
        }
        return result;
    }

    public static void main(String[] args) {
        OGLive og = new OGLive();
//        System.out.println(og.launch());
        System.out.println(og.deposit());
    }

}
