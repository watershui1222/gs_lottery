package com.gs.api.platform;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.gs.commons.utils.AesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CR皇冠体育
 */
@Slf4j
public class HuangGuanSport {
    @Value("${platform.HuangGuan.agId}")
    public String agId = "2829";
    @Value("${platform.HuangGuan.agPassword}")
    public String agPassword = "aaa123";
    @Value("${platform.HuangGuan.agName}")
    public String agName = "ZF946test";
    @Value("${platform.HuangGuan.secretKey}")
    public String secretKey = "9Sceij7Eka7331lR";
    @Value("${platform.HuangGuan.apiUrl}")
    public String apiUrl = "https://api.orb-6789.com/app/control_API/agents/api_doaction.php";

    /**
     * 其他接口的token从此接口获取 有效期36H
     * @return
     */
    public String agLogin() {
        String token = "";
        JSONObject param = new JSONObject();
        JSONObject request = new JSONObject();
        request.put("username", agName);
        request.put("password", agPassword);
        request.put("timestamp", DateUtil.current());
        String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey, false);
        param.put("Request", requestStr);
        param.put("Method", "AGLogin");
        param.put("AGID", agId);
        HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
        String encryptRes = res.body();
        String result = AesUtils.AESDecrypt(encryptRes, this.secretKey);
        log.info("皇冠 aglogin接口 param= " + param + " result = " + result);
        JSONObject resultJS = JSONObject.parseObject(result);
        if(StrUtil.equals(resultJS.getString("respcode"), "0000")){
            return resultJS.getString("token");
        }
        return token;
    }

    /**
     * 创建会员
     * @return
     */
    public String createMember(){
        String token = agLogin();
        if(StrUtil.isNotBlank(token)){
            //代理登录成功
            JSONObject param = new JSONObject();
            JSONObject request = new JSONObject();
            String memname = "GSTESTACCOUNT3";
            String password = "CRpassword2";
            request.put("memname", memname);
            request.put("password", password);
            request.put("currency", "CNY");
            request.put("method", "CreateMember");
            request.put("token", token);
            request.put("timestamp", DateUtil.current());
            String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey, false);
            param.put("Request", requestStr);
            param.put("Method", "CreateMember");
            param.put("AGID", agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            log.info("皇冠 createMember接口 param= " + param + " result = " + resStr);
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = AesUtils.AESDecrypt(resStr, this.secretKey);
                String respcode = JSONObject.parseObject(result).getString("respcode");
                if(StrUtil.equals(respcode, "0000")){
                    //创建成功
                    return "OK";
                }
            }
        }

        return "";
    }

    /**
     * 会员登陆 只是拿memtoken
     * @return
     * @throws Exception
     */
