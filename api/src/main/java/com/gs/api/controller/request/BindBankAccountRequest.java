package com.gs.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "绑定收款账户请求类", description = "绑定收款账户请求类")
public class BindBankAccountRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "银行名称", example = "银行名称", required = true)
    private String bankName;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "开户网点", example = "开户网点", required = true)
    private String address;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "银行卡号", example = "银行卡号", required = true)
    private String bankNo;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "持卡人", example = "资金密码", required = true)
    private String payeeName;
}
