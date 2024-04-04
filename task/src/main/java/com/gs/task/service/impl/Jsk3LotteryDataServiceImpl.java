package com.gs.task.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.gs.commons.entity.Lottery;
import com.gs.commons.entity.OpenresultJsk3;
import com.gs.commons.enums.LotteryCodeEnum;
import com.gs.commons.service.OpenresultJsk3Service;
import com.gs.commons.utils.RedisKeyUtil;
import com.gs.task.service.LotteryDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class Jsk3LotteryDataServiceImpl extends LotteryDataService<OpenresultJsk3> {

    @Autowired
    private OpenresultJsk3Service openresultJsk3Service;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public LotteryCodeEnum lotteryKindCode() {
        return LotteryCodeEnum.JSK3;
    }

    @Override
    public String getPaiqiQs(Date todayDate, Integer currCount) {
        String todayDateStr = DateUtil.format(todayDate, "YYMMdd");
        String qs = todayDateStr + String.format("%03d", currCount);
        return qs;
    }


    @Override
    public void generatePaiqi(Date today) {
        Lottery lottery = getLottery();
        if (null == lottery) {
            log.info("彩种代码[{}]未开启", lotteryKindCode().getLotteryCode());
            return;
        }
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
            String qs = getPaiqiQs(today, i);
            open.setQs(qs);
            open.setPlatQs(qs);
            open.setOpenResult(null);
            open.setOpenStatus(1);
            open.setCurrCount(i);
            open.setCloseTime(DateUtil.offsetSecond(firstOpenResult, -lottery.getCloseTime()));
            open.setOpenTime(DateUtil.offsetMinute(firstOpenResult, -lottery.getQsTime()));
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
