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
 * @TableName t_plat_record_control
 */
@TableName(value ="t_plat_record_control")
@Data
public class PlatRecordControl implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 平台代码
     */
    @TableField(value = "plat_code")
    private String platCode;

    /**
     * 平台名称
     */
    @TableField(value = "plat_name")
    private String platName;

    /**
     * 开始时间
     */
    @TableField(value = "begin_time")
    private Date beginTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 0:启用 1:停用
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}