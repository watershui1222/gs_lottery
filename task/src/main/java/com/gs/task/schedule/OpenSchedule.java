package com.gs.task.schedule;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gs.commons.entity.OpenresultJsk3;
import com.gs.commons.enums.LotteryCodeEnum;
import com.gs.commons.service.OpenresultJsk3Service;
import com.gs.task.client.LotteryDataClient;
import com.gs.task.config.LotterySourceProperties;
import com.gs.task.enums.LotterySourceEnum;
import com.gs.task.service.LotteryDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private OpenresultJsk3Service openresultJsk3Service;

    private static final String DUOCAI_URL = "https://vip.manycai.com/K2653601ec5af9f/p/5.json";

    @Scheduled(cron = "0/10 * * * * ?")
    public void jsk3Open() {
        LotteryDataService lotteryDataService = lotteryDataClient.getSourceService(LotteryCodeEnum.JSK3.getLotteryCode());
        LotterySourceProperties.SourceMerchants merChant = lotterySourceProperties.getMerChant(LotterySourceEnum.DUOCAI);
        lotteryDataService.openResult(merChant);
    }
}
