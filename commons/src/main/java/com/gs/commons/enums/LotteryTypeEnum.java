package com.gs.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum LotteryTypeEnum {


    JSK3(1, "江苏快三"),
    ;
    private Integer lotteryType;
    private String name;


}
