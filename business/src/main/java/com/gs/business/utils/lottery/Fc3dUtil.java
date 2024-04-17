package com.gs.business.utils.lottery;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.math.MathUtil;
import cn.hutool.core.util.NumberUtil;
import com.gs.commons.entity.LotteryOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

/**
 *
 */
public class Fc3dUtil {


    //红波	三个数值和为03,06,09,12,15,18,21,24即为红波。
    //绿波	三个数值和为01,04,07,10,16,19,22,25即为绿波。
    //蓝波	三个数值和为02,05,08,11,17,20,23,26即为蓝波。
    private static final List<String> HONG_BO = CollUtil.newArrayList("3", "6", "9", "12", "15", "18", "21", "24");
    private static final List<String> LV_BO = CollUtil.newArrayList("1", "4", "7", "10", "16", "19", "22", "25");
    private static final List<String> LAN_BO = CollUtil.newArrayList("2", "5", "8", "11", "17", "20", "23", "26");

    public static void checkWin(LotteryOrder order) {

        String openResult = order.getOpenResult();
        String[] resultArr = openResult.split(",");
        int sum = 0;
        for (String s : resultArr) {
            sum += NumberUtils.toInt(s);
        }
        if (StringUtils.equals(order.getPlayCode(), "zsp_b")) {
            checkdwdxds(order, resultArr[0]);
        } else if (StringUtils.equals(order.getPlayCode(), "zsp_s")) {
            checkdwdxds(order, resultArr[1]);
        }  else if (StringUtils.equals(order.getPlayCode(), "zsp_g")) {
            checkdwdxds(order, resultArr[2]);
        } else if (StringUtils.equals(order.getPlayCode(), "zsp_sb")) {
            twohedanshuang(order, resultArr[0], resultArr[1]);
        } else if (StringUtils.equals(order.getPlayCode(), "zsp_bg")) {
            twohedanshuang(order, resultArr[0], resultArr[2]);
        } else if (StringUtils.equals(order.getPlayCode(), "zsp_sg")) {
            twohedanshuang(order, resultArr[1], resultArr[2]);
        } else if (StringUtils.equals(order.getPlayCode(), "zsp_bshw")) {
            checkSumWdx(order, NumberUtil.add(resultArr[0], resultArr[1]).intValue());
        } else if (StringUtils.equals(order.getPlayCode(), "zsp_bghw")) {
            checkSumWdx(order, NumberUtil.add(resultArr[0], resultArr[2]).intValue());
        } else if (StringUtils.equals(order.getPlayCode(), "zsp_sghw")) {
            checkSumWdx(order, NumberUtil.add(resultArr[1], resultArr[2]).intValue());
        } else if (StringUtils.equals(order.getPlayCode(), "zsp_bsgh")) {
            checkSumdxds(order, NumberUtil.add(resultArr[1], resultArr[2], resultArr[0]).intValue());
        } else if (StringUtils.equals(order.getPlayCode(), "zsp_bsghw")) {
            checkSumWdx(order, NumberUtil.add(resultArr[1], resultArr[2], resultArr[0]).intValue());
        } else if (StringUtils.equals(order.getPlayCode(), "zsp_bs")) {
            checkBo(order, sum);
        } else if (StringUtils.equals(order.getPlayCode(), "zsp_lhh")) {
            checklhh(order, NumberUtils.toInt(resultArr[0]), NumberUtils.toInt(resultArr[2]));
        } else if (StringUtils.equals(order.getPlayCode(), "zsp_zjh")) {
            checkzjh(order, resultArr);
        }else if (StringUtils.equals(order.getPlayCode(), "dwdd_b")) {
            checkNum(order, resultArr[0]);
        } else if (StringUtils.equals(order.getPlayCode(), "dwdd_s")) {
            checkNum(order, resultArr[1]);
        } else if (StringUtils.equals(order.getPlayCode(), "dwdd_g")) {
            checkNum(order, resultArr[2]);
        } else if (StringUtils.equals(order.getPlayCode(), "bddd_bddd")) {
            checkbddd_bddd(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "2ddw_bs")) {
            check2ddw(order, resultArr[0], resultArr[1]);
        }else if (StringUtils.equals(order.getPlayCode(), "2ddw_bg")) {
            check2ddw(order, resultArr[0], resultArr[2]);
        }else if (StringUtils.equals(order.getPlayCode(), "2ddw_gs")) {
            check2ddw(order, resultArr[1], resultArr[2]);
        }   else if (StringUtils.equals(order.getPlayCode(), "3ddw_bsg")) {
            check3ddw_bsg(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "2dzh_2dzh")) {
            checkzux2(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "3dzh_3dzh")) {
            checkzux3(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "2dhz_sgh")) {
            check2dhz(order, NumberUtil.add(resultArr[1], resultArr[2]).intValue());
        }  else if (StringUtils.equals(order.getPlayCode(), "2dhz_bgh")) {
            check2dhz(order, NumberUtil.add(resultArr[0], resultArr[2]).intValue());
        }  else if (StringUtils.equals(order.getPlayCode(), "2dhz_bsh")) {
            check2dhz(order, NumberUtil.add(resultArr[0], resultArr[1]).intValue());
        } else if (StringUtils.equals(order.getPlayCode(), "2dhz_sghw")) {
            checksghw(order, NumberUtil.add(resultArr[1], resultArr[2]).intValue());
        } else if (StringUtils.equals(order.getPlayCode(), "2dhz_bghw")) {
            checksghw(order, NumberUtil.add(resultArr[0], resultArr[2]).intValue());
        } else if (StringUtils.equals(order.getPlayCode(), "2dhz_bshw")) {
            checksghw(order, NumberUtil.add(resultArr[0], resultArr[1]).intValue());
        } else if (StringUtils.equals(order.getPlayCode(), "3dhz_3dhz")) {
            check3dhz(order, NumberUtil.add(resultArr[0], resultArr[1], resultArr[2]).intValue());
        } else if (StringUtils.equals(order.getPlayCode(), "3dhz_3dhzw")) {
            checksghw(order, NumberUtil.add(resultArr[0], resultArr[1], resultArr[2]).intValue());
        } else if (StringUtils.equals(order.getPlayCode(), "zux3_zux3")) {
            checkzx3(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "zux6_zux6")) {
            checkzx6(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "kd_kd")) {
            checkkd_kd(order, resultArr);
        }





    }

