package com.gs.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "彩票投注", description = "彩票投注")
public class LotteryBetRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "投注内容", example = "[{\"amount\": 10, \"playCode\": \"hz_hz\", \"hm\": \"5\", \"oddsId\": \"2\"}]", required = true)
    private String betContent;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "期数", example = "240406031", required = true)
    private String qs;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "彩票代码", example = "JSK3", required = true)
    private String lotterCode;
}
