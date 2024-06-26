package com.gs.business.utils.lottery;

import cn.hutool.core.util.NumberUtil;
import com.gs.commons.entity.LotteryOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class PCDDUtil {

    public static void checkWin(LotteryOrder order){
        String betContent = order.getBetContent();

        String openResult = order.getOpenResult();
        String[] resultArr = openResult.split(",");
        int sum = NumberUtil.parseInt(resultArr[0]) + NumberUtil.parseInt(resultArr[1]) + NumberUtil.parseInt(resultArr[2]);
        if (StringUtils.equals(order.getPlayCode(), "hunhe_hunhe")) { // 混合
            if (StringUtils.equals(order.getBetContent(), "大")) {
                // 大：三个位置的数值相加和大于15,16,17,18,19,20,21,22,23,24,25,26,27为大。注：买100元大开14退回本金。
               if (sum >= 14) {
                    order.setOrderStatus(1);
                } else {
                    order.setOrderStatus(2);
                }
            } else if (StringUtils.equals(order.getBetContent(), "小")) {
                //小：三个位置的数值相加和小于00,01,02,03,04,05,06,07,08,09,10,11,12为小。注：买100元小开13退回本金。
                if (sum <= 13) {
                    order.setOrderStatus(1);
                } else {
                    order.setOrderStatus(2);
                }
            } else if (StringUtils.equals(order.getBetContent(), "单")) {
                //单：三个位置的数值相加和尾数为单时就为单。注：买100元单开13退回本金。
               if (sum == 1 || sum == 3 || sum == 5 || sum == 7 || sum == 9
                        || sum == 11 || sum == 13 || sum == 15 || sum == 17 || sum == 19 || sum == 21
                        || sum == 23 || sum == 25 || sum == 27) {
                    order.setOrderStatus(1);
                } else {
                    order.setOrderStatus(2);
                }
            } else if (StringUtils.equals(order.getBetContent(), "双")) {
                // 双：三个位置的数值相加和尾数为双时就为双。注：买100元双开14退回本金。
                 if (sum == 0 || sum == 2 || sum == 4 || sum == 6 || sum == 8 || sum == 10
                        || sum == 12 || sum == 14 || sum == 16 || sum == 18 || sum == 20 || sum == 22
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
                if (sum == 1 || sum == 3 || sum == 5 || sum == 7 || sum == 9
                        || sum == 11 || sum == 13) {
                    order.setOrderStatus(1);
                } else {
                    order.setOrderStatus(2);
                }
            } else if (StringUtils.equals(order.getBetContent(), "大双")) {
                // 大双（三个数值和）：16,18,20,22,24,26为大双。举例买100元大双开14退回本金。
               if (sum == 14 || sum == 16 || sum == 18 || sum == 20 || sum == 22 || sum == 24
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