    private static void checkkd_kd(LotteryOrder order, String[] resultArr) {
        int max = NumberUtils.toInt(NumberUtil.max(resultArr));
        int min = NumberUtils.toInt(NumberUtil.min(resultArr));
        int sub = max - min;
        if (StringUtils.equals(order.getBetContent(), String.valueOf(sub))) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }




    private static void checkzx6(LotteryOrder order, String[] resultArr) {
        String res1 = resultArr[0];
        String res2 = resultArr[1];
        String res3 = resultArr[2];

        if (!StringUtils.equals(res1, res2) && !StringUtils.equals(res1, res3) && !StringUtils.equals(res2, res3)) {
            List<String> betList = Arrays.asList(order.getBetContent().split(","));
            if (CollUtil.containsAll(betList, Arrays.asList(resultArr))) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else {
            order.setOrderStatus(2);
        }




    }

    private static void checkzx3(LotteryOrder order, String[] resultArr) {
        boolean checkduizi = checkduizi(resultArr);
        if (!checkduizi) {
            order.setOrderStatus(2);
        } else {
            Map<String, Integer> countMap = CollUtil.countMap(Arrays.asList(resultArr));
            Optional<String> firstOptional = countMap.entrySet().stream()
                    .filter(entry -> entry.getValue() == 2)
                    .map(Map.Entry::getKey).distinct()
                    .findFirst();


            Optional<String> secondOptional = countMap.entrySet().stream()
                    .filter(entry -> entry.getValue() == 1)
                    .map(Map.Entry::getKey).distinct()
                    .findFirst();

            List<String> betList = Arrays.asList(order.getBetContent().split(","));
            if (firstOptional.isPresent() && CollUtil.contains(betList, firstOptional.get()) && secondOptional.isPresent() && CollUtil.contains(betList, secondOptional.get())) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }


    }

    private static void checksghw(LotteryOrder order, int sum) {
        int weiShu = getWeiShu(String.valueOf(sum));
        if (StringUtils.equals(order.getBetContent(), String.valueOf(weiShu))) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }


    private static void check3dhz(LotteryOrder order, int sum) {
        if (StringUtils.equals(order.getBetContent(), "0~6点")) {
            if (sum == 0 || sum == 1 || sum == 2 || sum == 3 || sum == 4 || sum == 5 || sum == 6) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "7点")) {
            if (sum == 7) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "8点")) {
            if (sum == 8) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }else if (StringUtils.equals(order.getBetContent(), "9点")) {
            if (sum == 9) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }else if (StringUtils.equals(order.getBetContent(), "10点")) {
            if (sum == 10) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }else if (StringUtils.equals(order.getBetContent(), "11点")) {
            if (sum == 11) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }else if (StringUtils.equals(order.getBetContent(), "12点")) {
            if (sum == 12) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }else if (StringUtils.equals(order.getBetContent(), "13点")) {
            if (sum == 13) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }else if (StringUtils.equals(order.getBetContent(), "14点")) {
            if (sum == 14) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }else if (StringUtils.equals(order.getBetContent(), "15点")) {
            if (sum == 15) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }else if (StringUtils.equals(order.getBetContent(), "16点")) {
            if (sum == 16) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "17点")) {
            if (sum == 17) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "18点")) {
            if (sum == 18) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "19点")) {
            if (sum == 19) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "20点")) {
            if (sum == 20) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "21~27点")) {
            if (sum == 21 || sum == 22 || sum == 23 || sum == 24 || sum == 25 || sum == 26 || sum == 27) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }
    }

    private static void check2dhz(LotteryOrder order, int sum) {
        if (StringUtils.equals(order.getBetContent(), "0~4点")) {
            if (sum == 0 || sum == 1 || sum == 2 || sum == 3 || sum == 4) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "5点")) {
            if (sum == 5) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "6点")) {
            if (sum == 6) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }else if (StringUtils.equals(order.getBetContent(), "7点")) {
            if (sum == 7) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }else if (StringUtils.equals(order.getBetContent(), "8点")) {
            if (sum == 8) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }else if (StringUtils.equals(order.getBetContent(), "9点")) {
            if (sum == 9) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }else if (StringUtils.equals(order.getBetContent(), "10点")) {
            if (sum == 10) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }else if (StringUtils.equals(order.getBetContent(), "11点")) {
            if (sum == 11) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }else if (StringUtils.equals(order.getBetContent(), "12点")) {
            if (sum == 12) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }else if (StringUtils.equals(order.getBetContent(), "13点")) {
            if (sum == 13) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }else if (StringUtils.equals(order.getBetContent(), "14~18点")) {
            if (sum == 14 || sum == 15 || sum == 16 || sum == 17 || sum == 18) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }
    }

    private static void checkzux2(LotteryOrder order, String[] resultArr) {
        char[] chars = order.getBetContent().toCharArray();
        ArrayList<String> betStrings = CollUtil.newArrayList(String.valueOf(chars[0]), String.valueOf(chars[1]));
        // 所有组合
        List<String[]> combinationSelect = MathUtil.combinationSelect(resultArr, 2);

        Boolean b = false;
        for (String[] resArr : combinationSelect) {
            if (CollUtil.containsAll(Arrays.asList(resArr), betStrings) && CollUtil.containsAll(betStrings, Arrays.asList(resArr))) {
                b = true;
            }
        }
        if (b) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }

    }



    private static void checkzux3(LotteryOrder order, String[] resultArr) {
        char[] chars = order.getBetContent().toCharArray();
        ArrayList<String> betStrings = CollUtil.newArrayList(String.valueOf(chars[0]), String.valueOf(chars[1]), String.valueOf(chars[2]));
        // 所有组合
        List<String[]> combinationSelect = MathUtil.combinationSelect(resultArr, 3);

        Boolean b = false;
        for (String[] resArr : combinationSelect) {
            if (CollUtil.containsAll(Arrays.asList(resArr), betStrings) && CollUtil.containsAll(betStrings, Arrays.asList(resArr))) {
                b = true;
            }
        }
        if (b) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }

    }

    private static void check2ddw(LotteryOrder lotteryOrder, String resNum1, String resNum2) {
        String betNum1 = StringUtils.split(lotteryOrder.getBetContent(), ",")[0];
        String betNum2 = StringUtils.split(lotteryOrder.getBetContent(), ",")[1];

        if (StringUtils.equals(betNum1, resNum1) && StringUtils.equals(betNum2, resNum2)) {
            lotteryOrder.setOrderStatus(1);
        } else {
            lotteryOrder.setOrderStatus(2);
        }

    }

    private static void check3ddw_bsg(LotteryOrder lotteryOrder, String[] resultArr) {
        String betNum1 = StringUtils.split(lotteryOrder.getBetContent(), ",")[0];
        String betNum2 = StringUtils.split(lotteryOrder.getBetContent(), ",")[1];
        String betNum3 = StringUtils.split(lotteryOrder.getBetContent(), ",")[2];
        String resNum1 = resultArr[0];
        String resNum2 = resultArr[1];
        String resNum3 = resultArr[2];

        if (StringUtils.equals(betNum1, resNum1) && StringUtils.equals(betNum2, resNum2) && StringUtils.equals(betNum3, resNum3)) {
            lotteryOrder.setOrderStatus(1);
        } else {
            lotteryOrder.setOrderStatus(2);
        }

    }




    private static int getWeiShu(String num) {
        int weishu = NumberUtils.toInt(num);
        if (weishu >= 0 && weishu <= 9) {
            return weishu;
        }
        char[] chars = num.toCharArray();
        return NumberUtils.toInt(String.valueOf(chars[1]));
    }


    private static boolean checkBaozi(String[] resultArr) {
        return StringUtils.equals(resultArr[0], resultArr[1]) && StringUtils.equals(resultArr[1], resultArr[2]);
    }

    /**
     * 开出的三个号码有两个相同，即为对子，豹子除外。
     * @param resultArr
     * @return
     */
    private static boolean checkduizi(String[] resultArr) {
        if (checkBaozi(resultArr)) {
            return false;
        }
        Map<String, Integer> countMap = CollUtil.countMap(Arrays.asList(resultArr));
        if (countMap.size() == 2) {
            return true;
        }
        return false;
    }


    /**
     * 开出的三个号码不分順序相连即为顺子（8,9,0、9,0,1不为顺子）
     * @param resultArr
     * @return
     */
    private static boolean checksz(String[] resultArr) {
        List<String> sortList = CollUtil.sort(Arrays.asList(resultArr), Comparator.comparingInt(NumberUtils::toInt));
        int sub1 = NumberUtils.toInt(sortList.get(1)) - NumberUtils.toInt(sortList.get(0));
        int sub2 = NumberUtils.toInt(sortList.get(2)) - NumberUtils.toInt(sortList.get(1));
        if (sub1 == 1 && sub2 == 1) {
            return true;
        }
        return false;
    }


    private static void checkNum(LotteryOrder order, String resultNum) {
        if (StringUtils.equals(order.getBetContent(), resultNum)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }
    private static void checkzjh(LotteryOrder order, String[] resultArr) {
        if (StringUtils.equals(order.getBetContent(), "豹子")) {
            if (checkBaozi(resultArr)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(order.getBetContent(), "对子")) {
            if (checkduizi(resultArr)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(order.getBetContent(), "顺子")) {
            if (checksz(resultArr)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }

    private static void checklhh(LotteryOrder order, int num1, int num2) {
        if (StringUtils.equals(order.getBetContent(), "龙")) {
            if (num1 > num2) {
                order.setOrderStatus(1);

            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "虎")) {
            if (num1 < num2) {
                order.setOrderStatus(1);

            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "和")) {
            if (num1 == num2) {
                order.setOrderStatus(1);

            } else {
                order.setOrderStatus(2);
            }

        }
    }


    private static void checkbddd_bddd(LotteryOrder order, String[] resultArr) {
        if (CollUtil.contains(Arrays.asList(resultArr), order.getBetContent())) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }

    }


    private static void checkBo(LotteryOrder order, int num) {
        if (StringUtils.equals(order.getBetContent(), "红波")) {
            if (CollUtil.contains(HONG_BO, String.valueOf(num))) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "绿波")) {
            if (CollUtil.contains(LV_BO, String.valueOf(num))) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "蓝波")) {
            if (CollUtil.contains(LAN_BO, String.valueOf(num))) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        }
    }
    private static void checkSumdxds(LotteryOrder order, int sum) {
        if (StringUtils.equals(order.getBetContent(), "大")) {
            if (sum >= 14 && sum <= 27) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "小")) {
            if (sum >= 0 && sum <= 13) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(order.getBetContent(), "单")) {
            // 1、3、5、7、9、11、13、15、17、19、21、23、25、27为单，0、2、4、6、8、10、12、14、16、18、20、22、24、26为双。
            if (sum % 2 == 1) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "双")) {
            if (sum % 2 == 0) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }
    private static void checkSumWdx(LotteryOrder order, int sum) {
        int weiShu = getWeiShu(String.valueOf(sum));
        if (StringUtils.equals(order.getBetContent(), "大")) {
            if (weiShu >= 5 && weiShu <= 9) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "小")) {
            if (weiShu >= 0 && weiShu <= 4) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(order.getBetContent(), "质")) {
            if (StringUtils.equalsAny(String.valueOf(weiShu), "1", "2", "3", "5", "7")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(order.getBetContent(), "合")) {
            if (StringUtils.equalsAny(String.valueOf(weiShu), "0", "4", "6", "8", "9")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }

    /**二胆和数单双
     * 1、3、5、7、9、11、13、15、17为单，0、2、4、6、8、10、12、14、16、18为双。
     * @param order
     * @param num1
     * @param num2
     */
    private static void twohedanshuang(LotteryOrder order, String num1, String num2) {
        Integer numSum = NumberUtil.add(num1, num2).intValue();
        if (StringUtils.equals(order.getBetContent(), "单")) {
            if (StringUtils.equalsAny(numSum.toString(), "1", "3", "5", "7", "9", "11", "13", "15", "17")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(order.getBetContent(), "双")) {
            if (StringUtils.equalsAny(numSum.toString(), "0", "2", "4", "6", "8", "10", "12", "14", "16", "18")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }

    private static void checkdwdxds(LotteryOrder order, String resultNum) {
        String betContent = order.getBetContent();
        Integer resultInt = Integer.valueOf(resultNum);
        if (StringUtils.equals(betContent, "大")) {
            if (resultInt >= 5 && resultInt <= 9) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(betContent, "小")) {
            if (resultInt <= 4 && resultInt >= 0) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(betContent, "单")) {
            if (resultInt % 2 == 1) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(betContent, "双")) {
            if (resultInt % 2 == 0) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }


        } else if (StringUtils.equals(betContent, "质")) {
            if (StringUtils.equalsAny(resultNum, "1", "2", "3", "5", "7")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }

        } else if (StringUtils.equals(betContent, "合")) {
            if (StringUtils.equalsAny(resultNum, "0", "4", "6", "8", "9")) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }


    }


}
