package com.gs.task.client;

import com.google.common.collect.Maps;
import com.gs.commons.utils.BeanFactoryUtil;
import com.gs.task.service.LotteryDataService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

@Component
public class LotteryDataClient {

    @Resource
    protected BeanFactoryUtil beanFactoryUtil;
    private Map<String, LotteryDataService> lotteryDataServiceMap;

    @PostConstruct
    private void init() {
        Map<String, LotteryDataService> map = beanFactoryUtil.getInstance().getBeansOfType(LotteryDataService.class);
        if (map.size() > 0) {
            lotteryDataServiceMap = Maps.newHashMap();
            map.forEach((key, handle) -> {
                if (handle instanceof LotteryDataService) {
                    lotteryDataServiceMap.put(handle.lotteryKindCode().getLotteryCode(), handle);
                }
            });
        } else {
            this.lotteryDataServiceMap = Maps.newHashMap();
        }
    }

    public LotteryDataService getSourceService(String lotteryCode) {
        return lotteryDataServiceMap.get(lotteryCode);
    }
}
