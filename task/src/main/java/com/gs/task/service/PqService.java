package com.gs.task.service;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gs.commons.entity.Lottery;
import com.gs.commons.service.LotteryService;
import com.gs.task.enums.LotteryKindEnum;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Wrapper;
import java.util.List;

public abstract class PqService<T> {
    @Autowired
    private LotteryService lotteryService;


    public abstract LotteryKindEnum lotteryKindCode();
    public abstract List<T> getData();
    public abstract void saveData();


    public Lottery getLottery() {
        Lottery lottery = lotteryService.getOne(Wrappers.lambdaQuery(Lottery.class).eq(Lottery::getLotteryCode, lotteryKindCode().getLotteryCode()));
        return lottery;
    }

}
