package com.gs.business.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class LotteryCurrQsBO {

    /**
     * 期号
     */
    private String qs;

    /**
     * 三方期号
     */
    private String platQs;

    /**
     * 开奖号码
     */
    private String openResult;

    /**
     * 0:已开奖 1:未开奖
     */
    private Integer openStatus;

    /**
     * 当前第N期
     */
    private Integer currCount;

    /**
     * 开盘时间
     */
    private Date openTime;

    /**
     * 封盘时间
     */
    private Date closeTime;

    /**
     * 开奖时间
     */
    private Date openResultTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}
