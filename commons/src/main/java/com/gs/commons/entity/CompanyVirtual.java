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
 * @TableName t_company_virtual
 */
@TableName(value ="t_company_virtual")
@Data
public class CompanyVirtual implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 通道名称
     */
    @TableField(value = "channel_name")
    private String channel_name;

    /**
     * 1:trc20 2:erc20
     */
    @TableField(value = "channel_type")
    private Integer channel_type;

    /**
     * 收款账户
     */
    @TableField(value = "account_no")
    private String account_no;

    /**
     * 汇率
     */
    @TableField(value = "exchange_rate")
    private BigDecimal exchange_rate;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date create_time;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date update_time;

    /**
     * 操作人
     */
    @TableField(value = "oper_name")
    private String oper_name;

    /**
     * 排序号(值越大越靠前)
     */
    @TableField(value = "pxh")
    private Integer pxh;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}