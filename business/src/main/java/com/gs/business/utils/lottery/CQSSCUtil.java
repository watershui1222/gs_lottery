package com.gs.business.utils.lottery;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.math.MathUtil;
import com.gs.commons.entity.LotteryOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Date;
import java.util.List;

public class CQSSCUtil {

    public static void checkWin(LotteryOrder order){
        String betContent = order.getBetContent();
        String openResult = order.getOpenResult();
        String[] resultArr = openResult.split(",");
        int sum = 0;
        for (String s : resultArr) {
            sum += NumberUtils.toInt(s);
        }
        if (StringUtils.equals(order.getPlayCode(), "dw15_no1")) {
            checkDwd(order, resultArr[0]);
        } else if (StringUtils.equals(order.getPlayCode(), "dw15_no2")) {
            checkDwd(order, resultArr[1]);
        } else if (StringUtils.equals(order.getPlayCode(), "dw15_no3")) {
            checkDwd(order, resultArr[2]);
        } else if (StringUtils.equals(order.getPlayCode(), "dw15_no4")) {
            checkDwd(order, resultArr[3]);
        } else if (StringUtils.equals(order.getPlayCode(), "dw15_no5")) {
            checkDwd(order, resultArr[4]);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_he")) {
            checkLmZh(order, sum);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_no1")) {
            checkLmDxds(order, resultArr[0]);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_no2")) {
            checkLmDxds(order, resultArr[1]);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_no3")) {
            checkLmDxds(order, resultArr[2]);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_no4")) {
            checkLmDxds(order, resultArr[3]);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_no5")) {
            checkLmDxds(order, resultArr[4]);
        } else if (StringUtils.equals(order.getPlayCode(), "qzh_q3")) {
            if (StringUtils.equals("豹子", betContent)) {

            } else if (StringUtils.equals("顺子", betContent)) {
                
            } else if (StringUtils.equals("对子", betContent)) {

            } else if (StringUtils.equals("半顺", betContent)) {

            } else if (StringUtils.equals("杂六", betContent)) {

            }
        }
    }

    /**
     * 前三球-顺子
     * @param order
     */
    private static void checkQsqSz(LotteryOrder order, String d1q, String d2q, String d3q) {

    }

    public static void main(String[] args) {
        Date now = new Date();

        Date end = DateUtil.offsetSecond(now, 9);

        System.out.println(now.before(end));

        System.out.println(DateUtil.between(end, now, DateUnit.SECOND, false));

    }

    /**
     * 两面-大小单双
     * @param order
     * @param numberStr
     */
    private static void checkLmDxds(LotteryOrder order, String numberStr) {
        String betContent = order.getBetContent();
        int number = NumberUtils.toInt(numberStr);
        if (StringUtils.equals(betContent, "大")) {
            // 根据相应单项投注的第一球特 ~ 第五球特开出的球号大於或等於5为特码大，小於或等於4为特码小。
            if (number >= 5) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "小")) {
            if (number <= 4) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "单")) {
            // 根据相应单项投注的第一球特 ~ 第五球特开出的球号为双数叫特双，如2、6；特码为单数叫特单，如1、3。
            if (number % 2 == 1) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "双")) {
            if (number % 2 == 0) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }

    /**
     * 两面-总和
     * @param order
     * @param sum
     */
    private static void checkLmZh(LotteryOrder order, int sum) {
        String betContent = order.getBetContent();
        if (StringUtils.equals(betContent, "总和大")) {
            // 大小：根据相应单项投注的第一球特 ~ 第五球特开出的球号大於或等於23为特码大，小於或等於22为特码小。
            if (sum >= 23) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "总和小")) {
            if (sum <= 22) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "总和单")) {
            // 单双：根据相应单项投注的第一球特 ~ 第五球特开出的球号数字总和值是双数为总和双，数字总和值是单数为总和单。
            if (sum % 2 == 1) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "总和双")) {
            if (sum % 2 == 0) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }

    /**
     * 号码定位
     * @param order
     * @param resultNumStr
     */
    private static void checkDwd(LotteryOrder order, String resultNumStr) {
        if (StringUtils.equals(order.getBetContent(), resultNumStr)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }
}
