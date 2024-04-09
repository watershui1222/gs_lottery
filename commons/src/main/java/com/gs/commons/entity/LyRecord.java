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
 * @TableName t_ly_record
 */
@TableName(value ="t_ly_record")
@Data
public class LyRecord implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 平台用户名
     */
    @TableField(value = "plat_user_name")
    private String platUserName;

    /**
     * 游戏局号
     */
    @TableField(value = "order_no")
    private String orderNo;

    /**
     * 游戏ID
     */
    @TableField(value = "game_id")
    private String gameId;

    /**
     * 游戏名称
     */
    @TableField(value = "game_name")
    private String gameName;

    /**
     * 有效投注
     */
    @TableField(value = "effective_bet")
    private BigDecimal effectiveBet;

    /**
     * 总投注额
     */
    @TableField(value = "all_bet")
    private BigDecimal allBet;

    /**
     * 输赢金额(纯盈利,不包含本金)
     */
    @TableField(value = "profit")
    private BigDecimal profit;

    /**
     * 游戏开始时间
     */
    @TableField(value = "game_start_time")
    private Date gameStartTime;

    /**
     * 游戏结束时间
     */
    @TableField(value = "game_end_time")
    private Date gameEndTime;

    /**
     * 结算时间
     */
    @TableField(value = "settle_time")
    private Date settleTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}