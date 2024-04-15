package com.gs.business.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "修改用户昵称", description = "修改用户昵称")
public class UpdateNickNameRequest {
    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "用户昵称", example = "张三", required = true)
    private String nickName;
}
