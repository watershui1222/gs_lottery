package com.gs.commons.utils;

import cn.hutool.http.HttpUtil;
import org.lionsoul.ip2region.xdb.Searcher;

public class IpUtil {

    private static byte[] BUFF = null;

    static {
        BUFF = HttpUtil.downloadBytes("https://juhaijituan.oss-ap-southeast-1.aliyuncs.com/gs/ip2region.xdb");
    }

    public static String getIpDetail(String ip) {
        try {
            Searcher searcher = Searcher.newWithBuffer(BUFF);
            return searcher.search(ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "未知";
    }
}
