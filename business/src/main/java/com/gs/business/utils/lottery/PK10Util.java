package com.gs.business.utils.lottery;

import com.gs.commons.entity.LotteryOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class PK10Util {

    public static void checkWin(LotteryOrder order){
        String betContent = order.getBetContent();
        String openResult = order.getOpenResult();
        String[] resultArr = openResult.split(",");
        if (StringUtils.equals(order.getPlayCode(), "smp_champ")) {
            // 冠军 龙/虎∶"第一名"之车号大于"第十名"之车号视为「龙」中奖、反之小于视为「虎」中奖，其馀情况视为不中奖。
            checkSmpDxds(order, resultArr[0]);
            checkSmpLh(order, resultArr[0], resultArr[9]);
        } else if (StringUtils.equals(order.getPlayCode(), "smp_2nd")) {
            // 亚军 龙/虎∶"第二名"之车号大于"第九名"之车号视为「龙」中奖、反之小于视为「虎」中奖，其馀情况视为不中奖。
            checkSmpDxds(order, resultArr[1]);
            checkSmpLh(order, resultArr[1], resultArr[8]);
        } else if (StringUtils.equals(order.getPlayCode(), "smp_3rd")) {
            // 季军 龙/虎∶"第三名"之车号大于"第八名"之车号视为「龙」中奖、反之小于视为「虎」中奖，其馀情况视为不中奖。
            checkSmpDxds(order, resultArr[2]);
            checkSmpLh(order, resultArr[2], resultArr[7]);
        } else if (StringUtils.equals(order.getPlayCode(), "smp_4th")) {
            // 第四名 龙/虎∶"第四名"之车号大于"第七名"之车号视为「龙」中奖、反之小于视为「虎」中奖，其馀情况视为不中奖。
            checkSmpDxds(order, resultArr[3]);
            checkSmpLh(order, resultArr[3], resultArr[6]);
        } else if (StringUtils.equals(order.getPlayCode(), "smp_5th")) {
            // 第五名 龙/虎∶"第五名"之车号大于"第六名"之车号视为「龙」中奖、反之小于视为「虎」中奖，其馀情况视为不中奖。
            checkSmpDxds(order, resultArr[4]);
            checkSmpLh(order, resultArr[4], resultArr[5]);
        } else if (StringUtils.equals(order.getPlayCode(), "smp_6th")) {
            checkSmpDxds(order, resultArr[5]);
        } else if (StringUtils.equals(order.getPlayCode(), "smp_7th")) {
            checkSmpDxds(order, resultArr[6]);
        } else if (StringUtils.equals(order.getPlayCode(), "smp_8th")) {
            checkSmpDxds(order, resultArr[7]);
        } else if (StringUtils.equals(order.getPlayCode(), "smp_9th")) {
            checkSmpDxds(order, resultArr[8]);
        } else if (StringUtils.equals(order.getPlayCode(), "smp_10th")) {
            checkSmpDxds(order, resultArr[9]);
        } else if (StringUtils.equals(order.getPlayCode(), "onenum1_10_champ")) {
            // 冠军~第十名车号指定: 每一个车号为一投注组合，开奖结果"投注车号"对应所投名次视为中奖，其馀情形视为不中奖。
            checkDwd(order, resultArr[0]);
        } else if (StringUtils.equals(order.getPlayCode(), "onenum1_10_2nd")) {
            checkDwd(order, resultArr[1]);
        } else if (StringUtils.equals(order.getPlayCode(), "onenum1_10_3rd")) {
            checkDwd(order, resultArr[2]);
        } else if (StringUtils.equals(order.getPlayCode(), "onenum1_10_4th")) {
            checkDwd(order, resultArr[3]);
        } else if (StringUtils.equals(order.getPlayCode(), "onenum1_10_5th")) {
            checkDwd(order, resultArr[4]);
        } else if (StringUtils.equals(order.getPlayCode(), "onenum1_10_6th")) {
            checkDwd(order, resultArr[5]);
        } else if (StringUtils.equals(order.getPlayCode(), "onenum1_10_7th")) {
            checkDwd(order, resultArr[6]);
        } else if (StringUtils.equals(order.getPlayCode(), "onenum1_10_8th")) {
            checkDwd(order, resultArr[7]);
        } else if (StringUtils.equals(order.getPlayCode(), "onenum1_10_9th")) {
            checkDwd(order, resultArr[8]);
        } else if (StringUtils.equals(order.getPlayCode(), "onenum1_10_10th")) {
            checkDwd(order, resultArr[9]);
        } else if (StringUtils.equals(order.getPlayCode(), "champ_2nd_gyh")) {
            int number1 = NumberUtils.toInt(resultArr[0]);
            int number2 = NumberUtils.toInt(resultArr[1]);
            int sum = number1 + number2;
            // 冠亚和指定:以指定冠军车号和亚军车号之和来判断胜负；冠亚车号总和可能出现结果为3~19，投中对应"冠亚和指定"数字视为中奖，其馀情形视为不中奖。
            if (StringUtils.equalsAny(betContent, "3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19")) {
                int betContentNum = NumberUtils.toInt(betContent);
                if (betContentNum == sum) {
                    order.setOrderStatus(1);
                } else {
                    order.setOrderStatus(2);
                }
            } else if (StringUtils.equals(betContent, "和大")) {
                // 冠亚和大小∶以冠军车号和亚军车号之和大小来判断胜负，"冠亚和"大于11为大，小于等于11为小。假如投注组合符合中奖结果，视为中奖，其馀情形视为不中奖。
                if (sum > 11) {
                    order.setOrderStatus(1);
                } else {
                    order.setOrderStatus(2);
                }
            } else if (StringUtils.equals(betContent, "和小")) {
                if (sum <= 11) {
                    order.setOrderStatus(1);
                } else {
                    order.setOrderStatus(2);
                }
            } else if (StringUtils.equals(betContent, "和单")) {
                // 冠亚和单双∶以冠军车号和亚军车号之和单双来判断胜负，"冠亚和"为双数叫双，为单数叫单。假如投注组合符合中奖结果，视为中奖，其馀情形视为不中奖。
                if (sum % 2 == 1) {
                    order.setOrderStatus(1);
                } else {
                    order.setOrderStatus(2);
                }
            } else if (StringUtils.equals(betContent, "和双")) {
                if (sum % 2 == 0) {
                    order.setOrderStatus(1);
                } else {
                    order.setOrderStatus(2);
                }
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

    /**
     * 检查双盘面大小单双
     * @param order
     */
    private static void checkSmpDxds(LotteryOrder order, String number1Str) {
        // 大小∶开出之号码大于或等于6为大，小于或等于5为小。
        // 单双∶开出之号码为双数叫双，如4、8；号码为单数叫单，如5、9。
        int number1 = NumberUtils.toInt(number1Str);
        String betContent = order.getBetContent();
        if (StringUtils.equals(betContent, "大")) {
            if (number1 >= 6) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "小")) {
            if (number1 <= 5) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "单")) {
            if (number1 == 1 || number1 == 3 || number1 == 5 || number1 == 7 || number1 == 9) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "双")) {
            if (number1 == 2 || number1 == 4 || number1 == 6 || number1 == 8 || number1 == 10) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }

    /**
     * 检查双盘面大小单双
     * @param order
     */
    private static void checkSmpLh(LotteryOrder order, String number1Str, String number2Str) {
        int number1 = NumberUtils.toInt(number1Str);
        int number2 = NumberUtils.toInt(number2Str);
        String betContent = order.getBetContent();
       if (StringUtils.equals(betContent, "龙")) {
            if (number1 > number2) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "虎")) {
            if (number1 < number2) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }
}
