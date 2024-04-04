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

        LotteryDataService k3LotteryDataService = lotteryDataClient.getSourceService(LotteryCodeEnum.JSK3.getLotteryCode());
        k3LotteryDataService.openResult(merChant, jsonObject);


        LotteryDataService cqsscLotteryDataService = lotteryDataClient.getSourceService(LotteryCodeEnum.CQSSC.getLotteryCode());
        cqsscLotteryDataService.openResult(merChant, jsonObject);

        LotteryDataService ftLotteryDataService = lotteryDataClient.getSourceService(LotteryCodeEnum.FT.getLotteryCode());
        ftLotteryDataService.openResult(merChant, jsonObject);

    }



    @Scheduled(cron = "0/10 * * * * ?")
    public void ssxwOpen() {

        // 多彩数据原
        LotterySourceProperties.SourceMerchants merChant = lotterySourceProperties.getMerChant(LotterySourceEnum.DUOCAI);
        HttpRequest httpRequest = HttpRequest.get(merChant.getUrl());
        HttpResponse httpResponse = httpRequest.execute();
        String body = httpResponse.body();
        JSONObject jsonObject = JSONObject.parseObject(body);

        LotteryDataService gd11x5LotteryDataService = lotteryDataClient.getSourceService(LotteryCodeEnum.GD11X5.getLotteryCode());
        gd11x5LotteryDataService.openResult(merChant, jsonObject);


        LotteryDataService bjkl8LotteryDataService = lotteryDataClient.getSourceService(LotteryCodeEnum.BJKL8.getLotteryCode());
        bjkl8LotteryDataService.openResult(merChant, jsonObject);

        LotteryDataService pcddLotteryDataService = lotteryDataClient.getSourceService(LotteryCodeEnum.PCDD.getLotteryCode());
        pcddLotteryDataService.openResult(merChant, jsonObject);


    }


    @Scheduled(cron = "0/10 * * * * ?")
    public void bjpk10Open() {

        // 多彩数据原
        LotterySourceProperties.SourceMerchants merChant = lotterySourceProperties.getMerChant(LotterySourceEnum.DUOCAI);
        HttpRequest httpRequest = HttpRequest.get(merChant.getUrl());
        HttpResponse httpResponse = httpRequest.execute();
        String body = httpResponse.body();
        JSONObject jsonObject = JSONObject.parseObject(body);

        LotteryDataService bjpk10LotteryDataService = lotteryDataClient.getSourceService(LotteryCodeEnum.BJPK10.getLotteryCode());
        bjpk10LotteryDataService.openResult(merChant, jsonObject);



        LotteryDataService molhcLotteryDataService = lotteryDataClient.getSourceService(LotteryCodeEnum.MO6HC.getLotteryCode());
        molhcLotteryDataService.openResult(merChant, jsonObject);


//        LotteryDataService fc3dLotteryDataService = lotteryDataClient.getSourceService(LotteryCodeEnum.FC3D.getLotteryCode());
//        fc3dLotteryDataService.openResult(merChant, jsonObject);

    }






}
