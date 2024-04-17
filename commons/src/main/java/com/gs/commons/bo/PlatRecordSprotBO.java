package com.gs.commons.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName 体育注单内容
 */
@Data
public class PlatRecordSprotBO {

    private String wf;

    /**
     * 开奖结果
     */
//    private String openResult;

    /**
     * 下注内容
     */
    private String orderContent;

    private String gameName;


    /**
     * 主队
     */
    private String tnameHome;


    /**
     * 客队
     */
    private String tnameAway;


    /**
     * 联赛名
     */
    private String league;


    /**
     * 赔率
     */
    private BigDecimal Odds;

    /**
     * 下注时比分
     */
    private String score;

    /**
     * 比分
     */
    private String resultScore;

    private String oddsFormat;

    private String strong;

    private String resultStatus;

}