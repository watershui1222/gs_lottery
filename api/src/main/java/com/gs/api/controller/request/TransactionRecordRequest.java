package com.gs.api.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "帐变记录列表请求", description = "帐变记录列表请求")
public class TransactionRecordRequest extends PageBaseRequest{

    @ApiModelProperty(value = "类型", example = "0:充值 1:提现 2:下注 3:彩票奖金 4:彩票撤单 5:三方上分 6:三方下分 7:返水 8:优惠活动 9:后台入款 10:后台扣款", required = false)
    private String type;

    @ApiModelProperty(value = "时间", example = "1:今天 2:昨天 3:一周内 4:一月内", required = false)
    private String dateStr;
}
