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
 * @TableName t_platform
 */
@TableName(value ="t_platform")
@Data
public class Platform implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 平台大类
     */
    @TableField(value = "plat_code")
    private String platCode;

    /**
     * 平台子类
     */
    @TableField(value = "sub_plat_code")
    private String subPlatCode;

    /**
     * 平台名称
     */
    @TableField(value = "plat_name")
    private String platName;

    /**
     * 返水比例
     */
    @TableField(value = "ratio")
    private BigDecimal ratio;

    /**
     * 0:启用 1:停用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 0:正常 1:维护
     */
    @TableField(value = "maintenance_status")
    private Integer maintenanceStatus;

    /**
     * 维护信息
     */
    @TableField(value = "maintenance_msg")
    private String maintenanceMsg;

    /**
     * 图标
     */
    @TableField(value = "img1")
    private String img1;

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
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 排序(值越大越靠前)
     */
    @TableField(value = "pxh")
    private Integer pxh;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}