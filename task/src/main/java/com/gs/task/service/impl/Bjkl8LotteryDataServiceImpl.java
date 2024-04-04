package com.gs.task.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gs.commons.entity.Lottery;
import com.gs.commons.entity.OpenresultBjkl8;
import com.gs.commons.entity.OpenresultJsk3;
import com.gs.commons.enums.LotteryCodeEnum;
import com.gs.commons.service.OpenresultBjkl8Service;
import com.gs.commons.utils.RedisKeyUtil;
import com.gs.task.config.LotterySourceProperties;
import com.gs.task.enums.LotterySourceCodeEnum;
import com.gs.task.service.LotteryDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class Bjkl8LotteryDataServiceImpl extends LotteryDataService<OpenresultJsk3> {

    @Autowired
    private OpenresultBjkl8Service openresultBjkl8Service;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${bjkl8.yesterday-qs}")
    private Integer yesterdayQs;

    @Override
    public LotteryCodeEnum lotteryKindCode() {
        return LotteryCodeEnum.BJKL8;
    }

    @Override
    public String getPaiqiQs(Date todayDate, Integer currCount) {
        return String.valueOf(currCount);
    }


    @Override
    public void generatePaiqi(Date today) {
        Lottery lottery = getLottery();
        if (null == lottery) {
            log.info("彩种代码[{}]未开启", lotteryKindCode().getLotteryCode());
            return;
        }


        // 昨日最后一期期数
        String yesterdayQsKey = RedisKeyUtil.bjkl8YesterdayQs(lottery.getLotteryCode(), DateUtil.offsetDay(today, -1));
        String yesterdayQsValue = redisTemplate.opsForValue().get(yesterdayQsKey);
        if (StringUtils.isEmpty(yesterdayQsValue)) {
            yesterdayQsValue = String.valueOf(yesterdayQs);
        }
        Integer qsValue = Integer.valueOf(yesterdayQsValue);

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
        List<OpenresultBjkl8> paiqiList = new ArrayList<>();
        for (Integer i = 1; i <= lottery.getDayCount(); i++) {
            qsValue++;
            OpenresultBjkl8 open = new OpenresultBjkl8();
            String qs = getPaiqiQs(today, qsValue);
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
        String todayQsKey = RedisKeyUtil.bjkl8YesterdayQs(lottery.getLotteryCode(), today);
        redisTemplate.opsForValue().set(todayQsKey, String.valueOf(qsValue), 2, TimeUnit.DAYS);
        openresultBjkl8Service.saveBatch(paiqiList);
        redisTemplate.opsForValue().set(paiqiKey, "true", 2, TimeUnit.DAYS);
    }

    @Override
    public void openResult(LotterySourceProperties.SourceMerchants merchants, JSONObject jsonObject) {

        List<OpenresultBjkl8> list = new ArrayList<>();

        // 获取对应上游彩种代码
        LotterySourceCodeEnum sourceCodeEnum = LotterySourceCodeEnum.getLotterySourceCode(merchants.getCode(), lotteryKindCode().getLotteryCode());
        if (null == sourceCodeEnum) {
            log.info("彩种[{}]未配置上游代码LotterySourceCodeEnum", lotteryKindCode().getLotteryCode());
            return;
        }

        String lotterySourceLotteryCode = sourceCodeEnum.getLotterySourceLotteryCode();


        JSONArray jsks = jsonObject.getJSONArray(lotterySourceLotteryCode);
        for (int i = 0; i < jsks.size(); i++) {
            JSONObject openObj = jsks.getJSONObject(i);
            OpenresultBjkl8 openresultJsk3 = new OpenresultBjkl8();
            openresultJsk3.setPlatQs(openObj.getString("issue"));
            openresultJsk3.setOpenResult(openObj.getString("code"));
            openresultJsk3.setOpenStatus(0);
            openresultJsk3.setOpenResultTime(new Date());
            openresultJsk3.setUpdateTime(new Date());
            list.add(openresultJsk3);
        }
        openresultBjkl8Service.batchOpenResult(list);
    }
}
