package com.gs.business.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "绑定微信、支付宝、虚拟货币账户", description = "绑定微信、支付宝、虚拟货币账户")
public class DelAccountRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "id", example = "1", required = true)
    private String id;
}
