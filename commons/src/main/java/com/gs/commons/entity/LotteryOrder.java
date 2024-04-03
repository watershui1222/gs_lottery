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
 * @TableName t_lottery_order
 */
@TableName(value ="t_lottery_order")
@Data
public class LotteryOrder implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String user_name;

    /**
     * 订单号
     */
    @TableField(value = "order_no")
    private String order_no;

    /**
     * 彩种代码
     */
    @TableField(value = "lottery_code")
    private String lottery_code;

    /**
     * 彩种名称
     */
    @TableField(value = "lottery_name")
    private String lottery_name;

    /**
     * 盘口代码
     */
    @TableField(value = "handicap_code")
    private String handicap_code;

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
     * 投注内容
     */
    @TableField(value = "bet_content")
    private String bet_content;

    /**
     * 赔率
     */
    @TableField(value = "odds")
    private BigDecimal odds;

    /**
     * 期号
     */
    @TableField(value = "qs")
    private String qs;

    /**
     * 投注金额
     */
    @TableField(value = "bet_amount")
    private BigDecimal bet_amount;

    /**
     * 中奖金额(未中奖为0)
     */
    @TableField(value = "bonus_amount")
    private BigDecimal bonus_amount;

    /**
     * 盈利金额
     */
    @TableField(value = "profit_amount")
    private BigDecimal profit_amount;

    /**
     * 投注时间
     */
    @TableField(value = "bet_time")
    private Date bet_time;

    /**
     * 结算时间
     */
    @TableField(value = "settle_time")
    private Date settle_time;

    /**
     * 0:待结算 1:结算中 2:已结算 3:已撤单 4:结算异常
     */
    @TableField(value = "settle_status")
    private Integer settle_status;

    /**
     * 0:待结算 1:已中奖 2:未中奖 3:已撤单
     */
    @TableField(value = "order_status")
    private Integer order_status;

    /**
     * 开奖结果
     */
    @TableField(value = "open_result")
    private String open_result;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}