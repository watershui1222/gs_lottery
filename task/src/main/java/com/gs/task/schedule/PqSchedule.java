package com.gs.task.schedule;

import com.gs.task.client.LotteryDataClient;
import com.gs.task.enums.LotteryEnum;
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
public class PqSchedule {

    @Autowired
    private LotteryDataClient lotteryDataClient;
    @Scheduled(cron = "0/30 * * * * ?")
    public void jsk3Paiqi() {
        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryEnum.JSK3.getLotteryCode());
        pqService.savePaiqiData();
        System.out.println("123");
    }
}
