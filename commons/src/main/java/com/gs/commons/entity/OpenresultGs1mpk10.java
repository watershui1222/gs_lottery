package com.gs.commons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_openresult_gs1mpk10
 */
@TableName(value ="t_openresult_gs1mpk10")
@Data
public class OpenresultGs1mpk10 implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 期号
     */
    @TableField(value = "qs")
    private String qs;

    /**
     * 三方期号
     */
    @TableField(value = "plat_qs")
    private String platQs;

    /**
     * 开奖号码
     */
    @TableField(value = "open_result")
    private String openResult;

    /**
     * 0:已开奖 1:未开奖
     */
    @TableField(value = "open_status")
    private Integer openStatus;

    /**
     * 当前第N期
     */
    @TableField(value = "curr_count")
    private Integer currCount;

    /**
     * 开盘时间
     */
    @TableField(value = "open_time")
    private Date openTime;

    /**
     * 封盘时间
     */
    @TableField(value = "close_time")
    private Date closeTime;

    /**
     * 开奖时间
     */
    @TableField(value = "open_result_time")
    private Date openResultTime;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}