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
 * @TableName t_edu_order
 */
@TableName(value ="t_edu_order")
@Data
public class EduOrder implements Serializable {
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
     * 订单号(自己平台)
     */
    @TableField(value = "order_no")
    private String order_no;

    /**
     * 三方订单号
     */
    @TableField(value = "plat_order_no")
    private String plat_order_no;

    /**
     * 转换金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 0:转入 1:转出
     */
    @TableField(value = "edu_type")
    private Integer edu_type;

    /**
     * 平台代码
     */
    @TableField(value = "plat_code")
    private String plat_code;

    /**
     * -1:异常 0:成功 1:失败
     */
    @TableField(value = "status")
    private Integer status;

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
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}