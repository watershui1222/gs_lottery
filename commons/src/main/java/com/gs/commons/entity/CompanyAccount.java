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
 * @TableName t_company_account
 */
@TableName(value ="t_company_account")
@Data
public class CompanyAccount implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账户名称
     */
    @TableField(value = "account_name")
    private String accountName;

    /**
     * 收款人
     */
    @TableField(value = "payee_name")
    private String payeeName;

    /**
     * 收款账号
     */
    @TableField(value = "account_no")
    private String accountNo;

    /**
     * 开户网点
     */
    @TableField(value = "address")
    private String address;

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
     * 1:银行卡 2:微信 3:支付宝
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 0:启用 1:停用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 值越大越靠前
     */
    @TableField(value = "pxh")
    private Integer pxh;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}