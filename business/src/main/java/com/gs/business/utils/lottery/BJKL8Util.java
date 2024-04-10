package com.gs.business.utils.lottery;

import com.gs.commons.entity.LotteryOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 该游戏投注、开奖时间和开奖号码与“北京快乐8”完全同步，北京时间（GMT+8）每天白天从早上09:05开到晚上23:55，每5分钟开一次奖，每天开奖179期。
 * 具体游戏规则如下:
 * 1.正码
 * 投注的1个号码与当期摇出的20个号码中的任1个号码相同，则中奖
 * 2.总和：以所有开出的全部20个号码加起来的和值来判定
 * 总和大/小：20个号码加总的和值大于810，为和大；20个号码加总的和值小于810，则为和小。
 * 总和单/双：20个号码加总的和值为单，叫做和单；20个号码加总的和值为双，叫做和双。
 * 总和810：20个号码加总的和值等于810，叫和值810。(当和值等于810则大、小、单、双不退回本金)举例：开奖号码为1，2，3，4，5，6，7，8，9，10，11，12，13，14，15，16，17，18，19，20；那么此20个开奖号码的和值总和为210，则为小，为双。则投注小和双者中奖。当和值等于810则大、小、单、双不退回本金
 * 3.总和过关：开出的20个号码的总和的游戏，分为「大单」，「小单」，「大双」和「小双」。
 * 总和大于810为「总数大」,小于810为「总数小」。
 * 总和为双数叫「双」，单数叫「单」。
 * 通过大小和单双组合产生「大单」，「小单」，「大双」和「小双」四种结果。
 * 总和等于810，则大、小、单、双不退回本金
 * 举例：开奖号码为01、04、05、10、11、13、20、27、30、32、33、36、40、47、54、59、61、64、67、79，总和是693，总和小于810，并且是单数，则为「小单」。下注「小单」为赢，反之则输。
 * 4.前后和：开奖号码1至40为前盘号码，41至80为后盘号码。
 * 开出的20个号码中：如前盘号码（1-40）在此局开出号码数目占多数时，此局为：前(多)。
 * 后盘号码（41-80）在此局开出号码数目占多数时，此局为:后(多)。
 * 通前盘号码（1－40）和后盘号码（41-80）在此局开出的数目相同时（各10个数字），此局为：前后(和)。
 * 举例：此局开出1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20. 此局为：前(多)。
 * 举例：此局开出41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60 此局为：后(多)。
 * 举例：此局开出 1,2,3,4,5,6,7,8,9,10,41,42,43,44,45,46,47,48,49,50 此局为：前后(和)。
 * 5.单双和
 * 开奖号码中1，3，5，7，…，75，77，79为单数号码，2，4，6，8，……，76，78，80为双数号码。当期开出的20个中奖号码中，如单数号码数目占多数时（超过10个），则为单(多)，投注单(多)中奖；双数号码占多数时（超过10个），则为双(多)，投注双(多)中奖；如果单数和双数号码数目相同时（均为 10个），则为单双(和)，投注单双(和)者中奖。
 * 举例：此期开出1，3，5，7，9，11，13，15，17，19，21，22，24，26，28，30，32，34，46，68， 其中单数11个双数9个，此期为：单(多)。
 * 举例：此期开出2，4，6，8，10，12，14，16，44，48，66，68，25，27，31，35，37，39，41，55， 其中双数12个单数8个，此期为：双(多)。
 * 举例：此期开出2，4，6，8，10，12，14，16，18，20，41，43，45，47，49，51，53，55，57，59， 其中单数10个双数10个，此期为：单双(和)。
 * 6.五行
 * 开出的20个号码的总和分在5个段，以金、木、水、火、土命名：金（210～695）、木（696～763）、水（764～855）、火（856～923）和土（924～1410）。
 * 举例(羊年)：开奖号码为01、04、05、10、11、13、20、27、30、32、33、36、40、47、54、59、61、64、67、79，总和是693，
 * 则总分数在210－695段内，则开出的是「金」。下注「金」为赢，反之则输。
 */
public class BJKL8Util {

    public static void checkWin(LotteryOrder order) {
        String openResult = order.getOpenResult();
        String[] resultArr = openResult.split(",");
        int sum = 0;
        for (String s : resultArr) {
            sum += NumberUtils.toInt(s);
        }
        if (StringUtils.equals(order.getPlayCode(), "zhenghe_sum")) {
            checkSum(order, sum);
        } else if (StringUtils.equals(order.getPlayCode(), "zhenghe_wxing")) {
            checkWuxing(order, sum);
        } else if (StringUtils.equals(order.getPlayCode(), "zhenghe_qhh")) {
            checkQhh(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "zhenghe_dsh")) {
            checkDsh(order, resultArr);
        } else if (StringUtils.equals(order.getPlayCode(), "zm_zm")) {
            checkZm(order, resultArr);
        }
    }

    private static void checkQhh(LotteryOrder order, String[] resultArr) {
        String betContent = order.getBetContent();
        int qian = 0;
        int hou = 0;
        for (String s : resultArr) {
            int num = NumberUtils.toInt(s);
            if (num >= 1 && num <= 40) {
                qian += 1;
            } else {
                hou += 1;
            }
        }
        if (StringUtils.equals(betContent, "前多")) {
            if (qian > hou) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "后多")) {
            if (hou > qian) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "前后和")) {
            if (qian == hou) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }

    private static void checkDsh(LotteryOrder order, String[] resultArr) {
        String betContent = order.getBetContent();
        int dan = 0;
        int shuang = 0;
        for (String s : resultArr) {
            int num = NumberUtils.toInt(s);
            if (num %2 == 0) {
                shuang += 1;
            } else {
                dan += 1;
            }
        }
        if (StringUtils.equals(betContent, "单多")) {
            if (dan > shuang) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "双多")) {
            if (shuang > dan) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "单双和")) {
            if (dan == shuang) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }

    private static void checkWuxing(LotteryOrder order, int sum) {
        String betContent = order.getBetContent();
        if (StringUtils.equals(betContent, "金")) {
            if (sum >= 210 && sum <= 695) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "木")) {
            if (sum >= 696 && sum <= 763) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "水")) {
            if (sum >= 764 && sum <= 855) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "火")) {
            if (sum >= 856 && sum <= 923) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "土")) {
            if (sum >= 924 && sum <= 1410) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }

    private static void checkSum(LotteryOrder order, int sum) {
        String betContent = order.getBetContent();
        if (StringUtils.equals(betContent, "总和大")) {
            if (sum > 810) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "总和小")) {
            if (sum < 810) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
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
        } else if (StringUtils.equals(betContent, "总和810")) {
            if (sum == 810) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "总大单")) {
            if (sum > 810 && sum %2 == 1) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "总小单")) {
            if (sum < 810 && sum %2 == 1) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "总大双")) {
            if (sum > 810 && sum %2 == 0) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        } else if (StringUtils.equals(betContent, "总小双")) {
            if (sum < 810 && sum %2 == 0) {
                order.setOrderStatus(1);
            } else {
                order.setOrderStatus(2);
            }
        }
    }

    private static void checkZm(LotteryOrder order, String[] resultArr) {
        String betContent = order.getBetContent();
        List<String> results = Arrays.asList(resultArr);
        if (results.contains(betContent)) {
            order.setOrderStatus(1);
        } else {
            order.setOrderStatus(2);
        }
    }
}
