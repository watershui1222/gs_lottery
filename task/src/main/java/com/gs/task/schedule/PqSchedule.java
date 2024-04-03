package com.gs.task.schedule;

import com.gs.task.client.PaiqiClient;
import com.gs.task.enums.LotteryKindEnum;
import com.gs.task.service.PqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 每周奖励
 * @author Administrator
 */
@Slf4j
@Component
public class PqSchedule {

    @Autowired
    private PaiqiClient paiqiClient;
    @Scheduled(cron = "0/50 * * * * ?")
    public void jsk3Paiqi() {
        PqService pqService = paiqiClient.getSourceService(LotteryKindEnum.JSK3.getLotteryCode());
        pqService.saveData();
        System.out.println("123");
    }
}
