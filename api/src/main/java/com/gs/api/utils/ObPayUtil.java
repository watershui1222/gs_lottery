package com.gs.api.utils;

import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ObPayUtil {


    private static String Secret = "56cad83c30ea37591fdc2e1bcb60aa24";
    private static String url = "https://open.obpay.fun/api/payment/create";
    public static void main(String[] args) throws Exception {
        Map<String, Object> treeMap = new TreeMap<>();
        treeMap.put("merchantNo", 16212792);
        treeMap.put("outTradeNo", "898138131237");
        treeMap.put("amount", 10000);
        treeMap.put("currency", "OB");
        treeMap.put("notifyUrl", "localhost:8080/notifyUrl");

        String sortData = sortData(treeMap);
        String stringSignTemp = StringUtils.join(sortData, "&", Secret);
        String sign = md5Sign(stringSignTemp).toUpperCase();
        treeMap.put("sign", sign);
        String postJSON = HttpUtil.postJSON(url, JSON.toJSONString(treeMap), "UTF-8");
        System.out.println(postJSON);


    }


    public static String sortData(Map<String, ?> sourceMap) {
        String returnStr = sortData(sourceMap, "&");
        return returnStr;
    }

    public static String sortData(Map<String, ?> sourceMap, String link) {
        //log.info("start sortData method()");
        if (StringUtils.isEmpty(link)) {
            link = "&";
        }
        Map<String, Object> sortedMap = new TreeMap<String, Object>();
        sortedMap.putAll(sourceMap);
        Set<Map.Entry<String, Object>> entrySet = sortedMap.entrySet();
        StringBuilder sbf = new StringBuilder();
        for (Map.Entry<String, Object> entry : entrySet) {
            if (null != entry.getValue() && StringUtils.isNotEmpty(entry.getValue().toString())) {
                sbf.append(entry.getKey()).append("=").append(entry.getValue()).append(link);
            }
        }
        String returnStr = sbf.toString();
        if (returnStr.endsWith(link)) {
            returnStr = returnStr.substring(0, returnStr.length() - 1);
        }
        return returnStr;
    }



    public static String md5Sign(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            //字符数组转换成字符串
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // 32位加密
            return buf.toString().toUpperCase();
            // 16位的加密
            //return buf.toString().substring(8, 24).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
