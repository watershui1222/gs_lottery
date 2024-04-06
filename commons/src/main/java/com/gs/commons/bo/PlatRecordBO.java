package com.gs.commons.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName t_ky_record
 */
@Data
public class PlatRecordBO {

    private String userName;


    private String orderNo;


    private String gameName;


    private BigDecimal effectiveBet;



    private BigDecimal allBet;


    private BigDecimal profit;




    private Date gameEndTime;

}