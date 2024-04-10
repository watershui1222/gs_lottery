package com.gs.business.utils.lottery;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.math.MathUtil;
import com.gs.commons.entity.LotteryOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

/**
 * 重庆时时彩规则说明
 * 该游戏的投注时间、开奖时间和开奖号码与重庆时时彩完全同步，北京时间（GMT+8）每天凌晨00:10开始到凌晨03:10结束，早上07:10开始到晚上23:50结束，每日59期；每20分钟开一次奖，每天开奖59期。
 * 具体游戏规则如下:
 * 1.第一球~第五球
 * 第一球特~第五球特：第一球特、第二球特、第三球特、第四球特、第五球特：指下注的每一球特与开出之号码其开奖顺序及开奖号码相同，视为中奖，如第一球开出号码8，下注第一球为8者视为中奖，其余情形视为不中奖。
 * 单双大小：根据相应单项投注第一球特 ~ 第五球特开出的球号，判断胜负。
 * 大小：根据相应单项投注的第一球特 ~ 第五球特开出的球号大於或等於5为特码大，小於或等於4为特码小。
 * 单双：根据相应单项投注的第一球特 ~ 第五球特开出的球号为双数叫特双，如2、6；特码为单数叫特单，如1、3。
 * 2.总和单双大小：
 * 大小：根据相应单项投注的第一球特 ~ 第五球特开出的球号大於或等於23为特码大，小於或等於22为特码小。
 * 单双：根据相应单项投注的第一球特 ~ 第五球特开出的球号数字总和值是双数为总和双，数字总和值是单数为总和单。
 * 3.前三特殊玩法： 豹子 > 顺子 > 对子 > 半顺 > 杂六 。
 * 豹子：中奖号码的万位千位百位数字都相同。----如中奖号码为000、111、999等，中奖号码的万位千位百位数字相同，则投注豹子者视为中奖，其它视为不中奖。
 * 顺子：中奖号码的万位千位百位数字都相连，不分顺序。（数字9、0、1相连）----如中奖号码为123、901、321、546等，中奖号码万位千位百位数字相连，则投注顺子者视为中奖，其它视为不中奖。
 * 对子：中奖号码的万位千位百位任意两位数字相同。（不包括豹子）----如中奖号码为001，112、696，中奖号码有两位数字相同，则投注对子者视为中奖，其它视为不中奖。如果开奖号码为豹子,则对子视为不中奖。如中奖号码为001，112、696，中奖号码有两位数字相同，则投注对子者视为中奖，其它视为不中奖。
 * 半顺：中奖号码的万位千位百位任意两位数字相连，不分顺序。（不包括顺子、对子。）----如中奖号码为125、540、390、706，中奖号码有两位数字相连，则投注半顺者视为中奖，其它视为不中奖。如果开奖号码为顺子、对子,则半顺视为不中奖。--如中奖号码为123、901、556、233，视为不中奖。
 * 杂六：不包括豹子、对子、顺子、半顺的所有中奖号码。----如中奖号码为157，中奖号码位数之间无关联性，则投注杂六者视为中奖，其它视为不中奖。
 * 4.中三特殊玩法： 豹子 > 顺子 > 对子 > 半顺 > 杂六 。
 * 豹子：中奖号码的千位百位十位数字都相同。----如中奖号码为000、111、999等，中奖号码的千位百位十位数字相同，则投注豹子者视为中奖，其它视为不中奖。
 * 顺子：中奖号码的千位百位十位数字都相连，不分顺序。（数字9、0、1相连）----如中奖号码为123、901、321、546等，中奖号码千位百位十位数字相连，则投注顺子者视为中奖，其它视为不中奖。
 * 对子：中奖号码的千位百位十位任意两位数字相同。（不包括豹子）----如中奖号码为001，112、696，中奖号码有两位数字相同，则投注对子者视为中奖，其它视为不中奖。如果开奖号码为豹子,则对子视为不中奖。如中奖号码为001，112、696，中奖号码有两位数字相同，则投注对子者视为中奖，其它视为不中奖。
 * 半顺：中奖号码的千位百位十位任意两位数字相连，不分顺序。（不包括顺子、对子。）----如中奖号码为125、540、390、706，中奖号码有两位数字相连，则投注半顺者视为中奖，其它视为不中奖。如果开奖号码为顺子、对子,则半顺视为不中奖。--如中奖号码为123、901、556、233，视为不中奖。
 * 杂六：不包括豹子、对子、顺子、半顺的所有中奖号码。----如中奖号码为157，中奖号码位数之间无关联性，则投注杂六者视为中奖，其它视为不中奖。
 * 5.后三特殊玩法： 豹子 > 顺子 > 对子 > 半顺 > 杂六 。
 * 豹子：中奖号码的百位十位个位数字都相同。----如中奖号码为000、111、999等，中奖号码的百位十位个位数字相同，则投注豹子者视为中奖，其它视为不中奖。
 * 顺子：中奖号码的百位十位个位数字都相连，不分顺序。（数字9、0、1相连）----如中奖号码为123、901、321、546等，中奖号码百位十位个位数字相连，则投注顺子者视为中奖，其它视为不中奖。
 * 对子：中奖号码的百位十位个位任意两位数字相同。（不包括豹子）----如中奖号码为001，112、696，中奖号码有两位数字相同，则投注对子者视为中奖，其它视为不中奖。如果开奖号码为豹子,则对子视为不中奖。如中奖号码为001，112、696，中奖号码有两位数字相同，则投注对子者视为中奖，其它视为不中奖。
 * 半顺：中奖号码的百位十位个位任意两位数字相连，不分顺序。（不包括顺子、对子。）----如中奖号码为125、540、390、706，中奖号码有两位数字相连，则投注半顺者视为中奖，其它视为不中奖。如果开奖号码为顺子、对子,则半顺视为不中奖。--如中奖号码为123、901、556、233，视为不中奖。
 * 杂六：不包括豹子、对子、顺子、半顺的所有中奖号码。----如中奖号码为157，中奖号码位数之间无关联性，则投注杂六者视为中奖，其它视为不中奖。
 * 6.龙虎和特殊玩法： 龙 > 虎 > 和 （0为最小，9为最大）。
 * 龙：开出之号码第一球（万位）的中奖号码大于第五球（个位）的中奖号码。如 第一球开出4 第五球开出2；第一球开出9 第五球开出8；第一球开出5 第五球开出1...中奖为龙。开和视为不中奖
 * 虎：开出之号码第一球（万位）的中奖号码小于第五球（个位）的中奖号码。如 第一球开出7 第五球开出9；第一球开出3 第五球开出5；第一球开出5 第五球开出8...中奖为虎。开和视为不中奖
 * 和：开出之号码第一球（万位）的中奖号码等于第五球（个位）的中奖号码，例如开出结果：2***2则投注和局者视为中奖，其它视为不中奖。
 * 7.斗牛
 * 开奖号码任意3个号码相加的和为10的倍数（0为10）视为有牛，否则为无牛，剩余的两个号码和数的尾数视为牛几。
 * 牛牛：任意3个号码相加的和为10的倍数，后剩余的两个号码和值也为10的倍数，如：开奖号56937，5+6+9=20/3+7=10，下注【牛牛】视为中奖。
 * 牛一～牛九：任意3个号码相加的和为10的倍数，后剩余的两个号码和值尾数视为牛号。如：开奖号99253，9+9+2=20 / 5+3=8，下注【牛八】视为中奖。
 * 无牛：任意3个号码相加无法组成10的倍数，如：开奖号22381，下注【无牛】视为中奖。
 * 8.斗牛两面
 * 牛大：牌型为，牛牛，牛9、牛8、牛7、牛6，即视为中奖，若开奖无牛视为不中奖。
 * 牛小：牌型为，牛5，牛4、牛3、牛2、牛1，即视为中奖，若开奖无牛视为不中奖。
 * 牛单：牌型为，牛1，牛3、牛5、牛7、牛9，即视为中奖，若开奖无牛视为不中奖。
 * 牛双：牌型为，牛2，牛4、牛6、牛8、牛牛，即视为中奖，若开奖无牛视为不中奖。
 * 9.斗牛梭哈 (开奖五个号码)
 * 五条：开奖的五个号码全部相同，如：22222、66666、88888，投注【五条】视为中奖，其余不中奖。
 * 四条：开奖号码中有四个号码相同，如：22221、66663、88885，投注【四条】视为中奖，其余不中奖。
 * 葫芦：开奖号码中有三个号码相同(三条)、且另外两个号码相同(一对)，如：22211、66633， 投注【葫芦】视为中奖，其余不中奖。
 * 顺子：开奖五个号码数字相连(不分顺序)，如：01234、54231、89021、32019，投注【顺子】视为中奖，其余不中奖。
 * 三条：开奖号码中有三个号码相同、且另外两个号码不相同，如：13511、20252、22231，投注【三条】视为中奖，其余不中奖。
 * 两对：开奖号码中有两组号码相同，如：22166、66355、82668，投注【两对】视为中奖，其余不中奖。
 * 一对：开奖号码中只有一组号码相同，如：22168、66315、82968，投注【一对】视为中奖，其余不中奖。
 * 高牌：当开奖的五个号码之间无关联性，无法成立五条、四条、葫芦、顺子、三条、两对、一对，如：23186、13579、21968，投注【高牌】即视为中奖，其余不中奖。
 */
