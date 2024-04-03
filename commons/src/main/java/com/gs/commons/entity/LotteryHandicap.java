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
 * @TableName t_lottery_handicap
 */
@TableName(value ="t_lottery_handicap")
@Data
public class LotteryHandicap implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 盘口代码
     */
    @TableField(value = "handicap_code")
    private String handicap_code;

    /**
     * 盘口名称
     */
    @TableField(value = "handicap_name")
    private String handicap_name;

    /**
     * 彩种代码
     */
    @TableField(value = "lottery_code")
    private String lottery_code;

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

    /**
     * 排序号(值越大越靠前)
     */
    @TableField(value = "pxh")
    private Integer pxh;

    /**
     * 0:启用 1:停用
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}