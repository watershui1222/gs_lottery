package com.gs.commons.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName t_openresult_bjkl8
 */
@Data
public class OpenresultTimeBO {



    private String qs;


    private String platQs;


    private String openResult;


    private Integer openStatus;

    private Integer currCount;


    private Date openTime;


    private Date closeTime;


    private Date openResultTime;



}