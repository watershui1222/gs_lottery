package com.gs.gamerecord.utils;

import java.util.HashMap;
import java.util.Map;

public class HgConstants {
    public static final Map<String, String> GAME_NAME = new HashMap<>();
    static {
        GAME_NAME.put("FT", "足球");
        GAME_NAME.put("BK", "篮球");
        GAME_NAME.put("TB", "网球");
        GAME_NAME.put("BS", "棒球");
        GAME_NAME.put("OP", "其他");
        GAME_NAME.put("VF", "虚拟足球");
        GAME_NAME.put("SK", "台球");
        GAME_NAME.put("MT", "跨球类过关");
    }
}
