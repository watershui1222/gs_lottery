package com.gs.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum PlatSubEnum {
    AGLIVE("AGLIVE", PlatEnum.AG.getPlatCode(),"AG真人"),
    AGELE("AGELE", PlatEnum.AG.getPlatCode(),"AG电子"),
    AGFISH("AGFISH", PlatEnum.AG.getPlatCode(),"AG捕鱼"),
    BBINFISH("BBINFISH", PlatEnum.BBIN.getPlatCode(),"BBIN捕鱼"),
    BBINELE("BBINELE", PlatEnum.BBIN.getPlatCode(),"BBIN电子"),
    BBINLIVE("BBINLIVE", PlatEnum.BBIN.getPlatCode(),"BBIN真人"),
    SB("SB", PlatEnum.SB.getPlatCode(),"沙巴体育"),
    HG("HG", PlatEnum.HG.getPlatCode(),"皇冠体育"),
    LY("LY", PlatEnum.LY.getPlatCode(),"乐游棋牌"),
    KY("KY", PlatEnum.KY.getPlatCode(),"开元棋牌"),
    ;


    private static Map<String, PlatSubEnum> platSubEnumMap;

    static {
        init();
    }

    private static void init() {
        platSubEnumMap = new HashMap<>();
        for (PlatSubEnum platSubEnum : PlatSubEnum.values()) {
            platSubEnumMap.put(platSubEnum.getPlatSubCode(), platSubEnum);
        }
    }

    public static PlatSubEnum getByCode(String platSubCode) {
        return platSubEnumMap.get(platSubCode);
    }
    private String platSubCode;
    private String platCode;
    private String platName;
    }
