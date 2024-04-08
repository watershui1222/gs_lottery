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
 * 沙巴体育注单表
 * @TableName t_sb_record
 */
@TableName(value ="t_sb_record")
@Data
public class SbRecord implements Serializable {
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
     * 平台用户名(vendor_member_id)
     */
    @TableField(value = "plat_user_name")
    private String platUserName;

    /**
     * 游戏局号(trans_id)
     */
    @TableField(value = "order_no")
    private String orderNo;

    /**
     * sportname
     */
    @TableField(value = "game_name")
    private String gameName;

    /**
     * 玩法bettypename
     */
    @TableField(value = "wtype")
    private String wtype;

    /**
     * 玩法
     */
    @TableField(value = "rtype")
    private String rtype;

    /**
     * 主队名称hometeamname
     */
    @TableField(value = "tname_home")
    private String tnameHome;

    /**
     * 客队名称awayteamname
     */
    @TableField(value = "tname_away")
    private String tnameAway;

    /**
     * 联赛名称leaguename
     */
    @TableField(value = "league")
    private String league;

    /**
     * H:主队为强队 C:客队为强队 ''无
     */
    @TableField(value = "strong")
    private String strong;

    /**
     * 赔率 odds
     */
    @TableField(value = "ioratio")
    private BigDecimal ioratio;

    /**
     * 有效投注
     */
    @TableField(value = "effective_bet")
    private BigDecimal effectiveBet;

    /**
     * 总投注额stake
     */
    @TableField(value = "all_bet")
    private BigDecimal allBet;

    /**
     * 此注输或赢的金额winlost_amount
     */
    @TableField(value = "profit")
    private BigDecimal profit;

    /**
     * 下注时间transaction_time
     */
    @TableField(value = "bet_time")
    private Date betTime;

    /**
     * 结算时间settlement_time
     */
    @TableField(value = "settle_time")
    private Date settleTime;

    /**
     * 0:未结算 1:已结算
     */
    @TableField(value = "settle_status")
    private Integer settleStatus;

    /**
     * waiting(等待中) running(进行中)void(作废)refund(退款)reject(已取消)lose(输)won(赢)draw(和局)half won(半赢)half lose(半输)
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
     * 串数内容 ParlayData
     */
    @TableField(value = "parlaysub")
    private String parlaysub;

    /**
     * 返回的原始数据 一般是json
     */
    @TableField(value = "raw_data")
    private String rawData;

    /**
     * 重新结算信息
     */
    @TableField(value = "resettlementinfo")
    private String resettlementinfo;

    /**
     * 重新结算时间ActionDate
     */
    @TableField(value = "action_date")
    private Date actionDate;

    /**
     * 赛事开球时间
     */
    @TableField(value = "match_datetime")
    private Date matchDatetime;

    /**
     * 滚球  0:否 1:是
     */
    @TableField(value = "is_live")
    private Integer isLive;

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