package com.gs.innerapi.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "平台转入请求类", description = "平台转入请求类")
public class PlatDepositRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "平台代码", example = "ag", required = true)
    private String platCode;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "转入金额", example = "100", required = true)
    private String amount;
}
