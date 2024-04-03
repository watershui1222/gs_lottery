package com.gs.task.client;

import com.google.common.collect.Maps;
import com.gs.commons.utils.BeanFactoryUtil;
import com.gs.task.service.PqService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

@Component
public class PaiqiClient {

    @Resource
    protected BeanFactoryUtil beanFactoryUtil;
    private Map<String, PqService> paiQiServiceMap;

    @PostConstruct
    private void init() {
        Map<String, PqService> map = beanFactoryUtil.getInstance().getBeansOfType(PqService.class);
        if (map.size() > 0) {
            paiQiServiceMap = Maps.newHashMap();
            map.forEach((key, handle) -> {
                if (handle instanceof PqService) {
                    paiQiServiceMap.put(handle.lotteryKindCode().getLotteryCode(), handle);
                }
            });
        } else {
            this.paiQiServiceMap = Maps.newHashMap();
        }
    }

    public PqService getSourceService(String lotteryCode) {
        return paiQiServiceMap.get(lotteryCode);
    }
}
