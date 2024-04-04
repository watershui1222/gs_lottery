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

    DUOCAI_JSK3(LotterySourceEnum.DUOCAI, LotteryCodeEnum.JSK3.getLotteryCode(), "JSKS"),
    ;

//    private static Map<String, LotteryEnum> lotteryCodeMap;
//
//    static {
//        init();
//    }
//
//    private static void init() {
//
//        lotteryCodeMap = Maps.newHashMap();
//        for (LotteryEnum lotteryEnum : values()) {
//            lotteryCodeMap.put(lotteryEnum.getLotteryCode(), lotteryEnum);
//        }
//    }


    private LotterySourceEnum lotterySourceEnum;
    private String lotteryCode;
    private String lotterySourceLotteryCode;
}
