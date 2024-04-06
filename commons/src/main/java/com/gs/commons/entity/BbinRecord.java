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
 * BBIN注单表
 * @TableName t_bbin_record
 */
@TableName(value ="t_bbin_record")
@Data
public class BbinRecord implements Serializable {
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
     * 注单号码WagersID
     */
    @TableField(value = "order_no")
    private String orderNo;

    /**
     * 游戏ID  GameType
     */
    @TableField(value = "game_id")
    private String gameId;

    /**
     * 游戏名称GameType
     */
    @TableField(value = "game_name")
    private String gameName;

    /**
     * 有效投注Commissionable
     */
    @TableField(value = "effective_bet")
    private BigDecimal effectiveBet;

    /**
     * 总投注额BetAmount
     */
    @TableField(value = "all_bet")
    private BigDecimal allBet;

    /**
     * Payoff派彩金额(不包含本金)
     */
    @TableField(value = "profit")
    private BigDecimal profit;

    /**
     * 下注时间WagersDate
     */
    @TableField(value = "bet_time")
    private Date betTime;

    /**
     * 下注时间美东
     */
    @TableField(value = "wagers_date")
    private Date wagersDate;

    /**
     * 结算时间ModifiedDate
     */
    @TableField(value = "settle_time")
    private Date settleTime;

    /**
     * 注单变更时间 美东
     */
    @TableField(value = "modified_date")
    private Date modifiedDate;

    /**
     * 注单结果(C:注销,W:赢,L:输)
     */
    @TableField(value = "result_status")
    private String resultStatus;

    /**
     * 开牌结果
     */
    @TableField(value = "open_result")
    private String openResult;

    /**
     * 结果牌
     */
    @TableField(value = "card")
    private String card;

    /**
     * 局号
     */
    @TableField(value = "serial_id")
    private String serialId;

    /**
     * 场次
     */
    @TableField(value = "round_no")
    private String roundNo;

    /**
     * 玩法
     */
    @TableField(value = "wager_detail")
    private String wagerDetail;

    /**
     * 1:视讯 2:电子 3:捕鱼
     */
    @TableField(value = "game_type")
    private Integer gameType;

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