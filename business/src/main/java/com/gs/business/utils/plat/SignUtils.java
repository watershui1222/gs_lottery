package com.gs.business.utils.plat;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class SignUtils {


    /**
     * 按照集合的key来排序
     * @param param
     * @param
     * @return
     */
    public static String sortParam(JSONObject param){
        SortedMap<String, Object> data = new TreeMap<>(param);
        Iterator<String> iterator = data.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = data.get(key);
            if (Objects.nonNull(value) && String.valueOf(value).trim().length() > 0) {
                if (!first) {
                    sb.append("&");
                }
                sb.append(key).append("=").append(value);
                first = false;
            }
        }
        return sb.toString();
    }

    public static String fbSportSign(JSONObject param, String channelId, Long timestamp, String channelSecret){
        List<String> keys = new ArrayList<>(param.keySet());
        // 对键进行升序排序
        Collections.sort(keys);
        JSONObject sortedJson = new JSONObject();
        for (String key : keys) {
            sortedJson.put(key, param.get(key));
        }
        StringBuilder sb = new StringBuilder();
        sb.append(sortedJson).append(".").append(channelId).append(".").append(timestamp).append(".").append(channelSecret);
        String sign = DigestUtil.md5Hex(sb.toString());
        return sign;
    }

    /**
     * ebet sign
     * @param content
     * @param privateKey
     * @param publicKey
     * @return
     */
    public static String eBetSign(String content, String privateKey, String publicKey){
        Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA, privateKey, publicKey);
        byte[] signByte = sign.sign(content);
        return Base64.encode(signByte);
    }

    /**
     * ebet sign
     * @param content
     * @param privateKey
     * @param publicKey
     * @return
     */
    public static boolean eBetverify(String signature, String content, String privateKey, String publicKey){
        Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA, privateKey, publicKey);
        byte[] signByte = sign.sign(content);
        String gSignStr = Base64.encode(signByte);
        boolean result = StrUtil.equals(gSignStr, signature);
        if(!result){
            log.error("eBet(we) 登录回调验签失败 signature:[{}] gSignStr:[{}] content:[{}]",signature,gSignStr,content);
            log.error("eBet(we) 登录回调验签失败 privateKey:[{}] publicKey:[{}]",privateKey,publicKey);
        }
        return result;
    }

}
