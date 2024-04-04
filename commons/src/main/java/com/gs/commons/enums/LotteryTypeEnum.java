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
    FT(9, "飞艇"),
    SSX5(6, "11选5"),
    ;
    private Integer lotteryType;
    private String name;


}
