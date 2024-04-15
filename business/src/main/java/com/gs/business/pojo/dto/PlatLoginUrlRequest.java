package com.gs.business.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "平台登录请求类", description = "平台登录请求类")
public class PlatLoginUrlRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "平台代码", example = "AG", required = true)
    private String platCode;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "子平台代码", example = "AGELE", required = true)
    private String platSubCode;

    @ApiModelProperty(value = "游戏代码", example = "5903")
    private String gameCode;

}
