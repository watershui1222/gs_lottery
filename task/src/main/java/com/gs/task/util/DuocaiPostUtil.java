package com.gs.task.util;

import com.gs.commons.utils.SpringContextHolderUtil;
import com.gs.task.config.LotterySourceProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class DuocaiPostUtil {

    private static final RestTemplate restTemplate;
    static {
        restTemplate = (RestTemplate) SpringContextHolderUtil.getBean("restTemplate");
    }

    public static String post(LotterySourceProperties.SourceMerchants sourceMerchants) {

        String postUrl = StringUtils.join(sourceMerchants.getUrl(), sourceMerchants.getKey(), "/p/", sourceMerchants.getFetchCount(), ".json");
        ResponseEntity<String> entity = restTemplate.getForEntity(postUrl, String.class);
        return "" ;

    }
}
