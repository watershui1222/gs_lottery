package com.gs.business.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "公司入款充值请求类", description = "公司入款充值请求类")
public class CompanyDepositRequest {

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "通道ID", example = "1", required = true)
    private String channelId;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "存款人姓名", example = "张三", required = true)
    private String name;

    @ApiModelProperty(value = "转账备注", example = "这是我充的", required = true)
    private String remark;

    @NotNull(message = "system.param.err")
    @ApiModelProperty(value = "金额", example = "100", required = true)
    private String amount;
}
