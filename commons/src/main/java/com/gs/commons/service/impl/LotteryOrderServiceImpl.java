package com.gs.commons.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.EduOrder;
import com.gs.commons.entity.LotteryOrder;
import com.gs.commons.service.LotteryOrderService;
import com.gs.commons.mapper.LotteryOrderMapper;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
* @author 69000
* @description 针对表【t_lottery_order】的数据库操作Service实现
* @createDate 2024-04-03 17:30:07
*/
@Service
public class LotteryOrderServiceImpl extends ServiceImpl<LotteryOrderMapper, LotteryOrder>
    implements LotteryOrderService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<LotteryOrder> wrapper = new QueryWrapper<LotteryOrder>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), LotteryOrder::getUserName, userName);

        String platCode = MapUtil.getStr(params, "lotteryCode");
        wrapper.eq(StringUtils.isNotBlank(platCode), LotteryOrder::getLotteryCode, platCode);

        Integer orderStatus = MapUtil.getInt(params, "orderStatus");
        wrapper.eq(null != orderStatus, LotteryOrder::getOrderStatus, orderStatus);

        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(startTime != null, LotteryOrder::getBetTime, startTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        wrapper.le(endTime != null, LotteryOrder::getBetTime, endTime);

        wrapper.orderByDesc(LotteryOrder::getBetTime);
        IPage<LotteryOrder> page = this.page(
                new Query<LotteryOrder>().getPage(params),
                wrapper);

        return new PageUtils(page);
    }
}




