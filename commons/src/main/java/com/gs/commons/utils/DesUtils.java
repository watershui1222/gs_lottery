package com.gs.commons.utils;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DES;

import java.net.URLEncoder;

public class DesUtils {

    public static String MD5(String sourceStr) {
        return DigestUtil.md5Hex(sourceStr);
    }

    /**
     * @param content 明文
     * @param key 偏移量
     * @return
     */
    public static  String DESEncrypt(String content, String key, boolean URLEncode){
        try {
            byte[] raw = key.getBytes("UTF-8");
            DES des = SecureUtil.des(raw);
            byte[] encrypt = des.encrypt(content);
            String encryptStr = Base64.encode(encrypt);
            if(URLEncode){
                return URLEncoder.encode(encryptStr, "UTF-8");
            }
            return encryptStr;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param content 密文
     * @param key 偏移量
     * @return
     */
    public static String DESDecrypt(String content, String key){
        try {
            byte[] raw = key.getBytes("UTF-8");
            DES des = SecureUtil.des(raw);
            String decryptHex = des.decryptStr(content, CharsetUtil.CHARSET_UTF_8);
            return decryptHex;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) throws Exception {
//        String ss = "{password:cit001123,remoteip:192.168.1.1,method:AGLogin,username:cit001,timestamp:1497866722526}";
//        String scr = "5rx9ibpyqdn6xmt3";
//        String enStr = DESEncrypt(ss, scr, false);
//        System.out.println(enStr);
//        String mingwen = DESDecrypt(enStr, scr);
//        System.out.println(mingwen);
//    }
}
