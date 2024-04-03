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
 * @TableName t_sys_param
 */
@TableName(value ="t_sys_param")
@Data
public class SysParam implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 参数KEY
     */
    @TableField(value = "param_key")
    private String param_key;

    /**
     * 参数值
     */
    @TableField(value = "param_value")
    private String param_value;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}