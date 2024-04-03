package com.gs.api.platform;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.gs.api.platform.platUtils.CRUtil;
import com.gs.api.platform.platUtils.KYUtil;
import org.springframework.beans.factory.annotation.Autowired;

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
        return token;
    }

    public String login(){

        return "";
    }

    public static void main(String[] args) throws Exception {
        CR cr = new CR();
        System.out.println(cr.agLogin());
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
