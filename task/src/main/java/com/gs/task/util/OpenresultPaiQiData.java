package com.gs.task.util;

import lombok.Data;

import java.util.Date;

/**
 * 
 * @TableName t_openresult_jsk3
 */
@Data
public class OpenresultPaiQiData {

    /**
     * 期号
     */
    private String qs;

    /**
     * 三方期号
     */
    private String plat_qs;

    /**
     * 开奖号码
     */
    private String open_result;

    /**
     * 0:已开奖 1:未开奖
     */
    private Integer open_status;

    /**
     * 当前第N期
     */
    private Integer curr_count;

    /**
     * 开盘时间
     */
    private Date open_time;

    /**
     * 封盘时间
     */
    private Date close_time;

    /**
     * 开奖时间
     */
    private Date open_result_time;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 修改时间
     */
    private Date update_time;
}