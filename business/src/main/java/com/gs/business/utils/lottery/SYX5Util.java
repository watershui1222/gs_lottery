package com.gs.business.utils.lottery;

import cn.hutool.core.collection.CollUtil;
import com.gs.commons.entity.LotteryOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 广东11选5规则说明
 * 该游戏的投注时间、开奖时间和开奖号码与广东11选5完全同步，北京时间（GMT+8）每天白天从上午9：30开到晚上23：10，每20 分钟开一次奖，每天开奖 42 期。
 * 具体游戏规则如下:
 * 1.单号：-指第一球、第二球、第三球、第四球、第五球出现的顺序与号码为派彩依据。如现场滚球第一个开奖号码为10号，投注第一球为10号则视为中奖，其它号码视为不中奖 。
 *    一中一：投注1个号码与当期开奖的5个号码中任1个号码相同，视为中奖。
 * 2.两面
 *    大小：开出的号码大于或等于6为大，小于或等于5为小，开出11为和 (不计算输赢)
 *    单双：号码为双数叫双，如2、8；号码为单数叫单，如5、9；开出11为和
 *    总和:-以全部开出的5个号码，加起来的总和来判定。
 *    总和大小: 所有开奖号码数字加总值大于30为和大；总和值小于30为和小；若总和值等于30为和 (不计算输赢)。
 *    总和单双：所有开奖号码数字加总值为单数叫和单，如11、31；加总值为双数叫和双，如42、30。
 *    总和尾数大小：所有开奖号码数字加总值的尾数，大于或等于5为尾大，小于或等于4为尾小。
 *    龙虎：
 *    龙：第一球开奖号码大于第五球开奖号码，如第一球开出10，第五球开出7。
 *    虎：第一球开奖号码小于第五球开奖号码，如第一球开出3，第五球开出7。
 * 3.连码
 *    任选二：投注2个号码与当期开奖的5个号码中任2个号码相同(顺序不限)，视为中奖。
 *    任选三: 投注3个号码与当期开奖的5个号码中任3个号码相同(顺序不限)，视为中奖。
 *    任选四：4个号码与当期开奖的5个号码中任4个号码相同(顺序不限)，视为中奖。
 *    任选五： 投注5个号码与当期开奖的5个号码中5个号码相同(顺序不限)，视为中奖。
 *    任选六：投注6个号码中任5个号码与当期开奖的5个号码相同(顺序不限)，视为中奖。
 *    任选七：投注7个号码中任5个号码与当期开奖的5个号码相同(顺序不限)，视为中奖。
 *    任选八：投注8个号码中任5个号码与当期开奖的5个号码相同(顺序不限)，视为中奖。
 *    组选前二：投注的2个号码与当期顺序开出的5个号码中的前2个号码相同，视为中奖。
 *    组选前三：投注的3个号码与当期顺序开出的5个号码中的前3个号码相同，视为中奖。
 * 4.直选
 *    直选前二：投注的2个号码与当期顺序开出的5个号码中的前2个号码相同且顺序一致，视为中奖。
 *    直选前三：投注的3个号码与当期顺序开出的5个号码中的前3个号码相同且顺序一致，视为中奖。
 */
public class SYX5Util {

