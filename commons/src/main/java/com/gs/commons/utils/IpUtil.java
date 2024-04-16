package com.gs.commons.utils;

import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class IpUtil {

    public static String getIpDetail(String ip) {
        Searcher searcher = null;
        try {
            Resource resource = new ClassPathResource("ip2region.xdb");
            searcher = Searcher.newWithFileOnly(resource.getFile().getPath());
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
