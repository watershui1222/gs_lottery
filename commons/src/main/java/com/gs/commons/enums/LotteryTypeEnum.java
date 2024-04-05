package com.gs.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum LotteryTypeEnum {


    K3(1, "快三"),
    SSC(2, "时时彩"),
    PK10(3, "PK10"),
    LHC(4, "六合彩"),
    PCDD(5, "PCDD"),
    SSXW(6, "11选5"),
    FC3D(7, "福彩3D"),
    KL8(8, "快乐8"),
    FT(9, "飞艇"),
    KLSF(10, "快乐10分"),
    ;
    private Integer lotteryType;
    private String name;


}
