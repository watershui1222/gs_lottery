package com.gs.settle.schedule;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gs.business.client.LotteryClient;
import com.gs.business.pojo.LotteryCurrQsBO;
import com.gs.business.service.LotterySettleService;
import com.gs.commons.entity.LotteryOrder;
import com.gs.commons.service.LotteryOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
//                        .le(LotteryOrder::getOpenResultTime, now)
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
                checkWin(order);
                lotterySettleService.settle(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断输赢 计算奖金
     * @param order
     */
    private void checkWin(LotteryOrder order) {
        String betContent = order.getBetContent();
        if (StringUtils.equals(order.getLotteryCode(), "JSK3")) {
            String openResult = order.getOpenResult();
            String[] resultArr = openResult.split(",");
            int sum = NumberUtil.parseInt(resultArr[0]) + NumberUtil.parseInt(resultArr[1]) + NumberUtil.parseInt(resultArr[2]);
            if (StringUtils.equals(order.getPlayCode(), "hz_hz")) { // 和值
                //    开奖号码总和值为4、5、6、7、8、9、10、11、12、13、14、15、16、17 时，即为中奖；
                //    若开出3、18，则不算中奖。举例：如开奖号码为1、2、3、总和值为6、则投注「6」即为中奖。
                if (sum == NumberUtil.parseInt(betContent)) {
                    order.setOrderStatus(1);
                } else {
                    order.setOrderStatus(2);
                }
            } else if (StringUtils.equals(order.getPlayCode(), "sjdx_sjdx")) { // 三军、大小
                //三个开奖号码其中一个与所选号码相同时、即为中奖。
                // 举例：如开奖号码为1、1、3，则投注三军1或三军3皆视为中奖。 备注：不论当局指定点数出现几次，仅派彩一次(不翻倍)
                if (StringUtils.equalsAny(betContent, "1", "2", "3", "4", "5", "6")) {
                    if (StringUtils.equalsAny(betContent, resultArr[0], resultArr[1], resultArr[2])) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equals(betContent, "大")) {
                    // 大小：三个开奖号码总和值11~18 为大；总和值3~10 为小；若三个号码相同、亦算大小。
                    if (sum >= 11 && sum <= 18) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equals(betContent, "小")) {
                    if (sum >= 3 && sum <= 10) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equals(betContent, "单")) {
                    // 单双：三个开奖号码总和值3,5,7,9,11,13,15,17为单；总和值4,6,8,10,12,14,16,18为双；若三个号码相同、亦算单双。
                    if (sum == 3 || sum == 5 || sum == 7 || sum == 9 || sum == 11 || sum == 13 || sum == 15 || sum == 17) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equals(betContent, "双")) {
                    if (sum == 4 || sum == 6 || sum == 8 || sum == 10 || sum == 12 || sum == 14 || sum == 16 || sum == 18) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                }

            } else if (StringUtils.equals(order.getPlayCode(), "wtqt_wtqt")) { // 围骰、全骰
                String numStr = resultArr[0] + resultArr[1] + resultArr[2];
                if (StringUtils.equals("全骰", betContent)) {
                    if (StringUtils.equals(resultArr[0], resultArr[1]) && StringUtils.equals(resultArr[1], resultArr[2])) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equalsAny(betContent, "111", "222", "333", "444", "555", "666")) {
                    if (StringUtils.equals(numStr, betContent)) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                }
            }
        } else if (StringUtils.equals(order.getLotteryCode(), "PCDD")) {
            String openResult = order.getOpenResult();
            String[] resultArr = openResult.split(",");
            int sum = NumberUtil.parseInt(resultArr[0]) + NumberUtil.parseInt(resultArr[1]) + NumberUtil.parseInt(resultArr[2]);
            if (StringUtils.equals(order.getPlayCode(), "hunhe_hunhe")) { // 混合
                if (StringUtils.equals(order.getBetContent(), "大")) {
                    // 大：三个位置的数值相加和大于15,16,17,18,19,20,21,22,23,24,25,26,27为大。注：买100元大开14退回本金。
                    if (sum == 14) {
                        order.setOrderStatus(4);
                    } else if (sum >= 15) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equals(order.getBetContent(), "小")) {
                    //小：三个位置的数值相加和小于00,01,02,03,04,05,06,07,08,09,10,11,12为小。注：买100元小开13退回本金。
                    if (sum == 13) {
                        order.setOrderStatus(4);
                    } else if (sum <= 12) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equals(order.getBetContent(), "单")) {
                    //单：三个位置的数值相加和尾数为单时就为单。注：买100元单开13退回本金。
                    if (sum == 13) {
                        order.setOrderStatus(4);
                    } else if (sum == 1 || sum == 3 || sum == 5 || sum == 7 || sum == 9
                            || sum == 11 || sum == 15 || sum == 17 || sum == 19 || sum == 21
                            || sum == 23 || sum == 25 || sum == 27) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equals(order.getBetContent(), "双")) {
                    // 双：三个位置的数值相加和尾数为双时就为双。注：买100元双开14退回本金。
                    if (sum == 14) {
                        order.setOrderStatus(4);
                    } else if (sum == 2 || sum == 4 || sum == 6 || sum == 8 || sum == 10
                            || sum == 12 || sum == 16 || sum == 18 || sum == 20 || sum == 22
                            || sum == 24 || sum == 26) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equals(order.getBetContent(), "大单")) {
                    // 大单（三个数值和）：15,17,19,21,23,25,27为大单。
                    if (sum == 15 || sum == 17 || sum == 19 || sum == 21 || sum == 23
                            || sum == 25 || sum == 27) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equals(order.getBetContent(), "小单")) {
                    // 小单（三个数值和）：01,03,05,07,09,11为小单。举例买100元小单开13退回本金。
                    if (sum == 13) {
                        order.setOrderStatus(4);
                    } else if (sum == 1 || sum == 3 || sum == 5 || sum == 7 || sum == 9
                            || sum == 11) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equals(order.getBetContent(), "大双")) {
                    // 大双（三个数值和）：16,18,20,22,24,26为大双。举例买100元大双开14退回本金。
                    if (sum == 14) {
                        order.setOrderStatus(4);
                    } else if (sum == 16 || sum == 18 || sum == 20 || sum == 22 || sum == 24
                            || sum == 26) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equals(order.getBetContent(), "小双")) {
                    // 小双（三个数值和）：00,02,04,06,08,10,12为小双。
                    if (sum == 0 || sum == 2 || sum == 4 || sum == 6 || sum == 8
                            || sum == 10 || sum == 12) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equals(order.getBetContent(), "极大")) {
                    // 极大（三个数值和）：23,24,25,26,27为极大。
                    if (sum == 23 || sum == 24 || sum == 25 || sum == 26 || sum == 27) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equals(order.getBetContent(), "极小")) {
                    // 极小（三个数值和）：00,01,02,03,04为极小。
                    if (sum == 0 || sum == 1 || sum == 2 || sum == 3 || sum == 4) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                }
            } else if (StringUtils.equals(order.getPlayCode(), "bose_baozi_bose_baozi")) { // 豹子波色
                // 购买波色如果三个数值和为0,13,14,27视为不中奖
                if (StringUtils.equals(order.getBetContent(), "红波")) {
                    // 红波（三个数值和）：03,06，09,12,15,18,21,24为红波。
                    if (sum == 3 || sum == 6 || sum == 9 || sum == 12 || sum == 15 || sum == 18 || sum == 21 || sum == 24) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equals(order.getBetContent(), "绿波")) {
                    // 绿波（三个数值和）：01,04,07,10,16,19,22，25为绿波。
                    if (sum == 1 || sum == 4 || sum == 7 || sum == 10 || sum == 16 || sum == 19 || sum == 22 || sum == 25) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equals(order.getBetContent(), "蓝波")) {
                    // 蓝波（三个数值和）：02,05,08,11,17,20,23,26为蓝波。
                    if (sum == 2 || sum == 5 || sum == 8 || sum == 11 || sum == 17 || sum == 20 || sum == 23 || sum == 26) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                } else if (StringUtils.equals(order.getBetContent(), "豹子")) {
                    // 豹子：当期开出三个数字相同即为豹子
                    //        （ 例 0+0+0=0,1+1+1=3,2+2+2=6,3+3+3=9,4+4+4=12,5+5+5=15,
                    //        6+6+6=18,7+7+7=21,8+8+8=24，9+9+9=27）。
                    if (StringUtils.equals(resultArr[0], resultArr[1]) && StringUtils.equals(resultArr[1], resultArr[2])) {
                        order.setOrderStatus(1);
                    } else {
                        order.setOrderStatus(2);
                    }
                }
            } else if (StringUtils.equals(order.getPlayCode(), "tm_tm")) {
                // 特码（三个数值和）：单选取一个数字投注。
                if (sum == NumberUtils.toInt(betContent)) {
                    order.setOrderStatus(1);
                } else {
                    order.setOrderStatus(2);
                }
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
