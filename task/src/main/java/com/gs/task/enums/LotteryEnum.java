package com.gs.task.enums;

import com.google.common.collect.Maps;
import com.gs.commons.enums.LotteryCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum LotteryEnum {


    JSK3(LotteryCodeEnum.JSK3.getLotteryCode(), "江苏快三", "08:29:50", 41, 1200),


    ;


    private String lotteryCode;
    private String name;
    private String startTime;
    private int count;
    private int rate;

    private static Map<String, LotteryEnum> lotteryCodeMap;

    static {
        init();
    }

    private static void init() {

        lotteryCodeMap = Maps.newHashMap();
        for (LotteryEnum lotteryEnum : values()) {
            lotteryCodeMap.put(lotteryEnum.getLotteryCode(), lotteryEnum);
        }
    }



}
