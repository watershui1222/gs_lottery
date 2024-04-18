package com.gs.commons.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 
 * @TableName 通用注单内容
 */
@Data
public class PlatRecordBO {

    private String userName;


    private String orderNo;


    private String gameName;


    private BigDecimal effectiveBet;



    private BigDecimal allBet;


    private BigDecimal profit;



    private Date betTime;

    private Date settleTime;

    /**
     * 0 不是   1:单条注单  2:串关
     */
    private Integer isSport = 0;

    private String resultStatus;

    private PlatRecordSprotBO sportDetail;

    private List<PlatRecordSprotBO> sportDetailList;



}