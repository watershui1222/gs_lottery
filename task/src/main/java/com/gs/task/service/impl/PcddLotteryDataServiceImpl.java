package com.gs.task.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.gs.commons.entity.Lottery;
import com.gs.commons.entity.OpenresultBjkl8;
import com.gs.commons.entity.OpenresultJsk3;
import com.gs.commons.entity.OpenresultPcdd;
import com.gs.commons.enums.LotteryCodeEnum;
import com.gs.commons.service.OpenresultPcddService;
import com.gs.commons.utils.RedisKeyUtil;
import com.gs.task.config.LotterySourceProperties;
import com.gs.task.enums.LotterySourceCodeEnum;
import com.gs.task.service.LotteryDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PcddLotteryDataServiceImpl extends LotteryDataService {

    @Autowired
    private OpenresultPcddService openresultPcddService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public LotteryCodeEnum lotteryKindCode() {
        return LotteryCodeEnum.PCDD;
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
        LambdaQueryWrapper<OpenresultPcdd> wrapper =
                new LambdaQueryWrapper<OpenresultPcdd>()
                        .orderByDesc(OpenresultPcdd::getOpenResultTime);

        Page<OpenresultPcdd> page = openresultPcddService.page(new Page<>(1, 1), wrapper);
        List<OpenresultPcdd> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            log.info("PCDD未获取到昨日最后一期");
            return;
        }
        Integer qsValue = Integer.valueOf(records.get(0).getPlatQs());

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
        List<OpenresultPcdd> paiqiList = new ArrayList<>();
        for (Integer i = 1; i <= lottery.getDayCount(); i++) {
            qsValue++;
            OpenresultPcdd open = new OpenresultPcdd();
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
        openresultPcddService.saveBatch(paiqiList);
        redisTemplate.opsForValue().set(paiqiKey, "true", 2, TimeUnit.DAYS);
    }

    @Override
    public void openResult(LotterySourceProperties.SourceMerchants merchants, JSONObject jsonObject) {

        List<OpenresultPcdd> list = new ArrayList<>();

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
            OpenresultPcdd openresultJsk3 = new OpenresultPcdd();
            openresultJsk3.setPlatQs(openObj.getString("issue"));

            // pc蛋蛋计算开奖结果
            String code = openObj.getString("code");
            List<String> codeList = Arrays.asList(StringUtils.split(code, ","));
            List<String> codeList1 = Lists.newArrayList(codeList.subList(0, 5));
            List<String> codeList2 = Lists.newArrayList(codeList.subList(6, 11));
            List<String> codeList3 = Lists.newArrayList(codeList.subList(12, 17));

            int code1 = codeList1.stream().mapToInt(Integer::valueOf).sum() % 10;
            int code2 = codeList2.stream().mapToInt(Integer::valueOf).sum() % 10;
            int code3 = codeList3.stream().mapToInt(Integer::valueOf).sum() % 10;
            String openResult = StringUtils.join(code1, ",", code2, ",", code3);

            openresultJsk3.setOpenResult(openResult);
            openresultJsk3.setOpenStatus(0);
            openresultJsk3.setOpenResultTime(new Date());
            openresultJsk3.setUpdateTime(new Date());
            list.add(openresultJsk3);
        }
        openresultPcddService.batchOpenResult(list);
    }

    public static void main(String[] args) {
//        Integer[] a = {1, 2, 6, 11, 14, 20, 26, 34, 35, 39, 41, 46, 47, 53, 55, 56, 61, 66, 67, 72};
//        List<Integer> codeList = Arrays.asList(a);
//        List<Integer> codeList1 = Lists.newArrayList(codeList.subList(0, 5));
//        List<Integer> codeList2 = Lists.newArrayList(codeList.subList(6, 11));
//        List<Integer> codeList3 = Lists.newArrayList(codeList.subList(12, 17));
//        int code1 = codeList1.stream().mapToInt(Integer::valueOf).sum() % 10;
//        int code2 = codeList2.stream().mapToInt(Integer::valueOf).sum() % 10;
//        int code3 = codeList3.stream().mapToInt(Integer::valueOf).sum() % 10;
//        String openResult = StringUtils.join(code1, ",", code2, ",", code3);
//        System.out.println(openResult);
    }

}


