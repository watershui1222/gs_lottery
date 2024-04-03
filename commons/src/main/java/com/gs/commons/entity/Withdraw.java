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
 * @TableName t_withdraw
 */
@TableName(value ="t_withdraw")
@Data
public class Withdraw implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String user_name;

    /**
     * 订单号
     */
    @TableField(value = "order_no")
    private String order_no;

    /**
     * 金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 申请时间
     */
    @TableField(value = "create_time")
    private Date create_time;

    /**
     * 审核时间
     */
    @TableField(value = "check_time")
    private Date check_time;

    /**
     * 0:待审核 1:审核通过 2:拒绝提现
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 操作人
     */
    @TableField(value = "oper_name")
    private String oper_name;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date update_time;

    /**
     * 1:银行卡 2:微信 3:支付宝 4:虚拟货币
     */
    @TableField(value = "account_type")
    private Integer account_type;

    /**
     * 通道名称
     */
    @TableField(value = "channel_name")
    private String channel_name;

    /**
     * 开户网点
     */
    @TableField(value = "address")
    private String address;

    /**
     * 持卡人姓名
     */
    @TableField(value = "real_name")
    private String real_name;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}