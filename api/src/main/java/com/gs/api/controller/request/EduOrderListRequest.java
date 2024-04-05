package com.gs.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "额度转换记录", description = "额度转换记录")
public class EduOrderListRequest extends PageBaseRequest{

    @ApiModelProperty(value = "平台代码", example = "", required = false)
    private String platCode;

    @ApiModelProperty(value = "时间", example = "1:今天 2:昨天 3:一周内 4:一月内", required = false)
    private String dateStr;
}