    public static void checkWin(LotteryOrder order) {
        String openResult = order.getOpenResult();
        String[] resultArr = openResult.split(",");
        int sum = 0;
        for (String s : resultArr) {
            sum += NumberUtils.toInt(s);
        }
        if (StringUtils.equals(order.getPlayCode(), "lm_sum")) {
            checkZongHe(order, resultArr, sum);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_1q")) {
            checkDxds(order, resultArr[0]);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_2q")) {
            checkDxds(order, resultArr[1]);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_3q")) {
            checkDxds(order, resultArr[2]);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_4q")) {
            checkDxds(order, resultArr[3]);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_5q")) {
            checkDxds(order, resultArr[4]);
        } else if (StringUtils.equals(order.getPlayCode(), "dh_1z1")) {
            check1z1(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "dh_1q")) {
            checkDwHm(order, resultArr[0]);
        } else if (StringUtils.equals(order.getPlayCode(), "dh_2q")) {
            checkDwHm(order, resultArr[1]);
        } else if (StringUtils.equals(order.getPlayCode(), "dh_3q")) {
            checkDwHm(order, resultArr[2]);
        } else if (StringUtils.equals(order.getPlayCode(), "dh_4q")) {
            checkDwHm(order, resultArr[3]);
        } else if (StringUtils.equals(order.getPlayCode(), "dh_5q")) {
            checkDwHm(order, resultArr[4]);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_2z2")) {
            check2z2(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_3z3")) {
            check3z3(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_4z4")) {
            check4z4(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_5z5")) {
            check5z5(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_6z5")) {
            check6z5(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_7z5")) {
            check7z5(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_8z5")) {
            check8z5(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_q2zx")) {
            checkQ2ZUX(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "lm_q3zx")) {
            checkQ3ZUX(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "zx_q2")) {
            checkZxQe(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "zx_q3")) {
            checkZxQs(order, resultArr);
        }
    }

    private static void check8z5(LotteryOrder order, String[] resultArr) {
        check6z5(order, resultArr);
    }

    private static void check7z5(LotteryOrder order, String[] resultArr) {
        check6z5(order, resultArr);
    }

    private static void check6z5(LotteryOrder order, String[] resultArr) {
        String betContent  = order.getBetContent();
        List<String> bet = Arrays.asList(betContent.split(","));
        List<String> result = Arrays.asList(resultArr);
        if (CollUtil.containsAll(bet, result)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }

    private static void check5z5(LotteryOrder order, String[] resultArr) {
        check2z2(order, resultArr);
    }

    private static void check4z4(LotteryOrder order, String[] resultArr) {
        check2z2(order, resultArr);
    }

    private static void check3z3(LotteryOrder order, String[] resultArr) {
        check2z2(order, resultArr);
    }

    private static void check2z2(LotteryOrder order, String[] resultArr) {
        String betContent  = order.getBetContent();
        List<String> bet = Arrays.asList(betContent.split(","));
        List<String> result = Arrays.asList(resultArr);
        if (CollUtil.containsAll(result, bet)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }

    public static void main(String[] args) {
        List<String> result = new ArrayList<>();
        result.add("1");
        result.add("3");
        result.add("4");
        result.add("5");
        result.add("8");


        List<String> bet = new ArrayList<>();
        bet.add("1");
        bet.add("3");
        bet.add("4");
        bet.add("5");
        bet.add("8");
        bet.add("9");
        System.out.println(CollUtil.containsAll(bet, result));
    }

    private static void checkDwHm(LotteryOrder order, String s) {
        String betContent = order.getBetContent();
        if (StringUtils.equals(s, betContent)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(4);
        }
    }

    private static void check1z1(LotteryOrder order, String[] resultArr) {
        String betContent = order.getBetContent();
        List<String> result = Arrays.asList(resultArr);
        if (result.contains(betContent)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }

    private static void checkDxds(LotteryOrder order, String s) {
        int num = NumberUtils.toInt(s);
        if (num == 11) {
            order.setOrderStatus(4);
            return;
        }
        String betContent = order.getBetContent();
        if (StringUtils.equals(betContent, "大")) {
            if (num >= 6 && num <= 10) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "小")) {
            if (num >= 1 && num <= 5) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "单")) {
            if (num == 1 || num == 3 || num == 5 || num == 7 || num == 9) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "双")) {
            if (num == 2 || num == 4 || num == 6 || num == 8 || num == 10) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }

    private static void checkZongHe(LotteryOrder order, String[] resultArr, int sum) {
        String betContent = order.getBetContent();
        if (StringUtils.equals(betContent, "总和大")) {
            if (sum > 30) {
                order.setOrderStatus(1);
            } else if (sum < 30) {
                order.setOrderStatus(2);
            } else {
                order.setOrderStatus(4);
            }
        } else if (StringUtils.equals(betContent, "总和小")) {
            if (sum > 30) {
                order.setOrderStatus(2);
            } else if (sum < 30) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(4);
            }
        } else if (StringUtils.equals(betContent, "总和单")) {
            if (sum %2 == 1) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "总和双")) {
            if (sum %2 == 0) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }else if (StringUtils.equals(betContent, "总和尾大")) {
            if (sum %10 >= 5) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "总和尾小")) {
            if (sum %10 <= 4) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "龙")) {
            int num1 = NumberUtils.toInt(resultArr[0]);
            int num5 = NumberUtils.toInt(resultArr[4]);
            if (num1 > num5) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "虎")) {
            int num1 = NumberUtils.toInt(resultArr[0]);
            int num5 = NumberUtils.toInt(resultArr[4]);
            if (num1 < num5) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }

    private static void checkQ2ZUX(LotteryOrder order, String[] resultArr) {
        String betContent = order.getBetContent();
        String[] betContentArr = betContent.split(",");
        if (StringUtils.equals(betContentArr[0], resultArr[0])
                && StringUtils.equals(betContentArr[1], resultArr[1])) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }

    private static void checkQ3ZUX(LotteryOrder order, String[] resultArr) {
        String betContent = order.getBetContent();
        String[] betContentArr = betContent.split(",");
        if (StringUtils.equals(betContentArr[0], resultArr[0])
                && StringUtils.equals(betContentArr[1], resultArr[1])
                && StringUtils.equals(betContentArr[2], resultArr[2])) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }

    private static void checkZxQe(LotteryOrder order, String[] resultArr) {
        String betContent = order.getBetContent();
        String[] betContentArr = betContent.split(",");
        if (StringUtils.equals(betContentArr[0], resultArr[0])
                && StringUtils.equals(betContentArr[1], resultArr[1])) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }

    private static void checkZxQs(LotteryOrder order, String[] resultArr) {
        String betContent = order.getBetContent();
        String[] betContentArr = betContent.split(",");
        if (StringUtils.equals(betContentArr[0], resultArr[0])
                && StringUtils.equals(betContentArr[1], resultArr[1])
                && StringUtils.equals(betContentArr[2], resultArr[2])) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }
}
