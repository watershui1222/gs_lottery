package com.gs.task.schedule;

import cn.hutool.core.date.DateUtil;
import com.gs.commons.enums.LotteryCodeEnum;
import com.gs.commons.service.LotteryService;
import com.gs.commons.service.OpenresultJsk3Service;
import com.gs.task.client.LotteryDataClient;
import com.gs.task.service.LotteryDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private OpenresultJsk3Service openresultJsk3Service;

    @Autowired
    private StringRedisTemplate redisTemplate;


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
}
