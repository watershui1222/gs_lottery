package com.gs.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum PlatSubEnum {
    AGLIVE("AGLIVE", PlatEnum.AG.getPlatCode(),"ag真人"),
    AGELE("AGELE", PlatEnum.AG.getPlatCode(),"ag电子"),
    AGFISH("AGFISH", PlatEnum.AG.getPlatCode(),"ag捕鱼"),
    BBINFISH("BBINFISH", PlatEnum.BBIN.getPlatCode(),"bbin捕鱼"),
    BBINELE("BBINELE", PlatEnum.BBIN.getPlatCode(),"bbin电子"),
    BBINLIVE("BBINLIVE", PlatEnum.BBIN.getPlatCode(),"bbin真人"),
    SB("SB", PlatEnum.SB.getPlatCode(),"沙巴体育"),
    HG("HG", PlatEnum.HG.getPlatCode(),"皇冠体育"),
    LY("LY", PlatEnum.LY.getPlatCode(),"乐游棋牌"),
    KY("KY", PlatEnum.KY.getPlatCode(),"开元棋牌"),
    ;


    private String platSubCode;
    private String platCode;
    private String platName;
    }
