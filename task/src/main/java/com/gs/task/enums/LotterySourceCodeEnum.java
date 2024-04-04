package com.gs.task.enums;

import com.google.common.collect.Maps;
import com.gs.commons.enums.LotteryCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum LotterySourceCodeEnum {

    DUOCAI_JSK3(LotterySourceEnum.DUOCAI.getCode(), LotteryCodeEnum.JSK3.getLotteryCode(), "JSKS"),
    DUOCAI_CWSSC(LotterySourceEnum.DUOCAI.getCode(), LotteryCodeEnum.CQSSC.getLotteryCode(), "JDCQSSC"),
    DUOCAI_FT(LotterySourceEnum.DUOCAI.getCode(), LotteryCodeEnum.FT.getLotteryCode(), "XIYFT"),
    ;

    private static Map<String, LotterySourceCodeEnum> lotterySourceCodeEnumMap;

    static {
        init();
    }

    private static void init() {

        lotterySourceCodeEnumMap = Maps.newHashMap();
        for (LotterySourceCodeEnum lotterySourceCodeEnum : values()) {
            lotterySourceCodeEnumMap.put(lotterySourceCodeEnum.getLotterySourceCode() + "_" + lotterySourceCodeEnum.getLotteryCode(), lotterySourceCodeEnum);
        }
    }

    public static LotterySourceCodeEnum getLotterySourceCode(String merchantCode, String lotterycode) {
        return lotterySourceCodeEnumMap.get(merchantCode + "_" + lotterycode);
    }


    private String lotterySourceCode;
    private String lotteryCode;
    private String lotterySourceLotteryCode;
}
