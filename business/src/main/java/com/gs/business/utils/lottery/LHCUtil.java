package com.gs.business.utils.lottery;

import cn.hutool.core.collection.CollUtil;
import com.gs.commons.entity.LotteryOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

/**
 *
 */
public class LHCUtil {

    private static final List<String> HONG_BO = CollUtil.newArrayList("1", "2", "7", "8", "12", "13", "18", "19", "23", "24", "29", "30", "34", "35", "40", "45", "46");
    private static final List<String> LAN_BO = CollUtil.newArrayList("3", "4", "9", "10", "14", "15", "20", "25", "26", "31", "36", "37", "41", "42", "47", "48");
    private static final List<String> LV_BO = CollUtil.newArrayList("5", "6", "11", "16", "17", "21", "22", "27", "28", "32", "33", "38", "39", "43", "44", "49");

    private static final String SHU = "5,17,29,41";
    private static final String NIU = "4,16,28,40";
    private static final String HU = "3,15,27,39";
    private static final String TU = "2,14,26,38";
    private static final String LONG = "1,13,25,37,49";
    private static final String SHE = "12,24,36,48";
    private static final String MA = "11,23,35,47";
    private static final String YANG = "10,22,34,46";
    private static final String HOU = "9,21,33,45";
    private static final String JI = "8,20,32,44";
    private static final String GOU = "7,19,31,43";
    private static final String ZHU = "6,18,30,42";

    public static void checkWin(LotteryOrder order) {
        String openResult = order.getOpenResult();
        String[] resultArr = openResult.split(",");
        int sum = 0;
        for (String s : resultArr) {
            sum += NumberUtils.toInt(s);
        }
        if (StringUtils.equals(order.getPlayCode(), "tma")) {
            checkTm(order, resultArr[6]);
        } else if (StringUtils.equals(order.getPlayCode(), "zm_zm")) {
            checkZmZm(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "ztm_1")) {
            checkZtm(order, resultArr[0]);
        } else if (StringUtils.equals(order.getPlayCode(), "ztm_2")) {
            checkZtm(order, resultArr[1]);
        } else if (StringUtils.equals(order.getPlayCode(), "ztm_3")) {
            checkZtm(order, resultArr[2]);
        } else if (StringUtils.equals(order.getPlayCode(), "ztm_4")) {
            checkZtm(order, resultArr[3]);
        } else if (StringUtils.equals(order.getPlayCode(), "ztm_5")) {
            checkZtm(order, resultArr[4]);
        } else if (StringUtils.equals(order.getPlayCode(), "ztm_6")) {
            checkZtm(order, resultArr[5]);
        } else if (StringUtils.equals(order.getPlayCode(), "zm1_6_1")) {
            checkZm1d6(order, resultArr[0]);
        } else if (StringUtils.equals(order.getPlayCode(), "zm1_6_2")) {
            checkZm1d6(order, resultArr[1]);
        } else if (StringUtils.equals(order.getPlayCode(), "zm1_6_3")) {
            checkZm1d6(order, resultArr[2]);
        } else if (StringUtils.equals(order.getPlayCode(), "zm1_6_4")) {
            checkZm1d6(order, resultArr[3]);
        } else if (StringUtils.equals(order.getPlayCode(), "zm1_6_5")) {
            checkZm1d6(order, resultArr[4]);
        } else if (StringUtils.equals(order.getPlayCode(), "zm1_6_6")) {
            checkZm1d6(order, resultArr[5]);
        } else if (StringUtils.equals(order.getPlayCode(), "bb_bb")) {
            checkBb(order, resultArr[6]);
        } else if (StringUtils.equals(order.getPlayCode(), "1xiaowei_1xiao")) {
            checkYX(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "1xiaowei_wei")) {
            checkWS(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "tmsx_tmsx")) {
            checkTMSX(order, resultArr[6]);
        } else if (StringUtils.equals(order.getPlayCode(), "qbz_qbz")) {
            checkQbz(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "hx_hx")) {
            checkHx(order, resultArr[6]);
        } else if (StringUtils.equals(order.getPlayCode(), "lianma_all2")) {
            check2qz(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "lianma_all3")) {
            check2qz(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "lianma_all4")) {
            check2qz(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "lianma_tc")) {
            checkTc(order, resultArr);
        }
    }

