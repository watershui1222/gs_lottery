package com.gs.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_pay_merchant
 */
@TableName(value ="t_pay_merchant")
@Data
public class PayMerchant implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 商户信息详情
     */
    @TableField(value = "merchant_detail")
    private String merchantDetail;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 最后修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 0:启用 1:停用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 回调地址
     */
    @TableField(value = "callback_url")
    private String callbackUrl;

    /**
     * 回调IP
     */
    @TableField(value = "callback_ip")
    private String callbackIp;

    /**
     * 支付网关地址
     */
    @TableField(value = "pay_url")
    private String payUrl;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}