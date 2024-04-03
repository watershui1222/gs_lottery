package com.gs.api.controller.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class LotteryPlayVo {

    /**
     * 玩法代码
     */
    private String playCode;

    /**
     * 玩法名称
     */
    private String playName;

    /**
     * 玩法提示
     */
    private String wfts;

    /**
     * 中奖说明
     */
    private String zjsm;

    /**
     * 范例
     */
    private String fl;
}