    private static void checkTc(LotteryOrder order, String[] resultArr) {
        List<String> betContent = Arrays.asList(order.getBetContent().split(","));
        List<String> zm = Arrays.asList(new String[]{resultArr[0], resultArr[1], resultArr[2], resultArr[3], resultArr[4], resultArr[5]});
        List<String> tm = Arrays.asList(new String[]{resultArr[6]});

        if (hasNum(tm, betContent) && hasNum(zm, betContent)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }

    private static boolean hasNum(List<String> betContent, List<String> result) {
        if (CollUtil.containsAny(betContent, result)) {
            return true;
        }
        return false;
    }

    private static void check2qz(LotteryOrder order, String[] resultArr) {
        String betContent = order.getBetContent();
        String[] result = new String[]{resultArr[0], resultArr[1], resultArr[2], resultArr[3], resultArr[4], resultArr[5]};
        List<String> betList = Arrays.asList(betContent.split(","));
        if (CollUtil.containsAll(Arrays.asList(result), betList)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }

    private static void checkHx(LotteryOrder order, String numStr) {
        String betContent = order.getBetContent();
        int num = NumberUtils.toInt(numStr);
        if (num == 49) {
            order.setOrderStatus(4);
            return;
        }

        String[] betContentArr = betContent.split(",");
        String hmStr = "";
        for (String s : betContentArr) {
            if (StringUtils.equals(s, "鼠")) {
                hmStr += SHU;
                hmStr += ",";
            } else if (StringUtils.equals(s, "牛")) {
                hmStr += NIU;
                hmStr += ",";
            } else if (StringUtils.equals(s, "虎")) {
                hmStr += HU;
                hmStr += ",";
            } else if (StringUtils.equals(s, "兔")) {
                hmStr += TU;
                hmStr += ",";
            } else if (StringUtils.equals(s, "龙")) {
                hmStr += LONG;
                hmStr += ",";
            } else if (StringUtils.equals(s, "蛇")) {
                hmStr += SHE;
                hmStr += ",";
            } else if (StringUtils.equals(s, "马")) {
                hmStr += MA;
                hmStr += ",";
            } else if (StringUtils.equals(s, "羊")) {
                hmStr += YANG;
                hmStr += ",";
            } else if (StringUtils.equals(s, "猴")) {
                hmStr += HOU;
                hmStr += ",";
            } else if (StringUtils.equals(s, "鸡")) {
                hmStr += JI;
                hmStr += ",";
            } else if (StringUtils.equals(s, "狗")) {
                hmStr += GOU;
                hmStr += ",";
            } else if (StringUtils.equals(s, "猪")) {
                hmStr += ZHU;
                hmStr += ",";
            }
        }

        List<String> betContentList = Arrays.asList(hmStr.split(","));
        if (betContentList.contains(numStr)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }

    }

    private static void checkQbz(LotteryOrder order, String[] resultArr) {
        String betContent = order.getBetContent();
        List<String> betContentList = Arrays.asList(betContent.split(","));
        List<String> resultList = Arrays.asList(resultArr);
        if (CollUtil.containsAny(betContentList, resultList)) {
            order.setOrderStatus(2);
        } else {
            order.setOrderStatus(1);
        }
    }

    private static void checkTMSX(LotteryOrder order, String numStr) {
        String betContent = order.getBetContent();

        String betContentNum = "";
        if (StringUtils.equals(betContent, "鼠")) {
            betContentNum = SHU;
        } else if (StringUtils.equals(betContent, "牛")) {
            betContentNum = NIU;
        } else if (StringUtils.equals(betContent, "虎")) {
            betContentNum = HU;
        } else if (StringUtils.equals(betContent, "兔")) {
            betContentNum = TU;
        } else if (StringUtils.equals(betContent, "龙")) {
            betContentNum = LONG;
        } else if (StringUtils.equals(betContent, "蛇")) {
            betContentNum = SHE;
        } else if (StringUtils.equals(betContent, "马")) {
            betContentNum = MA;
        } else if (StringUtils.equals(betContent, "羊")) {
            betContentNum = YANG;
        } else if (StringUtils.equals(betContent, "猴")) {
            betContentNum = HOU;
        } else if (StringUtils.equals(betContent, "鸡")) {
            betContentNum = JI;
        } else if (StringUtils.equals(betContent, "狗")) {
            betContentNum = GOU;
        } else if (StringUtils.equals(betContent, "猪")) {
            betContentNum = ZHU;
        }

        List<String> strings = Arrays.asList(betContentNum.split(","));
        if (strings.contains(numStr)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }

    private static void checkWS(LotteryOrder order, String[] resultArr) {
        String betContent = order.getBetContent();
        if (StringUtils.equals(betContent, "0尾")) {
            if (hasWs(resultArr, 0)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "1尾")) {
            if (hasWs(resultArr, 1)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "2尾")) {
            if (hasWs(resultArr, 2)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "3尾")) {
            if (hasWs(resultArr, 3)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "4尾")) {
            if (hasWs(resultArr, 4)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "5尾")) {
            if (hasWs(resultArr, 5)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "6尾")) {
            if (hasWs(resultArr, 6)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "7尾")) {
            if (hasWs(resultArr, 7)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "8尾")) {
            if (hasWs(resultArr, 8)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "9尾")) {
            if (hasWs(resultArr, 9)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }

    private static boolean hasWs(String[] resultArr, int weishu) {
        for (String result : resultArr) {
            int ws = getWeiShu(result);
            if (ws == weishu) {
                return true;
            }
        }
        return false;
    }

    private static void checkYX(LotteryOrder order, String[] resultArr) {
        String betContent = order.getBetContent();

        String betContentNum = "";
        if (StringUtils.equals(betContent, "鼠")) {
            betContentNum = SHU;
        } else if (StringUtils.equals(betContent, "牛")) {
            betContentNum = NIU;
        } else if (StringUtils.equals(betContent, "虎")) {
            betContentNum = HU;
        } else if (StringUtils.equals(betContent, "兔")) {
            betContentNum = TU;
        } else if (StringUtils.equals(betContent, "龙")) {
            betContentNum = LONG;
        } else if (StringUtils.equals(betContent, "蛇")) {
            betContentNum = SHE;
        } else if (StringUtils.equals(betContent, "马")) {
            betContentNum = MA;
        } else if (StringUtils.equals(betContent, "羊")) {
            betContentNum = YANG;
        } else if (StringUtils.equals(betContent, "猴")) {
            betContentNum = HOU;
        } else if (StringUtils.equals(betContent, "鸡")) {
            betContentNum = JI;
        } else if (StringUtils.equals(betContent, "狗")) {
            betContentNum = GOU;
        } else if (StringUtils.equals(betContent, "猪")) {
            betContentNum = ZHU;
        }
        List<String> strings = Arrays.asList(betContentNum.split(","));
        if (CollUtil.containsAny(strings, Arrays.asList(resultArr))) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }

    private static void checkBb(LotteryOrder order, String s) {
        String betContent = order.getBetContent();
        if (StringUtils.equals(betContent, "红单")) {
            if (StringUtils.equalsAny(betContent, "1", "7", "13", "19", "23", "29", "35", "45")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "红双")) {
            if (StringUtils.equalsAny(betContent, "2", "8", "12", "18", "24", "30", "34", "40", "46")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "红大")) {
            if (StringUtils.equalsAny(betContent, "29", "30", "34", "35", "40", "45", "46")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "红小")) {
            if (StringUtils.equalsAny(betContent, "1", "2", "7", "8", "12", "13", "18", "19", "23", "24")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "蓝单")) {
            if (StringUtils.equalsAny(betContent, "3", "9", "15", "25", "31", "37", "41", "47")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "蓝双")) {
            if (StringUtils.equalsAny(betContent, "2", "10", "14", "20", "26", "36", "42", "48")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "蓝大")) {
            if (StringUtils.equalsAny(betContent, "25", "26", "31", "36", "37", "41", "42", "47", "48")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "蓝小")) {
            if (StringUtils.equalsAny(betContent, "3", "4", "9", "10", "14", "15", "20")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "绿单")) {
            if (StringUtils.equalsAny(betContent, "5", "11", "17", "21", "27", "33", "39", "43")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "绿双")) {
            if (StringUtils.equalsAny(betContent, "6", "16", "22", "28", "32", "38", "44")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "绿大")) {
            if (StringUtils.equalsAny(betContent, "27", "28", "32", "33", "38", "39", "43", "44")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "绿小")) {
            if (StringUtils.equalsAny(betContent, "5", "6", "11", "16", "17", "21", "22")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }

    private static void checkZm1d6(LotteryOrder order, String num) {
        String betContent = order.getBetContent();
        int numInt = NumberUtils.toInt(num);
        if (StringUtils.equals(betContent, "大")) {
            if (numInt >= 25) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "小")) {
            if (numInt <= 24) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "单")) {
            if (numInt % 2 == 1) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "双")) {
            if (numInt % 2 == 0) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "合大")) {
            int tmHeShu = getTmHeShu(num);
            if (tmHeShu >= 7) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "合小")) {
            int tmHeShu = getTmHeShu(num);
            if (tmHeShu <= 6) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "合单")) {
            int tmHeShu = getTmHeShu(num);
            if (tmHeShu % 2 == 1) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "合双")) {
            int tmHeShu = getTmHeShu(num);
            if (tmHeShu % 2 == 0) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "尾大")) {
            int weiShu = getWeiShu(num);
            if (weiShu >= 5 && weiShu <= 9) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "尾小")) {
            int weiShu = getWeiShu(num);
            if (weiShu >= 0 && weiShu <= 4) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "红波")) {
            if (HONG_BO.contains(num)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "绿波")) {
            if (LV_BO.contains(num)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "蓝波")) {
            if (LAN_BO.contains(num)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }

    private static void checkZtm(LotteryOrder order, String s) {
        String betContent = order.getBetContent();
        if (StringUtils.equals(betContent, s)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }

    private static void checkZmZm(LotteryOrder order, String[] resultArr) {
        String betContent = order.getBetContent();
        List<String> result = Arrays.asList(resultArr);
        if (result.contains(betContent)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }

    private static void checkTm(LotteryOrder order, String num) {
        String betContent = order.getBetContent();
        int numInt = NumberUtils.toInt(num);
        if (StringUtils.equals(betContent, "大")) {
            if (numInt == 49) {
                order.setOrderStatus(4);
            } else if (numInt >= 25 && numInt <= 48) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "小")) {
            if (numInt == 49) {
                order.setOrderStatus(4);
            } else if (numInt >= 1 && numInt <= 24) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "单")) {
            if (numInt == 49) {
                order.setOrderStatus(4);
            } else if (numInt % 2 == 1) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "双")) {
            if (numInt == 49) {
                order.setOrderStatus(4);
            } else if (numInt % 2 == 0) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "合大")) {
            int tmHeShu = getTmHeShu(num);
            if (numInt == 49) {
                order.setOrderStatus(4);
            } else if (tmHeShu >= 7) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "合小")) {
            int tmHeShu = getTmHeShu(num);
            if (numInt == 49) {
                order.setOrderStatus(4);
            } else if (tmHeShu <= 6) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "合单")) {
            int tmHeShu = getTmHeShu(num);
            if (numInt == 49) {
                order.setOrderStatus(4);
            } else if (tmHeShu % 2 == 1) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "合双")) {
            int tmHeShu = getTmHeShu(num);
            if (numInt == 49) {
                order.setOrderStatus(4);
            } else if (tmHeShu % 2 == 0) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "大单")) {
            if (numInt == 49) {
                order.setOrderStatus(4);
            } else if (StringUtils.equalsAny(betContent, "25", "27", "29", "31", "33", "35", "37", "39", "41", "43", "45", "47")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "小单")) {
            if (numInt == 49) {
                order.setOrderStatus(4);
            } else if (StringUtils.equalsAny(betContent, "1", "3", "5", "7", "9", "11", "13", "15", "17", "19", "21", "23")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "大双")) {
            if (numInt == 49) {
                order.setOrderStatus(4);
            } else if (StringUtils.equalsAny(betContent, "26", "28", "30", "32", "34", "36", "38", "40", "42", "44", "46", "48")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "小双")) {
            if (numInt == 49) {
                order.setOrderStatus(4);
            } else if (StringUtils.equalsAny(betContent, "2", "4", "6", "8", "10", "12", "14", "16", "18", "20", "22", "24")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "尾大")) {
            int weiShu = getWeiShu(num);
            if (numInt == 49) {
                order.setOrderStatus(4);
            } else if (weiShu >= 5 && weiShu <= 9) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "尾小")) {
            int weiShu = getWeiShu(num);
            if (numInt == 49) {
                order.setOrderStatus(4);
            } else if (weiShu >= 0 && weiShu <= 4) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "红波")) {
            if (HONG_BO.contains(num)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "蓝波")) {
            if (LAN_BO.contains(num)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "绿波")) {
            if (LV_BO.contains(num)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else {
            // 这里全是号码
            if (StringUtils.equals(betContent, num)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }

    private static int getWeiShu(String num) {
        int weishu = NumberUtils.toInt(num);
        if (weishu >= 1 && weishu <= 9) {
            return weishu;
        }
        char[] chars = num.toCharArray();
        return NumberUtils.toInt(String.valueOf(chars[1]));
    }

    private static int getTmHeShu(String num) {
        int heshu = NumberUtils.toInt(num);
        if (heshu >= 1 && heshu <= 9) {
            return heshu;
        }
        char[] chars = num.toCharArray();
        return NumberUtils.toInt(String.valueOf(chars[0])) + NumberUtils.toInt(String.valueOf(chars[1]));
    }
}
