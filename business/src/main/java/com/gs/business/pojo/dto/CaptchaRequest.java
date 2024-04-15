package com.gs.business.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "获取验证码", description = "获取验证码")
public class CaptchaRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "业务类型", example = "1:注册 2:登录")
    protected String type;
}
