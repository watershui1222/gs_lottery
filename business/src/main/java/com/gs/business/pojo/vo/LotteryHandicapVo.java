package com.gs.business.pojo.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LotteryHandicapVo {

    /**
     * 盘口代码
     */
    private String handicapCode;

    /**
     * 盘口名称
     */
    private String handicapName;

    // 所有玩法
    private List<LotteryPlayVo> plays = new ArrayList<>();
}
