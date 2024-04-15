package com.gs.innerapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.business.client.LotteryClient;
import com.gs.business.pojo.bo.LotteryCurrQsBO;
import com.gs.business.service.LotteryBetService;
import com.gs.commons.entity.LotteryOrder;
import com.gs.commons.service.LotteryOrderService;
import com.gs.commons.service.LotteryService;
import com.gs.commons.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "彩票相关", tags = "彩票相关")
@RequestMapping("/lottery")
@RestController
public class LotteryController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private LotteryOrderService lotteryOrderService;
    @Autowired
    private LotteryBetService lotteryBetService;
    @Autowired
    private LotteryClient lotteryClient;

    @ApiOperation(value = "取消订单")
    @GetMapping("/cancel/{orderNo}")
    public R cancelOrder(@PathVariable("orderNo") String orderNo) throws Exception {
        // 查询订单信息
        LotteryOrder lotteryOrder = lotteryOrderService.getOne(
                new LambdaQueryWrapper<LotteryOrder>()
                        .eq(LotteryOrder::getOrderNo, orderNo)
        );
        if (lotteryOrder == null || lotteryOrder.getOrderStatus().intValue() != 0) {
            throw new Exception("未获取到订单[" + orderNo + "]信息或该订单已完成!");
        }
        // 查询当前期
        LotteryCurrQsBO currQs = lotteryClient.getCurrQs(lotteryOrder.getLotteryCode());
        if (!StringUtils.equals(lotteryOrder.getQs(), currQs.getQs())) {
            return R.error("已封盘,无法撤销订单");
        }
        // 修改订单
        lotteryBetService.cancel(lotteryOrder);
        return R.ok();
    }
}
