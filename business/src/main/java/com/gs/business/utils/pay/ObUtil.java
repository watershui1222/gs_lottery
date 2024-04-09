package com.gs.business.utils.pay;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ObUtil {

    public static String sortData(Map<String, ?> sourceMap, String link) {
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
}
