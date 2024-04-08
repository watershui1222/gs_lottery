package com.gs.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum PlatGameTypeEnum {

    LIVE(1, "真人"),
    SLOT(2, "电子"),
    FISH(3, "捕鱼"),

    ;

    private Integer gameType;
    private String typeName;
}
