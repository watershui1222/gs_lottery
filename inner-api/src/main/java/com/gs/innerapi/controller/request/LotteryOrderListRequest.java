package com.gs.innerapi.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "彩票注单记录", description = "彩票注单记录")
public class LotteryOrderListRequest extends PageBaseRequest{

    @ApiModelProperty(value = "彩种代码", example = "", required = false)
    private String lotteryCode;

    @ApiModelProperty(value = "时间", example = "1:今天 2:昨天 3:一周内 4:一月内", required = false)
    private String dateStr;

    @ApiModelProperty(value = "状态", example = "", required = false)
    private Integer orderStatus;
}
