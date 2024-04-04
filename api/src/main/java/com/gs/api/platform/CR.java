package com.gs.api.platform;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.gs.api.platform.platUtils.CRUtil;
import com.gs.api.platform.platUtils.KYUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CR皇冠体育
 */
public class CR {
    public String agId = "2829";
    public String agPassword = "aaa123";
    public String agName = "ZF946test";
    public String secretKey = "9Sceij7Eka7331lR";
    public String apiUrl = "https://api.orb-6789.com/app/control_API/agents/api_doaction.php";

    /**
     * 其他接口的token从此接口获取 有效期36H
     * @return
     */
    public String agLogin() throws Exception {
        String token = "";
        JSONObject param = new JSONObject();
        JSONObject request = new JSONObject();
        request.put("username", agName);
        request.put("password", agPassword);
        request.put("timestamp", DateUtil.current());
        String requestStr = CRUtil.AESEncrypt(request.toJSONString(), this.secretKey);
        param.put("Request", requestStr);
        param.put("Method", "AGLogin");
        param.put("AGID", agId);
        HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
        String encryptRes = res.body();
        String result = CRUtil.AESDecrypt(encryptRes, this.secretKey);
        JSONObject resultJS = JSONObject.parseObject(result);
        if(StrUtil.equals(resultJS.getString("respcode"), "0000")){
            token = resultJS.getString("token");
        }
        return token;
    }

    /**
     * 创建会员
     * @return
     */
    public String createMember() throws Exception {
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
            String requestStr = CRUtil.AESEncrypt(request.toJSONString(), this.secretKey);
            param.put("Request", requestStr);
            param.put("Method", "CreateMember");
            param.put("AGID", agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = CRUtil.AESDecrypt(resStr, this.secretKey);
                System.out.println(result);
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
    public String launchGame() throws Exception {
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
            String requestStr = CRUtil.AESEncrypt(request.toJSONString(), this.secretKey);
            param.put("Request", requestStr);
            param.put("Method", "LaunchGame");
            param.put("AGID", agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = CRUtil.AESDecrypt(resStr, this.secretKey);
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
    public String deposit() throws Exception {
        String token = agLogin();
        if(StrUtil.isNotBlank(token)){
            //代理登录成功
            JSONObject param = new JSONObject();
            JSONObject request = new JSONObject();
            String memname = "GSTESTACCOUNT3";
            String amount = "200";
            String payno = "GSTESTORDERNO1";//我方单号
            request.put("memname", memname);
            request.put("amount", amount);
            request.put("payno", payno);
            request.put("method", "Deposit");
            request.put("token", token);
            request.put("timestamp", DateUtil.current());
            String requestStr = CRUtil.AESEncrypt(request.toJSONString(), this.secretKey);
            param.put("Request", requestStr);
            param.put("Method", "Deposit");
            param.put("AGID", agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = CRUtil.AESDecrypt(resStr, this.secretKey);
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
    public String withdraw() throws Exception {
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
            String requestStr = CRUtil.AESEncrypt(request.toJSONString(), this.secretKey);
            param.put("Request", requestStr);
            param.put("Method", "Withdraw");
            param.put("AGID", agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = CRUtil.AESDecrypt(resStr, this.secretKey);
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
    public BigDecimal chkMemberBalance() throws Exception {
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
            String requestStr = CRUtil.AESEncrypt(request.toJSONString(), this.secretKey);
            param.put("Request", requestStr);
            param.put("Method", "chkMemberBalance");
            param.put("AGID", agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = CRUtil.AESDecrypt(resStr, this.secretKey);
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
    public String kickOutMem() throws Exception {
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
            String requestStr = CRUtil.AESEncrypt(request.toJSONString(), this.secretKey);
            param.put("Request", requestStr);
            param.put("Method", "KickOutMem");
            param.put("AGID", agId);
            HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
            String resStr = res.body();
            if(!JSONUtil.isTypeJSON(resStr)){
                String result = CRUtil.AESDecrypt(resStr, this.secretKey);
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
    public List<String> aLLWager() throws Exception {
        String token = agLogin();
        List<String> recordList = new ArrayList<>();
        if(StrUtil.isNotBlank(token)){

            Date now = new Date();
            String dateEnd = DateUtil.format(now, "yyyy-MM-dd hh:mm:ss");
            String dateStart = DateUtil.format(DateUtil.offsetDay(now, -1), "yyyy-MM-dd hh:mm:ss");
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
                String requestStr = CRUtil.AESEncrypt(request.toJSONString(), this.secretKey);
                param.put("Request", requestStr);
                param.put("Method", "ALLWager");
                param.put("AGID", agId);
                HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
                String resStr = res.body();
                if(!JSONUtil.isTypeJSON(resStr)){
                    String result = CRUtil.AESDecrypt(resStr, this.secretKey);
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
                System.out.println("wagerTotalpage = " + wagerTotalpage);
            }while(page <= wagerTotalpage);
        }
        return recordList;
    }


    public static void main(String[] args) throws Exception {
        CR cr = new CR();
        System.out.println(cr.aLLWager().size());
//        JSONObject request = new JSONObject();
//        request.put("password", "cit001123");
//        request.put("remoteip", "192.168.1.1");
//        request.put("method", "AGLogin");
//        request.put("username", "cit001");
//        request.put("timestamp", "1497866722526");
//        System.out.println(CRUtil.AESEncrypt(request.toJSONString(), "5rx9ibpyqdn6xmt3"));
//        String ggg = "Uz3vy0fL9i1CWYlSOZ+PpF4W0QntdjHsC4sYaASlQeCbkI4qvudhaRs6nTHQyu+6hjj9hg1hN6Ep" +
//                "CvL66FnQI2Twsq8dGl5mbRbP6UevQvQ=";
//        System.out.println(CRUtil.AESDecrypt(ggg, "9Sceij7Eka7331lR"));
//        String hhh = "Uz3vy0fL9i1CWYlSOZ+PpF4W0QntdjHsC4sYaASlQeCbkI4qvudhaRs6nTHQyu+6iwFTjFZTWZjr" +
//                "iWXZ/5lzizbUenDOOfFf3CPzlV0rqN8=";
//        System.out.println(hhh.replace("\n", ""));
    }

}
