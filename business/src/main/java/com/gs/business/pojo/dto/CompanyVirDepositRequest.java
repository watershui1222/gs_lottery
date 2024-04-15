package com.gs.business.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "虚拟货币充值请求类", description = "虚拟货币充值请求类")
public class CompanyVirDepositRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "通道ID", example = "1", required = true)
    private String channelId;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "交易ID", example = "sdfasdfasfa", required = true)
    private String trxId;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "金额", example = "100", required = true)
    private String amount;
}
