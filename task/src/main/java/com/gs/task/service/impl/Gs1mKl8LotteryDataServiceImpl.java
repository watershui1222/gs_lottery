package com.gs.task.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSONObject;
import com.gs.commons.entity.Lottery;
import com.gs.commons.entity.OpenresultGs1mkl8;
import com.gs.commons.enums.LotteryCodeEnum;
import com.gs.commons.service.OpenresultGs1mkl8Service;
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
public class Gs1mKl8LotteryDataServiceImpl extends LotteryDataService {

    @Autowired
    private OpenresultGs1mkl8Service openresultGs1mkl8Service;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private List<Integer> numbers = ListUtil.of(
            1, 2, 3, 4, 5, 6, 7, 8
            , 9, 10, 11, 12, 13, 14, 15, 16
            , 17, 18, 19, 20, 21, 22, 23, 24
            , 25, 26, 27, 28, 29, 30, 31, 32
            , 33, 34, 35, 36, 37, 38, 39, 40
            , 41, 42, 43, 44, 45, 46, 47, 48
            , 49, 50, 51, 52, 53, 54, 55, 56
            , 57, 58, 59, 60, 61, 62, 63, 64
            , 65, 66, 67, 68, 69, 70, 71, 72
            , 73, 74, 75, 76, 77, 78, 79, 80);


    @Override
    public LotteryCodeEnum lotteryKindCode() {
        return LotteryCodeEnum.GS1MKL8;
    }

    @Override
    public String getPaiqiQs(Date todayDate, Integer currCount) {
        String todayDateStr = DateUtil.format(todayDate, "YYMMdd");
        String qs = todayDateStr + String.format("%04d", currCount);
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
        List<OpenresultGs1mkl8> paiqiList = new ArrayList<>();
        for (Integer i = 1; i <= lottery.getDayCount(); i++) {
            OpenresultGs1mkl8 open = new OpenresultGs1mkl8();
            String qs = getPaiqiQs(today, i);
            open.setQs(qs);
            open.setPlatQs(qs);
            List<Integer> integers = RandomUtil.randomEleList(numbers, 20);

            String result = CollUtil.join(integers, ",");
            open.setOpenResult(result);
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
        openresultGs1mkl8Service.saveBatch(paiqiList);
        redisTemplate.opsForValue().set(paiqiKey, "true", 2, TimeUnit.DAYS);
    }

    @Override
    public void openResult(LotterySourceProperties.SourceMerchants merchants, JSONObject jsonObject) {

//        List<OpenresultBjpk10> list = new ArrayList<>();
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
//            OpenresultBjpk10 openresultJsk3 = new OpenresultBjpk10();
//            openresultJsk3.setPlatQs(openObj.getString("issue"));
//            openresultJsk3.setOpenResult(openObj.getString("code"));
//            openresultJsk3.setOpenStatus(0);
//            openresultJsk3.setOpenResultTime(new Date());
//            openresultJsk3.setUpdateTime(new Date());
//            list.add(openresultJsk3);
//        }
//        openresultBjpk10Service.batchOpenResult(list);
    }


}
