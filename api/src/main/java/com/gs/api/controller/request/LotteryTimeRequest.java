package com.gs.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "彩票倒计时", description = "彩票倒计时")
public class LotteryTimeRequest extends PageBaseRequest {

    @ApiModelProperty(value = "彩种代码", example = "", required = false)
    private String lotteryCode;
}
