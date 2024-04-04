package com.gs.task.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(
        prefix = "lottery.source"
)
public class LotterySourceProperties {
    private Map<String, LotterySourceProperties.SourceMerchants> merchants;
    @Data
    public static class SourceMerchants {


        private String key;
        private String name;
    }

    public void setMerchants(Map<String, SourceMerchants> merchants) {
        this.merchants = merchants;
    }

    public Map<String, SourceMerchants> getMerchants() {
        return merchants;
    }
}
