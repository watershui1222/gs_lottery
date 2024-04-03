package com.gs.task.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum LotteryKindEnum {


    JSK3("JSK3", "江苏快三", "08:29:50", 41, 1200),
    ;
    private String lotteryCode;
    private String name;
    private String startTime;
    private int count;
    private int rate;
}
