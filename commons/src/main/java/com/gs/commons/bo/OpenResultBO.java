package com.gs.commons.bo;


import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class OpenResultBO {

    /**
     * 期号
     */
    private String qs;


    /**
     * 开奖号码
     */
    private String openResult;

    /**
     * 0:已开奖 1:未开奖
     */
    private Integer openStatus;



    /**
     * 开奖时间
     */
    private Date openResultTime;

    private Integer currCount;
}
