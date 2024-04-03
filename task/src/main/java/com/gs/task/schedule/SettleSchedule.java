package com.gs.task.schedule;

import com.gs.task.service.SettleService;
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
public class SettleSchedule {


    @Autowired
    private SettleService settleService;

    @Scheduled(cron = "0/10 * * * * ?")
    public void settle() {
        System.out.println("123");
    }
}
