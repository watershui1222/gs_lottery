package com.gs.business.pojo.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "帐变记录列表请求", description = "帐变记录列表请求")
public class DepositRecordRequest extends PageBaseRequest {

}
