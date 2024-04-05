package com.gs.api.controller.request;

import com.gs.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "绑定微信、支付宝、虚拟货币账户", description = "绑定微信、支付宝、虚拟货币账户")
public class WithdrawRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "金额", example = "100", required = true)
    private String amount;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "账户ID", example = "1", required = true)
    private String accountId;

    @NotNull(message = "validation.user.register.paypwd")
    @Pattern(regexp = RegConstant.WITHDRAW_PWD_REG, message = "validation.user.register.paypwd")
    @ApiModelProperty(value = "提现密码", example = "123456", required = true)
    private String payPwd;
}
