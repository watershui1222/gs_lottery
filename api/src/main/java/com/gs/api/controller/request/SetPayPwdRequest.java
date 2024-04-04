package com.gs.api.controller.request;

import com.gs.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "设置资金密码", description = "设置资金密码")
public class SetPayPwdRequest {

    @NotNull(message = "validation.user.register.paypwd")
    @Pattern(regexp = RegConstant.WITHDRAW_PWD_REG, message = "validation.user.register.paypwd")
    @ApiModelProperty(value = "资金密码", example = "资金密码", required = true)
    private String payPwd;
}
