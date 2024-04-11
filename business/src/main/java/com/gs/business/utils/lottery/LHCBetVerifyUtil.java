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
public class LHCBetVerifyUtil {

    public static void verify(JSONObject betContentObj, LotteryOdds lotteryOdds) {
        String playCode = betContentObj.getString("playCode");
        String hm = betContentObj.getString("hm");
        String[] split = hm.split(",");
        Set<String> set = Arrays.stream(split)
                .collect(Collectors.toSet());
        if (StringUtils.equals(playCode, "qbz_qbz")) {
            if (StringUtils.equals(lotteryOdds.getHmCode(), "qbz_5")) {
                if (split.length != 5 || set.size() != 5) {
                    throw new BusinessException("非法参数");
                }
            } else if (StringUtils.equals(lotteryOdds.getHmCode(), "qbz_6")) {
                if (split.length != 6 || set.size() != 6) {
                    throw new BusinessException("非法参数");
                }
            } else if (StringUtils.equals(lotteryOdds.getHmCode(), "qbz_7")) {
                if (split.length != 7 || set.size() != 7) {
                    throw new BusinessException("非法参数");
                }
            } else if (StringUtils.equals(lotteryOdds.getHmCode(), "qbz_8")) {
                if (split.length != 8 || set.size() != 8) {
                    throw new BusinessException("非法参数");
                }
            } else if (StringUtils.equals(lotteryOdds.getHmCode(), "qbz_9")) {
                if (split.length != 9 || set.size() != 9) {
                    throw new BusinessException("非法参数");
                }
            } else if (StringUtils.equals(lotteryOdds.getHmCode(), "qbz_10")) {
                if (split.length != 10 || set.size() != 10) {
                    throw new BusinessException("非法参数");
                }
            } else if (StringUtils.equals(lotteryOdds.getHmCode(), "qbz_11")) {
                if (split.length != 11 || set.size() != 11) {
                    throw new BusinessException("非法参数");
                }
            }
        } else if (StringUtils.equals(playCode, "hx_hx")) {
            if (StringUtils.equals(lotteryOdds.getHmCode(), "hx_2x")) {
                if (split.length != 2 || set.size() != 2) {
                    throw new BusinessException("非法参数");
                }
            } else if (StringUtils.equals(lotteryOdds.getHmCode(), "hx_3x")) {
                if (split.length != 3 || set.size() != 3) {
                    throw new BusinessException("非法参数");
                }
            } else if (StringUtils.equals(lotteryOdds.getHmCode(), "hx_4x")) {
                if (split.length != 4 || set.size() != 4) {
                    throw new BusinessException("非法参数");
                }
            } else if (StringUtils.equals(lotteryOdds.getHmCode(), "hx_5x")) {
                if (split.length != 5 || set.size() != 5) {
                    throw new BusinessException("非法参数");
                }
            } else if (StringUtils.equals(lotteryOdds.getHmCode(), "hx_6x")) {
                if (split.length != 6 || set.size() != 6) {
                    throw new BusinessException("非法参数");
                }
            } else if (StringUtils.equals(lotteryOdds.getHmCode(), "hx_7x")) {
                if (split.length != 7 || set.size() != 7) {
                    throw new BusinessException("非法参数");
                }
            } else if (StringUtils.equals(lotteryOdds.getHmCode(), "hx_8x")) {
                if (split.length != 8 || set.size() != 8) {
                    throw new BusinessException("非法参数");
                }
            } else if (StringUtils.equals(lotteryOdds.getHmCode(), "hx_9x")) {
                if (split.length != 9 || set.size() != 9) {
                    throw new BusinessException("非法参数");
                }
            } else if (StringUtils.equals(lotteryOdds.getHmCode(), "hx_10x")) {
                if (split.length != 10 || set.size() != 10) {
                    throw new BusinessException("非法参数");
                }
            } else if (StringUtils.equals(lotteryOdds.getHmCode(), "hx_11x")) {
                if (split.length != 11 || set.size() != 11) {
                    throw new BusinessException("非法参数");
                }
            }
        } else if (StringUtils.equals(playCode, "lianma_all2")) {
            if (split.length != 2 || set.size() != 2) {
                throw new BusinessException("非法参数");
            }
        } else if (StringUtils.equals(playCode, "lianma_all3")) {
            if (split.length != 3 || set.size() != 3) {
                throw new BusinessException("非法参数");
            }
        } else if (StringUtils.equals(playCode, "lianma_all4")) {
            if (split.length != 4 || set.size() != 4) {
                throw new BusinessException("非法参数");
            }
        } else if (StringUtils.equals(playCode, "lianma_tc")) {
            if (split.length != 2 || set.size() != 2) {
                throw new BusinessException("非法参数");
            }
        }
    }
}
