package com.gs.business.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "第三方游戏记录", description = "第三方游戏记录")
public class PlatOrderListRequest extends PageBaseRequest {

    @ApiModelProperty(value = "子平台代码", example = "", required = true)
    private String subPlatCode;

    @ApiModelProperty(value = "时间", example = "1:今天 2:昨天 3:一周内 4:一月内", required = false)
    private String dateStr;
}
