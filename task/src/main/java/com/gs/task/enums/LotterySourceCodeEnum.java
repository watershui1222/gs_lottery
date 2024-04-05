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
    DUOCAI_GD11X5(LotterySourceEnum.DUOCAI.getCode(), LotteryCodeEnum.GD11X5.getLotteryCode(), "GD11X5"),
    DUOCAI_BJKL8(LotterySourceEnum.DUOCAI.getCode(), LotteryCodeEnum.BJKL8.getLotteryCode(), "BJKL8"),
    DUOCAI_PCDD(LotterySourceEnum.DUOCAI.getCode(), LotteryCodeEnum.PCDD.getLotteryCode(), "BJKL8"),
    DUOCAI_BJPK10(LotterySourceEnum.DUOCAI.getCode(), LotteryCodeEnum.BJPK10.getLotteryCode(), "BJPK10"),
    DUOCAI_MOLHC(LotterySourceEnum.DUOCAI.getCode(), LotteryCodeEnum.MO6HC.getLotteryCode(), "MOLHC"),
    DUOCAI_FC3D(LotterySourceEnum.DUOCAI.getCode(), LotteryCodeEnum.FC3D.getLotteryCode(), "FC3D"),
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
