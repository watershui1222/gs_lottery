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
    BJKL8("BJKL8", "北京快乐8"),
    PCDD("PCDD", "PC蛋蛋"),
    BJPK10("BJPK10", "北京PK10"),
    MO6HC("MOLHC", "澳门六合彩"),
    ;
    private String lotteryCode;
    private String name;


}
