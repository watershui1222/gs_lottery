package com.gs.business.pojo.vo;

import lombok.Data;

@Data
public class RecommendVo {
    /**
     * 期号
     */
    private String qs;

    private String lotteryName;

    private String lotteryCode;

    private long closeTime;

    private long openResultTime;

    private String lastOpenReuslt;

    private int openResultStatus;

    private int pxh;

    private String img;

    private long serverTime;

    private int lotteryType;
}
