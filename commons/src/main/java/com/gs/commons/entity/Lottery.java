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
 * @TableName t_lottery
 */
@TableName(value ="t_lottery")
@Data
public class Lottery implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 彩种代码
     */
    @TableField(value = "lottery_code")
    private String lotteryCode;

    /**
     * 彩种名称
     */
    @TableField(value = "lottery_name")
    private String lotteryName;

    /**
     * 9:00-23:50(全天开奖不用填)
     */
    @TableField(value = "day_open")
    private String dayOpen;

    /**
     * 0:启用 1:停用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 彩种图标
     */
    @TableField(value = "img")
    private String img;

    /**
     * 提前封盘时间
     */
    @TableField(value = "close_time")
    private Integer closeTime;

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
     * 排序号(值越大越靠前)
     */
    @TableField(value = "pxh")
    private Integer pxh;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}