//    public String memLogin() throws Exception {
//        String token = agLogin();
//        if(StrUtil.isNotBlank(token)){
//            //代理登录成功
//            JSONObject param = new JSONObject();
//            JSONObject request = new JSONObject();
//            String memname = "GSTESTACCOUNT3";
//            String password = "CRpassword2";
//            request.put("memname", memname);
//            request.put("password", password);
//            request.put("currency", "CNY");
//            request.put("method", "MemLogin");
//            request.put("token", token);
//            request.put("remoteip", "127.0.0.1");
//            request.put("timestamp", DateUtil.current());
//            String requestStr = CRUtil.AESEncrypt(request.toJSONString(), this.secretKey);
//            param.put("Request", requestStr);
//            param.put("Method", "MemLogin");
//            param.put("AGID", agId);
//            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
//            String resStr = res.body();
//            if(!JSONUtil.isTypeJSON(resStr)){
//                String result = CRUtil.AESDecrypt(resStr, this.secretKey);
//                System.out.println(result);
//                String respcode = JSONObject.parseObject(result).getString("respcode");
//                if(StrUtil.equals(respcode, "0000")){
//                    //创建成功
//                    return "OK";
//                }
//            }
//        }
//        return "";
//    }

    /**
     * 登陆游戏
     * @return
     * @throws Exception
     */
    public String launchGame(){
        String token = agLogin();
        if(StrUtil.isNotBlank(token)){
            //代理登录成功
            JSONObject param = new JSONObject();
            JSONObject request = new JSONObject();
            String memname = "GSTESTACCOUNT3";
            String password = "CRpassword2";
            request.put("memname", memname);
            request.put("password", password);
            request.put("currency", "CNY");
            request.put("method", "LaunchGame");
            request.put("token", token);
            request.put("machine", "MOBILE");
            request.put("langx", "zh-cn");
            request.put("remoteip", "127.0.0.1");
            request.put("timestamp", DateUtil.current());
            String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey, false);
            param.put("Request", requestStr);
            param.put("Method", "LaunchGame");
            param.put("AGID", agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            log.info("皇冠 launchGame接口 param= " + param + " result = " + resStr);
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = AesUtils.AESDecrypt(resStr, this.secretKey);
                JSONObject resultJson = JSONObject.parseObject(result);
                String respcode = resultJson.getString("respcode");
                if(StrUtil.equals(respcode, "0000")){
                    return resultJson.getString("launchgameurl");
                }
            }
        }
        return "";
    }

    /**
     * 存款
     * @return
     * @throws Exception
     */
    public String deposit(){
        String token = agLogin();
        if(StrUtil.isNotBlank(token)){
            //代理登录成功
            JSONObject param = new JSONObject();
            JSONObject request = new JSONObject();
            String memname = "GSTESTACCOUNT3";
            String amount = "200";
            String payno = "GSTESTORDERNO1";
            request.put("memname", memname);
            request.put("amount", amount);
            request.put("payno", payno);
            request.put("method", "Deposit");
            request.put("token", token);
            request.put("timestamp", DateUtil.current());
            String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey,false);
            param.put("Request", requestStr);
            param.put("Method", "Deposit");
            param.put("AGID", agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            log.info("皇冠 deposit param= " + param + " result = " + resStr);
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = AesUtils.AESDecrypt(resStr, this.secretKey);
                System.out.println(result);
                JSONObject resultJson = JSONObject.parseObject(result);
                String respcode = resultJson.getString("respcode");
                if(StrUtil.equals(respcode, "0000")){
                    return "OK";
                }
            }
        }
        return "FAIL";
    }

    /**
     * 提现
     * @return
     * @throws Exception
     */
    public String withdraw(){
        String token = agLogin();
        if(StrUtil.isNotBlank(token)){
            //代理登录成功
            JSONObject param = new JSONObject();
            JSONObject request = new JSONObject();
            String memname = "GSTESTACCOUNT3";
            String amount = "50";
            String payno = "GSTESTORDERNO1";//我方单号
            request.put("memname", memname);
            request.put("amount", amount);
            request.put("payno", payno);
            request.put("method", "Withdraw");
            request.put("token", token);
            request.put("timestamp", DateUtil.current());
            String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey,false);
            param.put("Request", requestStr);
            param.put("Method", "Withdraw");
            param.put("AGID", agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            log.info("皇冠 withdraw接口 param= " + param + " result = " + resStr);
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = AesUtils.AESDecrypt(resStr, this.secretKey);
                System.out.println(result);
                JSONObject resultJson = JSONObject.parseObject(result);
                String respcode = resultJson.getString("respcode");
                if(StrUtil.equals(respcode, "0000")){
                    return "OK";
                }
            }
        }
        return "FAIL";
    }

    /**
     * 查询余额
     * @return
     * @throws Exception
     */
    public BigDecimal chkMemberBalance(){
        String token = agLogin();
        if(StrUtil.isNotBlank(token)){
            //代理登录成功
            JSONObject param = new JSONObject();
            JSONObject request = new JSONObject();
            String memname = "GSTESTACCOUNT3";
            request.put("memname", memname);
            request.put("method", "chkMemberBalance");
            request.put("token", token);
            request.put("timestamp", DateUtil.current());
            String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey,false);
            param.put("Request", requestStr);
            param.put("Method", "chkMemberBalance");
            param.put("AGID", agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            log.info("皇冠 chkMemberBalance接口 param= " + param + " result = " + resStr);
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = AesUtils.AESDecrypt(resStr, this.secretKey);
                System.out.println(result);
                JSONObject resultJson = JSONObject.parseObject(result);
                String respcode = resultJson.getString("respcode");
                if(StrUtil.equals(respcode, "0000")){
                    return new BigDecimal(resultJson.getString("balance"));
                }
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * 踢出
     * @return
     * @throws Exception
     */
    public String kickOutMem(){
        String token = agLogin();
        if(StrUtil.isNotBlank(token)){
            //代理登录成功
            JSONObject param = new JSONObject();
            JSONObject request = new JSONObject();
            String memname = "GSTESTACCOUNT3";
            request.put("memname", memname);
            request.put("method", "KickOutMem");
            request.put("token", token);
            request.put("timestamp", DateUtil.current());
            String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey,false);
            param.put("Request", requestStr);
            param.put("Method", "KickOutMem");
            param.put("AGID", agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            log.info("皇冠 kickOutMem接口 param= " + param + " result = " + resStr);
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = AesUtils.AESDecrypt(resStr, this.secretKey);
                System.out.println(result);
                JSONObject resultJson = JSONObject.parseObject(result);
                String respcode = resultJson.getString("respcode");
                if(StrUtil.equals(respcode, "0000")){
                    return "OK";
                }
            }
        }
        return "FAIL";
    }

    /**
     * 拉注单
     * @return
     * @throws Exception
     */
    public List<String> aLLWager(){
        String token = agLogin();
        List<String> recordList = new ArrayList<>();
        if(StrUtil.isNotBlank(token)){

            Date now = new Date();
            String dateEnd = DateUtil.format(now, "yyyy-MM-dd hh:mm:ss");
            String dateStart = DateUtil.format(DateUtil.offsetHour(now, -1), "yyyy-MM-dd hh:mm:ss");
            int page = 1;
            int wagerTotalpage = 1;

            do{
                //代理登录成功
                JSONObject param = new JSONObject();
                JSONObject request = new JSONObject();
                request.put("method", "ALLWager");
                request.put("settle", "1");
                request.put("dateStart", dateStart);
                request.put("dateEnd", dateEnd);
                request.put("page", page);
                request.put("token", token);
                request.put("timestamp", DateUtil.current());
                request.put("langx", "zh-cn");
                String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey,false);
                param.put("Request", requestStr);
                param.put("Method", "ALLWager");
                param.put("AGID", agId);
                HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
                String resStr = res.body();
                if(!JSONUtil.isTypeJSON(resStr)){
                    String result = AesUtils.AESDecrypt(resStr, this.secretKey);
                    System.out.println(result);
                    JSONObject resultJson = JSONObject.parseObject(result);
                    String respcode = resultJson.getString("respcode");
                    if(StrUtil.equals(respcode, "0000")){
                        wagerTotalpage = resultJson.getInteger("wager_totalpage");
                        List<String> subResult = JSONUtil.toList(resultJson.getString("wager_data"), String.class);
                        recordList.addAll(subResult);
                    }
                }
                page++;
            }while(page <= wagerTotalpage);
        }
        return recordList;
    }


    public static void main(String[] args){
        HuangGuanSport cr = new HuangGuanSport();
        System.out.println(cr.aLLWager());
    }

}
