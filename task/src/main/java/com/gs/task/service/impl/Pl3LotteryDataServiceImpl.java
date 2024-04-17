package com.gs.task.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gs.commons.entity.OpenresultPl3;
import com.gs.commons.enums.LotteryCodeEnum;
import com.gs.commons.service.OpenresultPl3Service;
import com.gs.task.config.LotterySourceProperties;
import com.gs.task.enums.LotterySourceCodeEnum;
import com.gs.task.service.LotteryDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class Pl3LotteryDataServiceImpl extends LotteryDataService {

    @Autowired
    private OpenresultPl3Service openresultPl3Service;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public LotteryCodeEnum lotteryKindCode() {
        return LotteryCodeEnum.PL3;
    }

    @Override
    public String getPaiqiQs(Date todayDate, Integer currCount) {

        return StrUtil.EMPTY;
    }


    @Override
    public void generatePaiqi(Date today) {

    }

    @Override
    public void openResult(LotterySourceProperties.SourceMerchants merchants, JSONObject jsonObject) {

        List<OpenresultPl3> list = new ArrayList<>();

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
            OpenresultPl3 openresultJsk3 = new OpenresultPl3();
            openresultJsk3.setPlatQs(openObj.getString("issue"));
            openresultJsk3.setOpenResult(openObj.getString("code"));
            openresultJsk3.setOpenStatus(0);
            openresultJsk3.setOpenResultTime(new Date());
            openresultJsk3.setUpdateTime(new Date());
            list.add(openresultJsk3);
        }
        openresultPl3Service.batchOpenResult(list);
    }

}
