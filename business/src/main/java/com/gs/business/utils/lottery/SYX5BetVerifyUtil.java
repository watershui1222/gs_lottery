package com.gs.business.utils.lottery;

import com.alibaba.fastjson2.JSONObject;
import com.gs.commons.entity.LotteryOdds;
import com.gs.commons.excption.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
public class SYX5BetVerifyUtil {

    public static void verify(JSONObject betContentObj, LotteryOdds lotteryOdds) {
        String playCode = betContentObj.getString("playCode");
        String hm = betContentObj.getString("hm");
        String[] split = hm.split(",");

        Set<String> set = Arrays.stream(split).collect(Collectors.toSet());
        if (StringUtils.equals(playCode, "lm_2z2")) {
            if (split.length != 2 || set.size() != 2) {
                throw new BusinessException("非法参数");
            }
        } else if (StringUtils.equals(playCode, "lm_3z3")) {
            if (split.length != 3 || set.size() != 3) {
                throw new BusinessException("非法参数");
            }
        } else if (StringUtils.equals(playCode, "lm_4z4")) {
            if (split.length != 4 || set.size() != 4) {
                throw new BusinessException("非法参数");
            }
        } else if (StringUtils.equals(playCode, "lm_5z5")) {
            if (split.length != 5 || set.size() != 5) {
                throw new BusinessException("非法参数");
            }
        } else if (StringUtils.equals(playCode, "lm_6z5")) {
            if (split.length != 6 || set.size() != 6) {
                throw new BusinessException("非法参数");
            }
        } else if (StringUtils.equals(playCode, "lm_7z5")) {
            if (split.length != 7 || set.size() != 7) {
                throw new BusinessException("非法参数");
            }
        } else if (StringUtils.equals(playCode, "lm_8z5")) {
            if (split.length != 8 || set.size() != 8) {
                throw new BusinessException("非法参数");
            }
        }
    }
}
