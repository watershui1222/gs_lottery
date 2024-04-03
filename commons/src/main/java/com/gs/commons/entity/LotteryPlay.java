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
 * @TableName t_lottery_play
 */
@TableName(value ="t_lottery_play")
@Data
public class LotteryPlay implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 玩法代码
     */
    @TableField(value = "play_code")
    private String play_code;

    /**
     * 玩法名称
     */
    @TableField(value = "play_name")
    private String play_name;

    /**
     * 彩票代码
     */
    @TableField(value = "lottery_code")
    private String lottery_code;

    /**
     * 盘口代码
     */
    @TableField(value = "handicap_code")
    private String handicap_code;

    /**
     * 玩法提示
     */
    @TableField(value = "wfts")
    private String wfts;

    /**
     * 中奖说明
     */
    @TableField(value = "zjsm")
    private String zjsm;

    /**
     * 范例
     */
    @TableField(value = "fl")
    private String fl;

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
     * 排序号
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