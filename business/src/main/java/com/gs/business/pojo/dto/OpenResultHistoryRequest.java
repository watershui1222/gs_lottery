package com.gs.business.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "历史开奖结果请求", description = "历史开奖结果请求")
public class OpenResultHistoryRequest extends PageBaseRequest {

    @ApiModelProperty(value = "彩种代码", required = true)
    private String lotteryCode;


    @ApiModelProperty(value = "时间", example = "1:今天 2:昨天 3:一周内 4:一月内", required = false)
    private String dateStr;

}
