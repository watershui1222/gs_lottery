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
    FC3D("FC3D", "福彩3D"),
    GS1MK3("GS1MK3", "一分快三"),
    GS1MPK10("GS1MPK10", "一分赛车"),
    GS1MSSC("GS1MSSC", "一分时时彩"),
    GS1MFT("GS1MFT", "一分飞艇"),
    GS1MLHC("GS1MLHC", "一分六合彩"),
    GS1MPCDD("GS1MPCDD", "一分PC蛋蛋"),
    GS1M11X5("GS1M11X5", "一分11选5"),
    GS1MKL8("GS1MKL8", "一分快乐8"),
    HKLHC("HKLHC", "、香港六合彩"),
    PL3("PL3", "排列3"),
    GS1MPL3("GS1MPL3", "一分排列3"),
    GS1MFC3D("GS1MFC3D", "一分福彩3D"),
    ;
    private String lotteryCode;
    private String name;


}
