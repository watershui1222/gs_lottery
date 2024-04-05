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
 * 皇冠体育注单表
 * @TableName t_hg_record
 */
@TableName(value ="t_hg_record")
@Data
public class HgRecord implements Serializable {
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
     * 平台用户名(mid)
     */
    @TableField(value = "plat_user_name")
    private String platUserName;

    /**
     * 游戏局号(id)
     */
    @TableField(value = "order_no")
    private String orderNo;

    /**
     * FT:足球 BK:篮球/美足 TB:网球 BS:棒球 OP:其他 VF:虚拟足球 SK:台球 MT:跨球类过关 FS:冠军 VB:排球
     */
    @TableField(value = "game_name")
    private String gameName;

    /**
     * 玩法
     */
    @TableField(value = "wtype")
    private String wtype;

    /**
     * 玩法
     */
    @TableField(value = "rtype")
    private String rtype;

    /**
     * 主队名称
     */
    @TableField(value = "tname_home")
    private String tnameHome;

    /**
     * 客队名称
     */
    @TableField(value = "tname_away")
    private String tnameAway;

    /**
     * 联赛名称
     */
    @TableField(value = "league")
    private String league;

    /**
     * H:主队为强队 C:客队为强队 ''无
     */
    @TableField(value = "strong")
    private String strong;

    /**
     * 赔率
     */
    @TableField(value = "ioratio")
    private BigDecimal ioratio;

    /**
     * 有效投注
     */
    @TableField(value = "effective_bet")
    private BigDecimal effectiveBet;

    /**
     * 总投注额gold
     */
    @TableField(value = "all_bet")
    private BigDecimal allBet;

    /**
     * 输赢金额(纯盈利,不包含本金)
     */
    @TableField(value = "profit")
    private BigDecimal profit;

    /**
     * 下注时间
     */
    @TableField(value = "bet_time")
    private Date betTime;

    /**
     * 结算时间
     */
    @TableField(value = "settle_time")
    private Date settleTime;

    /**
     * 0:未结算 1:已结算
     */
    @TableField(value = "settle_status")
    private Integer settleStatus;

    /**
     * 0:无结果 L:输 W:赢 P:合 D:取消 A:还原
     */
    @TableField(value = "result_status")
    private String resultStatus;

    /**
     * 比分结果
     */
    @TableField(value = "result_score")
    private String resultScore;

    /**
     * 球头
     */
    @TableField(value = "odds_format")
    private String oddsFormat;

    /**
     * 2:2串1 3:3串1 4:4串1
     */
    @TableField(value = "parlaynum")
    private Integer parlaynum;

    /**
     * 串数内容 一般是json
     */
    @TableField(value = "parlaysub")
    private String parlaysub;

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