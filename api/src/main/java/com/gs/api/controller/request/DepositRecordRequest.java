package com.gs.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "帐变记录列表请求", description = "帐变记录列表请求")
public class DepositRecordRequest extends PageBaseRequest{

}
