package com.gs.api.platform.platUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CRUtil {

    /**
     * AES加密
     * @param sSrc
     * @return
     * @throws Exception
     */
    public static String AESEncrypt(String value,String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] raw = key.getBytes("UTF-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
        String base64 = new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码
        String result = base64.replace("\r\n", "");//去掉
        return result;
    }

    /**
     * AES 解密
     * @param sSrc
     * @return
     * @throws Exception
     */
    public static String AESDecrypt(String value,String key) throws Exception {
        try {
            byte[] raw = key.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(value);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "UTF-8");
            return originalString;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}