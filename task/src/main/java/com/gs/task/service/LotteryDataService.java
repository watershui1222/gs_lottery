package com.gs.task.service;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gs.commons.entity.Lottery;
import com.gs.commons.enums.LotteryCodeEnum;
import com.gs.commons.service.LotteryService;
import com.gs.task.config.LotterySourceProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public abstract class LotteryDataService<T> {
    @Autowired
    private LotteryService lotteryService;


    // 彩种代码
    public abstract LotteryCodeEnum lotteryKindCode();

    public abstract String getPaiqiQs(Date todayDate, Integer currCount);

    // 生成排期数据
    public abstract void generatePaiqi(Date today);

    // 生成开奖结果
    public abstract void openResult(LotterySourceProperties.SourceMerchants merchants);


    public Lottery getLottery() {
        Lottery lottery = lotteryService.getOne(Wrappers.lambdaQuery(Lottery.class)
                .eq(Lottery::getLotteryCode, lotteryKindCode().getLotteryCode())
                .eq(Lottery::getStatus, 0)
        );
        return lottery;
    }

}
