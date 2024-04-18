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
    // gameType（1-真人 2-电子 3-捕鱼 4-体育）
    AGLIVE("AGLIVE", PlatEnum.AG.getPlatCode(),"AG视讯", 1),
    AGELE("AGELE", PlatEnum.AG.getPlatCode(),"AG电子", 2),
    AGFISH("AGFISH", PlatEnum.AG.getPlatCode(),"AG捕鱼", 3),
    BBINFISH("BBINFISH", PlatEnum.BBIN.getPlatCode(),"BBIN捕鱼", 3),
    BBINELE("BBINELE", PlatEnum.BBIN.getPlatCode(),"BBIN电子", 2),
    BBINLIVE("BBINLIVE", PlatEnum.BBIN.getPlatCode(),"BBIN视讯", 1),
    SB("SB", PlatEnum.SB.getPlatCode(),"沙巴体育", 4),
    HG("HG", PlatEnum.HG.getPlatCode(),"皇冠体育", 4),
    LY("LY", PlatEnum.LY.getPlatCode(),"乐游棋牌", 2),
    KY("KY", PlatEnum.KY.getPlatCode(),"开元棋牌", 2),
    ;


    private static Map<String, PlatSubEnum> platSubEnumMap;

    static {
        initSubEnumMap();
    }

    private static void initSubEnumMap() {
        platSubEnumMap = new HashMap<>();
        for (PlatSubEnum platSubEnum : PlatSubEnum.values()) {
            platSubEnumMap.put(platSubEnum.getPlatSubCode(), platSubEnum);
        }
    }

    public static PlatSubEnum getBySubCode(String platSubCode) {
        return platSubEnumMap.get(platSubCode);
    }

    private static Map<String, PlatSubEnum> platEnumMap;
    static{
        initPlatEnumMap();
    }
    private static void initPlatEnumMap(){
        platEnumMap = new HashMap<>();
        for (PlatSubEnum platSubEnum : PlatSubEnum.values()) {
            platEnumMap.put(platSubEnum.getPlatCode() + "_" + platSubEnum.getGameType(), platSubEnum);
        }
    }
    public static PlatSubEnum getByPlatCode(String platCode, Integer gameType) {
        return platEnumMap.get(platCode + "_" + gameType);
    }
    private String platSubCode;
    private String platCode;
    private String platName;
    private Integer gameType;
    }
