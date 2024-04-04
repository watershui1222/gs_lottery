package com.gs.task.enums;

import com.gs.commons.enums.LotteryCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum LotterySourceCodeEnum {

    JSK3(LotterySourceEnum.DUOCAI, LotteryCodeEnum.JSK3.getLotteryCode(), "JSKS"),
    ;
    private LotterySourceEnum lotterySourceEnum;
    private String lotteryCode;
    private String lotterySourceLotteryCode;
}
