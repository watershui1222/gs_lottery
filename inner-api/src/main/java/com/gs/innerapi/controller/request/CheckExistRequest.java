package com.gs.innerapi.controller.request;

import com.gs.commons.constants.RegConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "登录请求类", description = "登录请求参数")
public class CheckExistRequest {

    @NotNull(message = "validation.user.register.username")
    @Pattern(regexp = RegConstant.USER_NAME_REG, message = "validation.user.register.username")
    @ApiModelProperty(value = "用户名", example = "dunaifen123", required = true)
    private String userName;
}
