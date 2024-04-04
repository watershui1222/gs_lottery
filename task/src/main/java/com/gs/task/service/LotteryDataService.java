package com.gs.task.service;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gs.commons.entity.Lottery;
import com.gs.commons.service.LotteryService;
import com.gs.task.enums.LotteryEnum;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class LotteryDataService<T> {
    @Autowired
    private LotteryService lotteryService;


    // 彩种代码
    public abstract LotteryEnum lotteryKindCode();

    // 生成排期数据
    public abstract List<T> getPaiqiData();

    // 获取开奖数据
    public abstract List<T> getResultData();

    // 保存排期数据
    public abstract void savePaiqiData();

    // 保存排期数据
    public abstract void saveOpenData();


    public Lottery getLottery() {
        Lottery lottery = lotteryService.getOne(Wrappers.lambdaQuery(Lottery.class).eq(Lottery::getLotteryCode, lotteryKindCode().getLotteryCode()));
        return lottery;
    }

}
