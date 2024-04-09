package com.gs.api.controller.request;

import com.gs.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "获取三方支付请求类", description = "获取三方支付请求类")
public class PayUrlRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "通道ID", example = "1", required = true)
    private String channelId;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "金额", example = "100", required = true)
    private String amount;

}