public class CQSSCUtil {

    public static void checkWin(LotteryOrder order) {
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
            checkSq(order, resultArr[0], resultArr[1], resultArr[2]);
        } else if (StringUtils.equals(order.getPlayCode(), "qzh_z3")) {
            checkSq(order, resultArr[0], resultArr[1], resultArr[2]);
        } else if (StringUtils.equals(order.getPlayCode(), "qzh_h3")) {
            checkSq(order, resultArr[0], resultArr[1], resultArr[2]);
        } else if (StringUtils.equals(order.getPlayCode(), "douniu_douniu")) {
            checkDn(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "douniu_lm")) {
            checkDnLm(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "douniu_sh")) {
            checkDnSh(order, resultArr);
        }
    }

    /**
     * 斗牛-梭哈
     * @param order
     * @param resultArr
     */
    private static void checkDnSh(LotteryOrder order, String[] resultArr) {
        String betContent = order.getBetContent();
        if (StringUtils.equals("一对", betContent)) {
            if (checkYidui(resultArr)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals("两对", betContent)) {
            if (checkLiangDui(resultArr)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals("三条", betContent)) {
            if (checkSantiao(resultArr)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals("顺子", betContent)) {
            if (checkSz(resultArr)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals("葫芦", betContent)) {
            if (checkHulu(resultArr)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals("四条", betContent)) {
            if (checkSt(resultArr)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals("五条", betContent)) {
            if (checkWt(resultArr)) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals("高排", betContent)) {

        }
    }

    private static boolean checkLiangDui(String[] resultArr) {
        Map<String, Integer> map = CollUtil.countMap(Arrays.asList(resultArr));
        if (map.size() == 3) {
            // 计算对子出现了几次
            for (String key : map.keySet()) {
                if (map.get(key).intValue() == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkHulu(String[] resultArr) {
        Map<String, Integer> map = CollUtil.countMap(Arrays.asList(resultArr));
        if (map.size() == 2) {
            for (String key : map.keySet()) {
                if (map.get(key).intValue() == 3) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkYidui(String[] resultArr) {
        Map<String, Integer> map = CollUtil.countMap(Arrays.asList(resultArr));
        for (String key : map.keySet()) {
            // 如果有一个数字出现了2次
            if (map.get(key).intValue() == 2) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkSantiao(String[] resultArr) {
        Map<String, Integer> map = CollUtil.countMap(Arrays.asList(resultArr));
        if (map.size() == 3) {
            for (String key : map.keySet()) {
                // 如果有一个数字出现了3次
                if (map.get(key).intValue() == 3) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkSz(String[] resultArr) {
        // 生产所有排列
        List<String[]> genList = MathUtil.arrangementSelect(new String[]{resultArr[0], resultArr[1], resultArr[2],resultArr[3],resultArr[4]});
        // 三个号码球组成的所有排列组合
        List<String> all = new ArrayList<>();
        for (String[] strings : genList) {
            all.add(strings[0] + strings[1] + strings[2] + strings[3]+ strings[4]);
        }
        // 所有顺子号
        List list = Arrays.asList(new String[]{"01234", "12345", "23456", "34567", "45678", "56789", "67890", "78901", "89012", "90123"});

        // 是否有顺子
        return CollUtil.containsAny(all, list);
    }

    private static boolean checkSt(String[] resultArr) {
        Map<String, Integer> map = CollUtil.countMap(Arrays.asList(resultArr));
        if (map.size() == 2) {
            for (String key : map.keySet()) {
                // 如果有一个数字出现了4次
                if (map.get(key).intValue() == 4) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkWt(String[] resultArr) {
        Map<String, Integer> map = CollUtil.countMap(Arrays.asList(resultArr));
        if (map.size() == 1) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String resultArr[] = new String[]{"4", "5", "5", "5", "5"};
        Map<String, Integer> map = CollUtil.countMap(Arrays.asList(resultArr));
        System.out.println(map.size());
        System.out.println(CollUtil.countMap(Arrays.asList(resultArr)));
    }


    /**
     * 斗牛-两面
     * @param order
     * @param resultArr
     */
    private static void checkDnLm(LotteryOrder order, String[] resultArr) {
        String betContent = order.getBetContent();
        int niu = (NumberUtils.toInt(resultArr[0]) + NumberUtils.toInt(resultArr[1]) + NumberUtils.toInt(resultArr[2]) + NumberUtils.toInt(resultArr[3]) + NumberUtils.toInt(resultArr[4])) %10;
        if(StringUtils.equals("牛单", betContent)) {
            if (hasNiu(resultArr) && (niu == 1 || niu == 3 || niu == 5 || niu == 7 || niu == 9)) {
                order.setOrderStatus(2);
            } else {
                order.setOrderStatus(1);
            }
        } else if (StringUtils.equals("牛双", betContent)) {
            if (hasNiu(resultArr) && (niu == 2 || niu == 4 || niu == 6 || niu == 8 || niu == 10)) {
                order.setOrderStatus(2);
            } else {
                order.setOrderStatus(1);
            }
        } else if (StringUtils.equals("牛大", betContent)) {
            if (hasNiu(resultArr) && (niu == 6 || niu == 7 || niu == 8 || niu == 9 || niu == 0)) {
                order.setOrderStatus(2);
            } else {
                order.setOrderStatus(1);
            }
        } else if (StringUtils.equals("牛小", betContent)) {
            if (hasNiu(resultArr) && (niu == 1 || niu == 2 || niu == 3 || niu == 4 || niu == 5)) {
                order.setOrderStatus(2);
            } else {
                order.setOrderStatus(1);
            }
        }
    }

    /**
     * 斗牛
     * @param order
     * @param resultArr
     */
    private static void checkDn(LotteryOrder order, String[] resultArr) {
        String betContent = order.getBetContent();
        int niu = (NumberUtils.toInt(resultArr[0]) + NumberUtils.toInt(resultArr[1]) + NumberUtils.toInt(resultArr[2]) + NumberUtils.toInt(resultArr[3]) + NumberUtils.toInt(resultArr[4])) %10;
        if(StringUtils.equals("牛牛", betContent)) {
            if (hasNiu(resultArr) && niu == 0) {
                order.setOrderStatus(2);
            } else {
                order.setOrderStatus(1);
            }
        } else if (StringUtils.equals("牛九", betContent)) {
            if (hasNiu(resultArr) && niu == 9) {
                order.setOrderStatus(2);
            } else {
                order.setOrderStatus(1);
            }
        } else if (StringUtils.equals("牛八", betContent)) {
            if (hasNiu(resultArr) && niu == 8) {
                order.setOrderStatus(2);
            } else {
                order.setOrderStatus(1);
            }
        } else if (StringUtils.equals("牛七", betContent)) {
            if (hasNiu(resultArr) && niu == 7) {
                order.setOrderStatus(2);
            } else {
                order.setOrderStatus(1);
            }
        } else if (StringUtils.equals("牛六", betContent)) {
            if (hasNiu(resultArr) && niu == 6) {
                order.setOrderStatus(2);
            } else {
                order.setOrderStatus(1);
            }
        } else if (StringUtils.equals("牛五", betContent)) {
            if (hasNiu(resultArr) && niu == 5) {
                order.setOrderStatus(2);
            } else {
                order.setOrderStatus(1);
            }
        } else if (StringUtils.equals("牛四", betContent)) {
            if (hasNiu(resultArr) && niu == 4) {
                order.setOrderStatus(2);
            } else {
                order.setOrderStatus(1);
            }
        } else if (StringUtils.equals("牛三", betContent)) {
            if (hasNiu(resultArr) && niu == 3) {
                order.setOrderStatus(2);
            } else {
                order.setOrderStatus(1);
            }
        } else if (StringUtils.equals("牛二", betContent)) {
            if (hasNiu(resultArr) && niu == 2) {
                order.setOrderStatus(2);
            } else {
                order.setOrderStatus(1);
            }
        } else if (StringUtils.equals("牛一", betContent)) {
            if (hasNiu(resultArr) && niu == 1) {
                order.setOrderStatus(2);
            } else {
                order.setOrderStatus(1);
            }
        } else if (StringUtils.equals("无牛", betContent)) {
            if (hasNiu(resultArr)) {
                order.setOrderStatus(2);
            } else {
                order.setOrderStatus(1);
            }
        }
    }

    /**
     * 判断是否有牛
     * @return
     */
    private static boolean hasNiu(String[] resultArr) {
        // 计算出所有三位数组合
        List<String[]> allComb = MathUtil.combinationSelect(resultArr, 3);
        // 判断是否有牛
        for (String[] strings : allComb) {
            int num1 = NumberUtils.toInt(strings[0]);
            int num2 = NumberUtils.toInt(strings[1]);
            int num3 = NumberUtils.toInt(strings[2]);
            if ((num1 + num2 + num3) % 10 == 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * 三球玩法
     * @param order
     * @param d1q
     * @param d2q
     * @param d3q
     */
    private static void checkSq(LotteryOrder order, String d1q, String d2q, String d3q) {
        String betContent = order.getBetContent();
        if (StringUtils.equals("豹子", betContent)) {
            checkSqBz(order, d1q, d2q, d3q);
        } else if (StringUtils.equals("顺子", betContent)) {
            checkSqSz(order, d1q, d2q, d3q);
        } else if (StringUtils.equals("对子", betContent)) {
            checkSqDz(order, d1q, d2q, d3q);
        } else if (StringUtils.equals("半顺", betContent)) {
            checkSqBs(order, d1q, d2q, d3q);
        } else if (StringUtils.equals("杂六", betContent)) {
            checkSqZl(order, d1q, d2q, d3q);
        }
    }

    /**
     * 三球-杂六
     *
     * @param order
     */
    private static void checkSqZl(LotteryOrder order, String d1q, String d2q, String d3q) {
        // 不包括豹子、对子、顺子、半顺的所有中奖号码。----如中奖号码为157，中奖号码位数之间无关联性，则投注杂六者视为中奖，其它视为不中奖。
        if (checkBz(d1q,d2q,d3q) || checkDz(d1q,d2q,d3q) || checkSz(d1q,d2q,d3q) || checkBs(d1q,d2q,d3q)) {
            order.setOrderStatus(2);
            return;
        }
        order.setOrderStatus(1);
    }

    /**
     * 三球-半顺
     *
     * @param order
     */
    private static void checkSqBs(LotteryOrder order, String d1q, String d2q, String d3q) {
        //中奖号码的万位千位百位任意两位数字相连，不分顺序。（不包括顺子、对子。）
        // ----如中奖号码为125、540、390、706，中奖号码有两位数字相连，则投注半顺者视为中奖，其它视为不中奖。如果开奖号码为顺子、对子,则半顺视为不中奖。--如中奖号码为123、901、556、233，视为不中奖。
        // 开出顺子不中奖 开出对子不中奖
        if (checkSz(d1q, d2q, d3q) || checkDz(d1q,d2q,d3q)) {
            order.setOrderStatus(2);
            return;
        }
        if (checkBs(d1q,d2q,d3q)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }

    /**
     * 三球-豹子
     *
     * @param order
     */
    private static void checkSqBz(LotteryOrder order, String d1q, String d2q, String d3q) {
        // 中奖号码的万位千位百位数字都相同。----如中奖号码为000、111、999等，中奖号码的万位千位百位数字相同，则投注豹子者视为中奖，其它视为不中奖。
        if (checkBz(d1q,d2q,d3q)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }

    /**
     * 三球-对子
     *
     * @param order
     */
    private static void checkSqDz(LotteryOrder order, String d1q, String d2q, String d3q) {
        // 中奖号码的万位千位百位任意两位数字相同。（不包括豹子）
        // ----如中奖号码为001，112、696，中奖号码有两位数字相同，则投注对子者视为中奖，其它视为不中奖。如果开奖号码为豹子,则对子视为不中奖。
        // 如中奖号码为001，112、696，中奖号码有两位数字相同，则投注对子者视为中奖，其它视为不中奖。
        // 开出豹子不算中奖
        if (checkBz(d1q, d2q, d3q)) {
            order.setOrderStatus(2);
            return;
        }
        if (checkDz(d1q,d2q,d3q)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }


    /**
     * 三球-顺子
     *
     * @param order
     */
    private static void checkSqSz(LotteryOrder order, String d1q, String d2q, String d3q) {
        // 中奖号码的万位千位百位数字都相连，不分顺序。（数字9、0、1相连）----如中奖号码为123、901、321、546等，中奖号码万位千位百位数字相连，则投注顺子者视为中奖，其它视为不中奖。
        // 是否有顺子
        if (checkSz(d1q, d2q, d3q)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }

    private static boolean checkDz(String d1q, String d2q, String d3q) {
        Set<String> set = new HashSet<>();
        set.add(d1q);
        set.add(d2q);
        set.add(d3q);
        if (set.size() == 2) {
            return true;
        }
        return false;
    }

    private static boolean checkBz(String d1q, String d2q, String d3q) {
        if (StringUtils.equals(d1q, d2q) && StringUtils.equals(d2q, d3q)) {
            return true;
        }
        return false;
    }

    private static boolean checkBs(String d1q, String d2q, String d3q) {
        // 生产所有排列
        List<String[]> genList = MathUtil.arrangementSelect(new String[]{d1q, d2q, d3q}, 2);
        // 三个号码球组成的所有排列组合
        List<String> all = new ArrayList<>();
        for (String[] strings : genList) {
            all.add(strings[0] + strings[1]);
        }
        // 所有顺子号
        List list = Arrays.asList(new String[]{"01", "12", "23", "34", "45", "56", "67", "78", "89", "90"});

        // 是否有顺子
        return CollUtil.containsAny(all, list);
    }

    private static boolean checkSz(String d1q, String d2q, String d3q) {
        // 生产所有排列
        List<String[]> genList = MathUtil.arrangementSelect(new String[]{d1q, d2q, d3q});
        // 三个号码球组成的所有排列组合
        List<String> all = new ArrayList<>();
        for (String[] strings : genList) {
            all.add(strings[0] + strings[1] + strings[2]);
        }
        // 所有顺子号
        List list = Arrays.asList(new String[]{"012", "123", "234", "345", "456", "567", "678", "789", "890", "901"});

        // 是否有顺子
        return CollUtil.containsAny(all, list);
    }

    /**
     * 两面-大小单双
     *
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
     *
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
     *
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
