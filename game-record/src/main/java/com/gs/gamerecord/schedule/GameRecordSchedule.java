package com.gs.gamerecord.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每周奖励
 * @author Administrator
 */
@Slf4j
@Component
public class GameRecordSchedule {

    @Value("${test.name}")
    private String name;

    @Scheduled(cron = "0/10 * * * * ?")
    public void kyRecord() {
        System.out.println(name);
        log.info("开元拉单");
    }
}
