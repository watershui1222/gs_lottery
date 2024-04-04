package com.gs.commons.utils;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.AES;

import java.net.URLEncoder;

public class AesUtils {

    public static String MD5(String sourceStr) {
        return DigestUtil.md5Hex(sourceStr);
    }

    /**
     * @param content 明文
     * @param key 偏移量
     * @return
     */
    public static  String AESEncrypt(String content, String key, boolean URLEncode){
        try {
            byte[] raw = key.getBytes("UTF-8");
            AES aes = SecureUtil.aes(raw);
            byte[] encrypt = aes.encrypt(content);
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
    public static String AESDecrypt(String content, String key){
        try {
            byte[] raw = key.getBytes("UTF-8");
            AES aes = SecureUtil.aes(raw);
            String decryptHex = aes.decryptStr(content, CharsetUtil.CHARSET_UTF_8);
            return decryptHex;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) throws Exception {
//        String ss = "{password:cit001123,remoteip:192.168.1.1,method:AGLogin,username:cit001,timestamp:1497866722526}";
//        String scr = "5rx9ibpyqdn6xmt3";
//        String enStr = AESEncrypt(ss, scr);
//        System.out.println(enStr);
//        String mingwen = AESDecrypt(enStr, scr);
//        System.out.println(mingwen);
//    }
}
