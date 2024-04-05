package com.gs.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "历史开奖结果请求", description = "历史开奖结果请求")
public class OpenResultHistoryRequest extends PageBaseRequest{

    @ApiModelProperty(value = "彩种代码", required = true)
    private String lotteryCode;

}
