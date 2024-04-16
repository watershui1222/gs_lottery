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
 * @TableName t_pay_order
 */
@TableName(value ="t_pay_order")
@Data
public class PayOrder implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 订单号
     */
    @TableField(value = "order_no")
    private String orderNo;

    /**
     * 三方订单号
     */
    @TableField(value = "pay_order_no")
    private String payOrderNo;

    /**
     * 金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;

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
     * 0:未支付 1:成功 2:失败
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 错误消息
     */
    @TableField(value = "error_msg")
    private String errorMsg;

    /**
     * 商户代码
     */
    @TableField(value = "merchant_code")
    private String merchantCode;

    /**
     * 通道代码
     */
    @TableField(value = "channel_code")
    private String channelCode;

    /**
     * 商户名称
     */
    @TableField(value = "merchant_name")
    private String merchantName;

    /**
     * 通道名称
     */
    @TableField(value = "channel_name")
    private String channelName;

    /**
     * 签名
     */
    @TableField(value = "sign")
    private String sign;

    /**
     * 用户IP
     */
    @TableField(value = "user_ip")
    private String userIp;

    /**
     * IP详情
     */
    @TableField(value = "ip_detail")
    private String ipDetail;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}