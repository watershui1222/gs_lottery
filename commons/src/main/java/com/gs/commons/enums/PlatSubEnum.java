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
    // gameType（1-真人 2-电子 3-捕鱼 4-体育 5-棋牌）
    AGLIVE("AGLIVE", PlatEnum.AG.getPlatCode(),"AG真人", 1),
    AGELE("AGELE", PlatEnum.AG.getPlatCode(),"AG电子", 2),
    AGFISH("AGFISH", PlatEnum.AG.getPlatCode(),"AG捕鱼", 3),
    BBINFISH("BBINFISH", PlatEnum.BBIN.getPlatCode(),"BBIN捕鱼", 3),
    BBINELE("BBINELE", PlatEnum.BBIN.getPlatCode(),"BBIN电子", 2),
    BBINLIVE("BBINLIVE", PlatEnum.BBIN.getPlatCode(),"BBIN真人", 1),
    SB("SB", PlatEnum.SB.getPlatCode(),"沙巴体育", 4),
    HG("HG", PlatEnum.HG.getPlatCode(),"皇冠体育", 4),
    LY("LY", PlatEnum.LY.getPlatCode(),"乐游棋牌", 5),
    KY("KY", PlatEnum.KY.getPlatCode(),"开元棋牌", 5),
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
    private Integer gameType;
    }
