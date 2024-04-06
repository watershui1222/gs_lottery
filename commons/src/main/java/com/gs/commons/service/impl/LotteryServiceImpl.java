package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.Lottery;
import com.gs.commons.service.LotteryService;
import com.gs.commons.mapper.LotteryMapper;
import org.springframework.stereotype.Service;

/**
* @author 69000
* @description 针对表【t_lottery】的数据库操作Service实现
* @createDate 2024-04-04 11:39:45
*/
@Service
public class LotteryServiceImpl extends ServiceImpl<LotteryMapper, Lottery>
    implements LotteryService{

    @Override
    public Lottery getLotteryInfo(String lotteryCode) {
        return this.getOne(new LambdaQueryWrapper<Lottery>().eq(Lottery::getLotteryCode, lotteryCode));
    }
}




