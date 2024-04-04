package com.gs.task.schedule;

import com.gs.task.config.LotterySourceProperties;
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
    @Scheduled(cron = "0/10 * * * * ?")
    public void jsk3Open() {
        lotterySourceProperties.getMerchants();
        System.out.println("open");
    }
}
