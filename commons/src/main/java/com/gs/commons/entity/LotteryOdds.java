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
 * @TableName t_lottery_odds
 */
@TableName(value ="t_lottery_odds")
@Data
public class LotteryOdds implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 号码代码
     */
    @TableField(value = "hm_code")
    private String hmCode;

    /**
     * 号码名称
     */
    @TableField(value = "hm_name")
    private String hmName;

    /**
     * 赔率
     */
    @TableField(value = "odds")
    private BigDecimal odds;

    /**
     * 0:启用 1:停用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 彩种代码
     */
    @TableField(value = "lottery_code")
    private String lotteryCode;

    /**
     * 盘口代码
     */
    @TableField(value = "handicap_code")
    private String handicapCode;

    /**
     * 盘口名称
     */
    @TableField(value = "handicap_name")
    private String handicapName;

    /**
     * 玩法代码
     */
    @TableField(value = "play_code")
    private String playCode;

    /**
     * 玩法名称
     */
    @TableField(value = "play_name")
    private String playName;

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
     * 排序号
     */
    @TableField(value = "pxh")
    private Integer pxh;

    /**
     * 号码分组
     */
    @TableField(value = "group_name")
    private String groupName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}