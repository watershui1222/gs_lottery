package com.gs.task.schedule;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.commons.entity.Lottery;
import com.gs.commons.entity.OpenresultJsk3;
import com.gs.commons.service.LotteryService;
import com.gs.commons.service.OpenresultJsk3Service;
import com.gs.commons.utils.RedisKeyUtil;
import com.gs.task.client.LotteryDataClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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


    @Scheduled(cron = "0/30 * * * * ?")
    public void jsk3Paiqi() {
//        LotteryDataService pqService = lotteryDataClient.getSourceService(LotteryEnum.JSK3.getLotteryCode());
//        pqService.savePaiqiData();
//        System.out.println("123");
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void jsk3Paiqi1() {
        // 获取彩种信息
        Lottery lottery = lotteryService.getOne(
                new LambdaQueryWrapper<Lottery>()
                        .eq(Lottery::getLotteryCode, "JSK3")
                        .eq(Lottery::getStatus, 0)
        );
        if (lottery == null) {
            return;
        }

        // 今日排期
        generateJsk3Paiqi(lottery, new Date());
        // 明日排期
        generateJsk3Paiqi(lottery, DateUtil.tomorrow());
    }

    private void generateJsk3Paiqi(Lottery lottery, Date today) {
        // 判断当前日期是否进行排期
        String paiqiKey = RedisKeyUtil.PaiqiGenerateKey(lottery.getLotteryCode(), today);
        Boolean hasKey = redisTemplate.hasKey(paiqiKey);
        if (hasKey) {
            log.info("彩种代码[{}]已完成[{}]排期", lottery.getLotteryCode(), DateUtil.formatDate(today));
            return;
        }

        Date now = new Date();
        String todayDateStr = DateUtil.format(today, "YYMMdd");

        DateTime firstOpenResult = DateUtil.parseDateTime(DateUtil.formatDate(today) + " " + lottery.getFirstQsTime());
        List<OpenresultJsk3> paiqiList = new ArrayList<>();
        for (Integer i = 1; i <= lottery.getDayCount(); i++) {
            OpenresultJsk3 open = new OpenresultJsk3();
            String qs = todayDateStr + String.format("%03d", i);
            open.setQs(qs);
            open.setPlatQs(qs);
            open.setOpenResult(null);
            open.setOpenStatus(1);
            open.setCurrCount(i);
            open.setCloseTime(DateUtil.offsetSecond(firstOpenResult, -lottery.getCloseTime()));
            open.setOpenTime(DateUtil.offsetMinute(open.getCloseTime(), -lottery.getQsTime()));
            open.setOpenResultTime(firstOpenResult);
            open.setCreateTime(now);
            open.setUpdateTime(now);
            paiqiList.add(open);
            // 下一期开奖时间
            firstOpenResult = DateUtil.offsetMinute(open.getOpenResultTime(), lottery.getQsTime());
        }
        openresultJsk3Service.saveBatch(paiqiList);
        redisTemplate.opsForValue().set(paiqiKey, "true", 2, TimeUnit.DAYS);
    }
}
