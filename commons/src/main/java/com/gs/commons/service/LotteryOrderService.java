package com.gs.commons.service;

import com.gs.commons.entity.LotteryOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.utils.PageUtils;

import java.util.Map;

/**
* @author 69000
* @description 针对表【t_lottery_order】的数据库操作Service
* @createDate 2024-04-03 17:30:07
*/
public interface LotteryOrderService extends IService<LotteryOrder> {
    PageUtils queryPage(Map<String, Object> params);
}
