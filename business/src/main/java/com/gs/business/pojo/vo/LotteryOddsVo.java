package com.gs.business.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LotteryOddsVo {

    /**
     * 玩法代码
     */
    private String name;

    /**
     * 玩法名称
     */
    private BigDecimal odds;

    /**
     * 玩法提示
     */
    private String code;

    /**
     * 中奖说明
     */
    private String g;

    /**
     * 范例
     */
    private Integer id;
}
