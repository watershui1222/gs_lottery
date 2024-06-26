package com.gs.task.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSONObject;
import com.gs.commons.entity.Lottery;
import com.gs.commons.entity.OpenresultGs1mssc;
import com.gs.commons.enums.LotteryCodeEnum;
import com.gs.commons.service.OpenresultGs1msscService;
import com.gs.commons.utils.RedisKeyUtil;
import com.gs.task.config.LotterySourceProperties;
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
public class Gs1msscLotteryDataServiceImpl extends LotteryDataService {

    @Autowired
    private OpenresultGs1msscService openresultGs1msscService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public LotteryCodeEnum lotteryKindCode() {
        return LotteryCodeEnum.GS1MSSC;
    }

    @Override
    public String getPaiqiQs(Date todayDate, Integer currCount) {
        String todayDateStr = DateUtil.format(todayDate, "yyyyMMdd");
        String qs = todayDateStr + "-" + String.format("%04d", currCount);
        return qs;
    }


    @Override
    public void generatePaiqi(Date today) {
        // 判断当前日期是否进行排期
        String paiqiKey = RedisKeyUtil.PaiqiGenerateKey(lotteryKindCode().getLotteryCode(), today);
        Boolean hasKey = redisTemplate.hasKey(paiqiKey);
        if (hasKey) {
            log.info("彩种代码[{}]已完成[{}]排期", lotteryKindCode().getLotteryCode(), DateUtil.formatDate(today));
            return;
        }
        Lottery lottery = getLottery();
        if (null == lottery) {
            log.info("彩种代码[{}]未开启", lotteryKindCode().getLotteryCode());
            return;
        }
        Date now = new Date();
        String todayDateStr = DateUtil.format(today, "YYMMdd");

        DateTime firstOpenResult = DateUtil.parseDateTime(DateUtil.formatDate(today) + " " + lottery.getFirstQsTime());
        List<OpenresultGs1mssc> paiqiList = new ArrayList<>();

        for (Integer i = 1; i <= lottery.getDayCount(); i++) {
            OpenresultGs1mssc open = new OpenresultGs1mssc();
            String qs = getPaiqiQs(today, i);
            open.setQs(qs);
            open.setPlatQs(qs);
            open.setOpenResult(RandomUtil.randomInt(0, 10) + "," + RandomUtil.randomInt(0, 10) + "," + RandomUtil.randomInt(0, 10) + ","  + RandomUtil.randomInt(0, 10) + "," + RandomUtil.randomInt(0, 10));
            open.setOpenStatus(0);
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

        openresultGs1msscService.saveBatch(paiqiList);
        redisTemplate.opsForValue().set(paiqiKey, "true", 2, TimeUnit.DAYS);
    }

    @Override
    public void openResult(LotterySourceProperties.SourceMerchants merchants, JSONObject jsonObject) {

//        List<OpenresultCqssc> list = new ArrayList<>();
//
//        // 获取对应上游彩种代码
//        LotterySourceCodeEnum sourceCodeEnum = LotterySourceCodeEnum.getLotterySourceCode(merchants.getCode(), lotteryKindCode().getLotteryCode());
//        if (null == sourceCodeEnum) {
//            log.info("彩种[{}]未配置上游代码LotterySourceCodeEnum", lotteryKindCode().getLotteryCode());
//            return;
//        }
//
//        String lotterySourceLotteryCode = sourceCodeEnum.getLotterySourceLotteryCode();
//
//
//        JSONArray jsks = jsonObject.getJSONArray(lotterySourceLotteryCode);
//        for (int i = 0; i < jsks.size(); i++) {
//            JSONObject openObj = jsks.getJSONObject(i);
//            OpenresultCqssc openresultJsk3 = new OpenresultCqssc();
//            openresultJsk3.setPlatQs(openObj.getString("issue"));
//            openresultJsk3.setOpenResult(openObj.getString("code"));
//            openresultJsk3.setOpenStatus(0);
//            openresultJsk3.setOpenResultTime(new Date());
//            openresultJsk3.setUpdateTime(new Date());
//            list.add(openresultJsk3);
//        }
//        openresultCqsscService.batchOpenResult(list);
    }


}
