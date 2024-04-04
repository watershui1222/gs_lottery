package com.gs.task.schedule;

import cn.hutool.core.date.DateUtil;
import com.gs.commons.enums.LotteryCodeEnum;
import com.gs.task.client.LotteryDataClient;
import com.gs.task.service.LotteryDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 每周奖励
 * @author Administrator
 */
@Slf4j
@Component
public class PqSchedule {

    @Autowired
    private LotteryDataClient lotteryDataClient;


    @Scheduled(cron = "0/10 * * * * ?")
    public void jsk3Paiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.JSK3.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }


    @Scheduled(cron = "0/10 * * * * ?")
    public void cqsscPaiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.CQSSC.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void ftPaiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.FT.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }


    @Scheduled(cron = "0/10 * * * * ?")
    public void gd11x5Paiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.GD11X5.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void bjkl8Paiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.BJKL8.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }


    @Scheduled(cron = "0/10 * * * * ?")
    public void pcddPaiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.PCDD.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void bjpk10Paiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.BJPK10.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }


    @Scheduled(cron = "0/10 * * * * ?")
    public void mo6hcPaiqi1() {

        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.MO6HC.getLotteryCode());

        pqService.generatePaiqi(new Date());
        pqService.generatePaiqi(DateUtil.tomorrow());


    }


//    @Scheduled(cron = "0/10 * * * * ?")
//    public void fc3dPaiqi1() {
//
//        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryCodeEnum.FC3D.getLotteryCode());
//
//        pqService.generatePaiqi(new Date());
//        pqService.generatePaiqi(DateUtil.tomorrow());
//
//
//    }


}
