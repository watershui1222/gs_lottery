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
 * @TableName t_transaction_record
 */
@TableName(value ="t_transaction_record")
@Data
public class TransactionRecord implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String user_name;

    /**
     * 金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 操作前余额
     */
    @TableField(value = "before_amount")
    private BigDecimal before_amount;

    /**
     * 操作后余额
     */
    @TableField(value = "after_amount")
    private BigDecimal after_amount;

    /**
     * 0:收入 1:支出
     */
    @TableField(value = "pay_type")
    private Integer pay_type;

    /**
     * 0:充值 1:提现 2:下注 3:彩票奖金 4:彩票撤单 5:三方上分 6:三方下分 7:返水 8:优惠活动 9:后台入款 10:后台扣款
     */
    @TableField(value = "business_type")
    private Integer business_type;

    /**
     * 业务订单号
     */
    @TableField(value = "business_order")
    private String business_order;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date create_time;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 操作人(前台业务可以不写)
     */
    @TableField(value = "oper_name")
    private String oper_name;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}