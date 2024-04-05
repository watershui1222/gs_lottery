package com.gs.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum PlatEnum {
    AG("AG","AG"),
    BBIN("BBIN","BBIN"),
    SB("SB","沙巴体育"),
    HG("HG","皇冠体育"),
    LY("LY","乐游棋牌"),
    KY("KY","开元棋牌"),
    ;

    private static Map<String, PlatEnum> platEnumMap;


    static {
        init();
    }
    private static void init() {
        platEnumMap = new HashMap<>();
        for (PlatEnum platEnum : PlatEnum.values()) {
            platEnumMap.put(platEnum.getPlatCode(), platEnum);
        }
    }

    private String platCode;
    private String platName;
    }
