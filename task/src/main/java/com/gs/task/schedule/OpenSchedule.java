package com.gs.task.schedule;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import com.gs.commons.enums.LotteryCodeEnum;
import com.gs.task.client.LotteryDataClient;
import com.gs.task.config.LotterySourceProperties;
import com.gs.task.enums.LotterySourceEnum;
import com.gs.task.service.LotteryDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每周奖励
 * @author Administrator
 */
@Slf4j
@Component
public class OpenSchedule {


    @Autowired
    private LotterySourceProperties lotterySourceProperties;

    @Autowired
    private LotteryDataClient lotteryDataClient;


    @Scheduled(cron = "0/10 * * * * ?")
    public void jsk3Open() {

        // 多彩数据原
        LotterySourceProperties.SourceMerchants merChant = lotterySourceProperties.getMerChant(LotterySourceEnum.DUOCAI);
        HttpRequest httpRequest = HttpRequest.get(merChant.getUrl());
        HttpResponse httpResponse = httpRequest.execute();
        String body = httpResponse.body();
        JSONObject jsonObject = JSONObject.parseObject(body);

        LotteryDataService lotteryDataService = lotteryDataClient.getSourceService(LotteryCodeEnum.JSK3.getLotteryCode());
        lotteryDataService.openResult(merChant, jsonObject);
    }


    @Scheduled(cron = "0/11 * * * * ?")
    public void cqsscOpen() {
        LotteryDataService lotteryDataService = lotteryDataClient.getSourceService(LotteryCodeEnum.CQSSC.getLotteryCode());
        LotterySourceProperties.SourceMerchants merChant = lotterySourceProperties.getMerChant(LotterySourceEnum.DUOCAI);
        HttpRequest httpRequest = HttpRequest.get(merChant.getUrl());
        HttpResponse httpResponse = httpRequest.execute();
        String body = httpResponse.body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        lotteryDataService.openResult(merChant, jsonObject);
    }
}
