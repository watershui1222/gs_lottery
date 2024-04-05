package com.gs.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "绑定微信、支付宝、虚拟货币账户", description = "绑定微信、支付宝、虚拟货币账户")
public class BindAccountRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "货币名称", example = "USDT", required = true)
    private String coinName;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "提币网络", example = "TRC20,ERC20", required = true)
    private String channelType;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "提币地址", example = "提币地址", required = true)
    private String address;
}
