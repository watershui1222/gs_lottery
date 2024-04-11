package com.gs.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_pay_channel
 */
@TableName(value ="t_pay_channel")
@Data
public class PayChannel implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 通道代码
     */
    @TableField(value = "channel_code")
    private String channelCode;

    /**
     * 通道名称
     */
    @TableField(value = "channel_name")
    private String channelName;

    /**
     * 商户代码
     */
    @TableField(value = "merchant_code")
    private String merchantCode;

    /**
     * 商户名称
     */
    @TableField(value = "merchant_name")
    private String merchantName;

    /**
     * 0:启用 1:停用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 最小金额
     */
    @TableField(value = "min_amount")
    private BigDecimal minAmount;

    /**
     * 最大金额
     */
    @TableField(value = "max_amount")
    private BigDecimal maxAmount;

    /**
     * 1:钱包 2:微信 3:支付宝 4:QQ 5:数字人民币 6:银联 7:手机银行 8:云闪付
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 排序号
     */
    @TableField(value = "pxh")
    private Integer pxh;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}