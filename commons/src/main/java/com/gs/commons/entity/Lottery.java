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

    /**
     * 1:快3 2:时时彩 3:PK10 4:六合彩 5:PC蛋蛋 6:11选5 7:福彩3D 8:快乐8
     */
    @TableField(value = "lottery_type")
    private Integer lotteryType;

    /**
     * 一天多少期
     */
    @TableField(value = "day_count")
    private Integer dayCount;

    /**
     * 第一期开奖时间
     */
    @TableField(value = "first_qs_time")
    private String firstQsTime;

    /**
     * 多少分钟一期
     */
    @TableField(value = "qs_time")
    private Integer qsTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}