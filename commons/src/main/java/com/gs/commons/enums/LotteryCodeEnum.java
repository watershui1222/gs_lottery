package com.gs.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum LotteryCodeEnum {


    JSK3("JSK3", "江苏快三"),
    CQSSC("CQSSC", "重庆时时彩"),
    FT("FT", "飞艇"),
    GD11X5("GD11X5", "广东11选5"),
    ;
    private String lotteryCode;
    private String name;


}
