package com.gs.business.utils.pay;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class MkpayUtil {

    /**
     * 生成商户请求签名
     *
     * @param paramMap  待签名数据
     * @param mchSecret 商户秘钥
     * @return 签名
     */
    public static String getNeedSignParamString(Map<String, Object> paramMap, String mchSecret) {
        SortedMap<String, Object> data = new TreeMap<>(paramMap);
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
        sb.append(mchSecret);
        return sb.toString();
    }
}
