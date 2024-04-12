package com.gs.settle.schedule;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.business.client.LotteryClient;
import com.gs.business.pojo.LotteryCurrQsBO;
import com.gs.business.service.LotterySettleService;
import com.gs.commons.entity.LotteryOrder;
import com.gs.commons.service.LotteryOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 彩票结算
 */
@Slf4j
@Component
public class LotterySettleSchedule {

    @Autowired
    private LotteryOrderService lotteryOrderService;

    @Autowired
    private LotteryClient lotteryClient;

    @Autowired
    private LotterySettleService lotterySettleService;

    @Scheduled(cron = "0/10 * * * * ?")
    public void settle1() throws Exception {
        // 获取未结算的订单
        Date now = new Date();
        List<LotteryOrder> list = lotteryOrderService.list(
                new LambdaQueryWrapper<LotteryOrder>()
                        .eq(LotteryOrder::getSettleStatus, 0)
                        .eq(LotteryOrder::getOrderStatus, 0)
                        .le(LotteryOrder::getOpenResultTime, now)
        );

        Map<String, LotteryCurrQsBO> openResultMap = new HashMap<>();
        for (LotteryOrder order : list) {
            try {
                // 获取开奖结果
                LotteryCurrQsBO openResult = getOpenResult(openResultMap, order);
                if (openResult == null || openResult.getOpenStatus().intValue() == 1) {
                    // 未开奖
                    log.info("订单号[{}]暂未开奖", order.getOrderNo());
                    continue;
                }
                order.setOpenResult(openResult.getOpenResult());
                // 判断输赢
                lotteryClient.checkWin(order);
                lotterySettleService.settle(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取订单期数开奖结果
     * @param openResultMap
     * @param order
     * @return
     */
    private LotteryCurrQsBO getOpenResult(Map<String, LotteryCurrQsBO> openResultMap, LotteryOrder order) {
        String mapKey = order.getLotteryCode() + "_" + order.getQs();
        LotteryCurrQsBO lotteryCurrQsBO = openResultMap.get(mapKey);
        if (lotteryCurrQsBO == null) {
            lotteryCurrQsBO = lotteryClient.getQsInfo(order.getLotteryCode(), order.getQs());
        }
        openResultMap.put(mapKey, lotteryCurrQsBO);
        return lotteryCurrQsBO;
    }
}
