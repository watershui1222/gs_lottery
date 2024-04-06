package com.gs.commons.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;

/**
 * 生成各类业务订单ID
 */
public class IdUtils {
    public static String getDepositOrderNo() {
        return "D" + IdUtil.getSnowflakeNextIdStr();
    }

    public static String getWithdrawOrderNo() {
        return "W" + IdUtil.getSnowflakeNextIdStr();
    }

    public static String getPlatInOrderNo() {
        return "IN" + IdUtil.getSnowflakeNextIdStr();
    }

    public static String getPlatOutOrderNo() {
        return "OUT" + IdUtil.getSnowflakeNextIdStr();
    }

    public static String getTransactionOrderNo() {
        return "T" + IdUtil.getSnowflakeNextIdStr();
    }
}
