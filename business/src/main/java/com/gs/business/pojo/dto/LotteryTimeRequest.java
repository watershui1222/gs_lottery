package com.gs.business.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "彩票倒计时", description = "彩票倒计时")
public class LotteryTimeRequest {

    @NotNull(message = "lottery.lotteryCode.notempty")
    @ApiModelProperty(value = "彩种代码", example = "", required = false)
    private String lotteryCode;
}
