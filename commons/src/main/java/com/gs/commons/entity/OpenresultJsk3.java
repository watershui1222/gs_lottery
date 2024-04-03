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
 * @TableName t_openresult_jsk3
 */
@TableName(value ="t_openresult_jsk3")
@Data
public class OpenresultJsk3 implements Serializable {
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
    private String plat_qs;

    /**
     * 开奖号码
     */
    @TableField(value = "open_result")
    private String open_result;

    /**
     * 0:已开奖 1:未开奖
     */
    @TableField(value = "open_status")
    private Integer open_status;

    /**
     * 当前第N期
     */
    @TableField(value = "curr_count")
    private Integer curr_count;

    /**
     * 开盘时间
     */
    @TableField(value = "open_time")
    private Date open_time;

    /**
     * 封盘时间
     */
    @TableField(value = "close_time")
    private Date close_time;

    /**
     * 开奖时间
     */
    @TableField(value = "open_result_time")
    private Date open_result_time;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date create_time;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date update_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}