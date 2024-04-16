package com.gs.commons.utils;

import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.util.ResourceUtils;

import java.io.File;

public class IpUtil {

    public static String getIpDetail(String ip) {
        Searcher searcher = null;
        try {
            File file = ResourceUtils.getFile("classpath:ip2region.xdb");
            String dbPath = file.getPath();
            searcher = Searcher.newWithFileOnly(dbPath);
            return searcher.search(ip);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 3、关闭资源
            try {
                if (null != searcher) {
                    searcher.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "未知";
    }
}
