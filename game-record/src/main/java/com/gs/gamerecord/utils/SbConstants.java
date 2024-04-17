package com.gs.gamerecord.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class SbConstants {
    public static final Map<String, String> BET_CONTENT = new HashMap<>();

    static {
        BET_CONTENT.put("2_h", "单");
        BET_CONTENT.put("2_c", "双");
        BET_CONTENT.put("3_h", "大");
        BET_CONTENT.put("3_a", "小");
        BET_CONTENT.put("8_h", "大");
        BET_CONTENT.put("8_a", "小");
        BET_CONTENT.put("12_h", "单");
        BET_CONTENT.put("12_a", "双");
        BET_CONTENT.put("13_hy", "主队没失球");
        BET_CONTENT.put("13_hn", "主队有失球");
        BET_CONTENT.put("13_ay", "客队没失球");
        BET_CONTENT.put("13_an", "客队有失球");
        BET_CONTENT.put("18_o", "大");
        BET_CONTENT.put("18_u", "小");
        BET_CONTENT.put("24_1x", "主队赢/和局");
        BET_CONTENT.put("24_12", "主队赢/客队赢");
        BET_CONTENT.put("24_2x", "客队赢/和局");
        BET_CONTENT.put("26_o", "一方得分");
        BET_CONTENT.put("26_n", "都没得分");
        BET_CONTENT.put("26_b", "双方都得分");
        BET_CONTENT.put("85_h", "大");
        BET_CONTENT.put("85_a", "小");
        BET_CONTENT.put("86_h", "单");
        BET_CONTENT.put("86_a", "双");
        BET_CONTENT.put("89_1:1", "大,单");
        BET_CONTENT.put("89_1:2", "大,双");
        BET_CONTENT.put("89_2:1", "小,单");
        BET_CONTENT.put("89_2:2", "小,双");
        BET_CONTENT.put("91_r", "红");
        BET_CONTENT.put("91_b", "蓝");
        BET_CONTENT.put("123_h", "和局");
        BET_CONTENT.put("123_a", "不是和局");
        BET_CONTENT.put("127_1:1", "主队最先进球");
        BET_CONTENT.put("127_1:2", "主队最后进球");
        BET_CONTENT.put("127_2:1", "客队最先进球");
        BET_CONTENT.put("127_2:2", "客队最后进球");
        BET_CONTENT.put("127_0:0", "没有任何进球");
        BET_CONTENT.put("128_oo", "半场单, 全场单");
        BET_CONTENT.put("128_oe", "半场单, 全场双");
        BET_CONTENT.put("128_eo", "半场双, 全场单");
        BET_CONTENT.put("128_ee", "半场双, 全场双");
        BET_CONTENT.put("133_y", "是");
        BET_CONTENT.put("133_n", "否");
        BET_CONTENT.put("438_y", "是");
        BET_CONTENT.put("438_n", "否");
        BET_CONTENT.put("134_y", "是");
        BET_CONTENT.put("134_n", "否");
        BET_CONTENT.put("439_y", "是");
        BET_CONTENT.put("439_n", "否");
        BET_CONTENT.put("135_y", "是");
        BET_CONTENT.put("135_n", "否");
        BET_CONTENT.put("140_1h", "上半场");
        BET_CONTENT.put("140_2n", "下半场");
        BET_CONTENT.put("140_tie", "一样多");
        BET_CONTENT.put("442_1h", "上半场");
        BET_CONTENT.put("442_2n", "下半场");
        BET_CONTENT.put("442_tie", "一样多");
        BET_CONTENT.put("141_1h", "上半场");
        BET_CONTENT.put("141_2n", "下半场");
        BET_CONTENT.put("141_tie", "一样多");
        BET_CONTENT.put("143_1h", "上半场");
        BET_CONTENT.put("143_2n", "下半场");
        BET_CONTENT.put("143_tie", "一样多");
        BET_CONTENT.put("142_1h", "上半场");
        BET_CONTENT.put("142_2n", "下半场");
        BET_CONTENT.put("142_tie", "一样多");
        BET_CONTENT.put("444_1h", "上半场");
        BET_CONTENT.put("444_2n", "下半场");
        BET_CONTENT.put("444_tie", "一样多");
        BET_CONTENT.put("1203_h", "大");
        BET_CONTENT.put("1203_a", "小");
        BET_CONTENT.put("1206_0", "零进球");
        BET_CONTENT.put("1206_1", "进 1 球");
        BET_CONTENT.put("1206_2", "进 2 球");
        BET_CONTENT.put("1206_3", "进 3 球");
        BET_CONTENT.put("1206_4", "进 4 球");
        BET_CONTENT.put("1235_1", "左边球员拿下局数 : 右边球员得 0 分");
        BET_CONTENT.put("1235_2", "左边球员拿下局数 : 右边球员得 15 分");
        BET_CONTENT.put("1235_3", "左边球员拿下局数 : 右边球员得 30 分");
        BET_CONTENT.put("1235_4", "左边球员拿下局数 : 右边球员得 40 分");
        BET_CONTENT.put("1235_5", "左边球员得 0 分 : 右边球员拿下局数");
        BET_CONTENT.put("1235_6", "左边球员得 15 分 : 右边球员拿下局数");
        BET_CONTENT.put("1235_7", "左边球员得 30 分 : 右边球员拿下局数");
        BET_CONTENT.put("1235_8", "左边球员得 40 分 : 右边球员拿下局数");
        BET_CONTENT.put("156_o", "大");
        BET_CONTENT.put("156_u", "小");
        BET_CONTENT.put("1305_h", "单");
        BET_CONTENT.put("1305_a", "双");
        BET_CONTENT.put("1306_o", "大");
        BET_CONTENT.put("1306_u", "小");
        BET_CONTENT.put("1312_o", "大");
        BET_CONTENT.put("1312_u", "小");
        BET_CONTENT.put("1318_h", "单");
        BET_CONTENT.put("1318_a", "双");
        BET_CONTENT.put("157_h", "单");
        BET_CONTENT.put("157_a", "双");
    }

    
    public static String getBetContent(JSONObject object) {
        // TODO: 2024/4/17 从445继续
        String betType = object.getString("bet_type");
        String betTeam = object.getString("bet_team");
        JSONArray hometeamname = object.getJSONArray("hometeamname");
        JSONArray awayteamname = object.getJSONArray("awayteamname");

        String betContent = betTeam;
        if (StringUtils.equals("1", betType)) {
            if (StringUtils.equals(betTeam, "h")) {
                betContent = getCnName(hometeamname);
            } else if (StringUtils.equals(betTeam, "a")) {
                betContent = getCnName(awayteamname);
            }
        } else if (StringUtils.equalsAny("5", "15", "28", "124", "125",
                "1205", "167", "176", "177", "430", betType)) {
            if (StringUtils.equals(betTeam, "1")) {
                betContent = getCnName(hometeamname);
            } else if (StringUtils.equals(betTeam, "2")) {
                betContent = getCnName(awayteamname);
            } else if (StringUtils.equals(betTeam, "x")) {
                betContent = "和局";
            }
        } else if (StringUtils.equalsAny("7", "17", "20", "21", "25", "27", "1201",
                "1220", "153", "154", "155", "1301", "1303", "1308", "1311",
                "1316", "1324", "168", "185", "432", "191", "411", "229", "501", "425",betType)) {
            if (StringUtils.equals(betTeam, "h")) {
                betContent = getCnName(hometeamname);
            } else if (StringUtils.equals(betTeam, "a")) {
                betContent = getCnName(awayteamname);
            }
        } else if (StringUtils.equals("22", betType)) {
            if (StringUtils.equals(betTeam, "1")) {
                betContent = getCnName(hometeamname);
            } else if (StringUtils.equals(betTeam, "2")) {
                betContent = getCnName(awayteamname);
            } else if (StringUtils.equals(betTeam, "x")) {
                betContent = "没有任何进球";
            }
        } else if (StringUtils.equalsAny("121", betType)) {
            if (StringUtils.equals(betTeam, "a")) {
                betContent = getCnName(awayteamname);
            } else if (StringUtils.equals(betTeam, "x")) {
                betContent = "和局";
            }
        } else if (StringUtils.equalsAny("122", betType)) {
            if (StringUtils.equals(betTeam, "h")) {
                betContent = getCnName(hometeamname);
            } else if (StringUtils.equals(betTeam, "x")) {
                betContent = "和局";
            }
        } else if (StringUtils.equals("29", betType)) {
            // 综合串关
            betContent = "串关";
        } else if (StringUtils.equalsAny("145", "146", "433", "147", "436", "148", "437", "149", "440", "150", "441", betType)) {
            if (StringUtils.equals(betTeam, "y")) {
                betContent = "是";
            } else if (StringUtils.equals(betTeam, "n")) {
                betContent = "否";
            }
        } else if (StringUtils.equalsAny("151", "410", "1224", betType)) {
            if (StringUtils.equals(betTeam, "1x")) {
                betContent = "主队或和局";
            } else if (StringUtils.equals(betTeam, "2x")) {
                betContent = "客队或和局";
            } else if (StringUtils.equals(betTeam, "12")) {
                betContent = "主队或客队";
            }
        } else if (StringUtils.equalsAny("160", "164", "180", betType)) {
            if (StringUtils.equals(betTeam, "1")) {
                betContent = getCnName(hometeamname);
            } else if (StringUtils.equals(betTeam, "2")) {
                betContent = getCnName(awayteamname);
            } else if (StringUtils.equals(betTeam, "x")) {
                betContent = "无";
            }
        } else if (StringUtils.equalsAny("206", "207", "208", "209", betType)) {
            if (StringUtils.equals(betTeam, "h")) {
                betContent = getCnName(hometeamname);
            } else if (StringUtils.equals(betTeam, "a")) {
                betContent = getCnName(awayteamname);
            } else if (StringUtils.equals(betTeam, "n")) {
                betContent = "无";
            }
        } else if (StringUtils.equalsAny("163", "144", betType)) {
            if (StringUtils.equals(betTeam, "hu")) {
                betContent = "主队 / 小";
            } else if (StringUtils.equals(betTeam, "ho")) {
                betContent = "主队 / 大";
            } else if (StringUtils.equals(betTeam, "du")) {
                betContent = "和局 / 小";
            } else if (StringUtils.equals(betTeam, "do")) {
                betContent = "和局 / 大";
            } else if (StringUtils.equals(betTeam, "au")) {
                betContent = "客队 / 小";
            } else if (StringUtils.equals(betTeam, "ao")) {
                betContent = "客队 / 大";
            }
        } else if (StringUtils.equalsAny("170", betType)) {
            if (StringUtils.equals(betTeam, "h")) {
                betContent = "主队";
            } else if (StringUtils.equals(betTeam, "a")) {
                betContent = "客队";
            } else if (StringUtils.equals(betTeam, "b")) {
                betContent = "都有";
            } else if (StringUtils.equals(betTeam, "n")) {
                betContent = "都无";
            }
        } else if (StringUtils.equalsAny("171", "408", betType)) {
            if (StringUtils.equals(betTeam, "h1")) {
                betContent = "主队净胜 1 球";
            } else if (StringUtils.equals(betTeam, "h2")) {
                betContent = "主队净胜 2 球";
            } else if (StringUtils.equals(betTeam, "h3")) {
                betContent = "主队净胜 3 球和以上";
            } else if (StringUtils.equals(betTeam, "d")) {
                betContent = "和局";
            } else if (StringUtils.equals(betTeam, "a1")) {
                betContent = "客队净胜 1 球";
            } else if (StringUtils.equals(betTeam, "a2")) {
                betContent = "客队净胜 2 球";
            } else if (StringUtils.equals(betTeam, "a3")) {
                betContent = "客队净胜 3 球和以上";
            } else if (StringUtils.equals(betTeam, "ng")) {
                betContent = "无进球";
            }
        } else if (StringUtils.equalsAny("172", "415", betType)) {
            if (StringUtils.equals(betTeam, "hh")) {
                betContent = "主队 / 主队";
            } else if (StringUtils.equals(betTeam, "dh")) {
                betContent = "和局 / 主队";
            } else if (StringUtils.equals(betTeam, "ha")) {
                betContent = "主队 / 客队";
            } else if (StringUtils.equals(betTeam, "ah")) {
                betContent = "客队 / 主队";
            } else if (StringUtils.equals(betTeam, "da")) {
                betContent = "和局 / 客队";
            } else if (StringUtils.equals(betTeam, "aa")) {
                betContent = "客队 / 客队";
            } else if (StringUtils.equals(betTeam, "no")) {
                betContent = "无进球";
            }
        } else if (StringUtils.equalsAny("173", "174", "188",
                "427", "189", "434", "190", "435", "210", "211", "212", "213", "214", "215", betType)) {
            if (StringUtils.equals(betTeam, "y")) {
                betContent = "是";
            } else if (StringUtils.equals(betTeam, "n")) {
                betContent = "否";
            }
        } else if (StringUtils.equalsAny("175", betType)) {
            if (StringUtils.equals(betTeam, "hr")) {
                betContent = "主队 / 正规赛";
            } else if (StringUtils.equals(betTeam, "he")) {
                betContent = "主队 / 加时赛";
            } else if (StringUtils.equals(betTeam, "hp")) {
                betContent = "主队 / 点球决胜";
            } else if (StringUtils.equals(betTeam, "ar")) {
                betContent = "客队 / 正规赛";
            } else if (StringUtils.equals(betTeam, "ae")) {
                betContent = "客队 / 加时赛";
            } else if (StringUtils.equals(betTeam, "ap")) {
                betContent = "客队 / 点球决胜";
            }
        } else if (StringUtils.equalsAny("178", "197", "198", "204", "205", "228", "401", "402", "403", "404", betType)) {
            if (StringUtils.equals(betTeam, "o")) {
                betContent = "大";
            } else if (StringUtils.equals(betTeam, "u")) {
                betContent = "小";
            }
        } else if (StringUtils.equalsAny("184", "428", "194", "203", betType)) {
            if (StringUtils.equals(betTeam, "o")) {
                betContent = "单";
            } else if (StringUtils.equals(betTeam, "e")) {
                betContent = "双";
            }
        } else if (StringUtils.equalsAny("186", "431", betType)) {
            if (StringUtils.equals(betTeam, "hd")) {
                betContent = "主队 / 和局";
            } else if (StringUtils.equals(betTeam, "ha")) {
                betContent = "主队 / 客队";
            } else if (StringUtils.equals(betTeam, "da")) {
                betContent = "和局 / 客队";
            }
        } else if (StringUtils.equalsAny("221", "222", betType)) {
            if (StringUtils.equals(betTeam, "2")) {
                betContent = "进球-是";
            } else if (StringUtils.equals(betTeam, "-2")) {
                betContent = "进球-否";
            } else if (StringUtils.equals(betTeam, "4")) {
                betContent = "角球-是";
            } else if (StringUtils.equals(betTeam, "-4")) {
                betContent = "角球-否";
            } else if (StringUtils.equals(betTeam, "8")) {
                betContent = "任意球-是";
            } else if (StringUtils.equals(betTeam, "-8")) {
                betContent = "任意球-否";
            } else if (StringUtils.equals(betTeam, "16")) {
                betContent = "龙门球-是";
            } else if (StringUtils.equals(betTeam, "-16")) {
                betContent = "龙门球-否";
            } else if (StringUtils.equals(betTeam, "32")) {
                betContent = "界外球-是";
            } else if (StringUtils.equals(betTeam, "-32")) {
                betContent = "界外球-否";
            } else if (StringUtils.equals(betTeam, "128")) {
                betContent = "点球-是";
            }
        } else if (StringUtils.equalsAny("223", betType)) {
            if (StringUtils.equals(betTeam, "1")) {
                betContent = "无";
            } else if (StringUtils.equals(betTeam, "2")) {
                betContent = "进球";
            } else if (StringUtils.equals(betTeam, "4")) {
                betContent = "角球";
            } else if (StringUtils.equals(betTeam, "8")) {
                betContent = "任意球";
            } else if (StringUtils.equals(betTeam, "16")) {
                betContent = "龙门球";
            } else if (StringUtils.equals(betTeam, "32")) {
                betContent = "界外球";
            }
        } else if (StringUtils.equalsAny("224", betType)) {
            if (StringUtils.equals(betTeam, "1")) {
                betContent = "无";
            } else if (StringUtils.equals(betTeam, "2")) {
                betContent = "进球";
            } else if (StringUtils.equals(betTeam, "4")) {
                betContent = "角球";
            } else if (StringUtils.equals(betTeam, "64")) {
                betContent = "罚牌";
            } else if (StringUtils.equals(betTeam, "128")) {
                betContent = "点球";
            }
        } else if (StringUtils.equalsAny("225", betType)) {
            if (StringUtils.equals(betTeam, "1")) {
                betContent = "是";
            } else if (StringUtils.equals(betTeam, "44")) {
                betContent = "否";
            }
        } else if (StringUtils.equalsAny("226", betType)) {
            if (StringUtils.equals(betTeam, "194")) {
                betContent = "进球 / 罚牌 / 点球";
            } else if (StringUtils.equals(betTeam, "4")) {
                betContent = "角球";
            } else if (StringUtils.equals(betTeam, "1")) {
                betContent = "无";
            }
        } else if (StringUtils.equalsAny("417", betType)) {
            if (StringUtils.equals(betTeam, "yh")) {
                betContent = "对/主队";
            } else if (StringUtils.equals(betTeam, "ya")) {
                betContent = "对/客队";
            } else if (StringUtils.equals(betTeam, "yd")) {
                betContent = "对/和局";
            } else if (StringUtils.equals(betTeam, "nh")) {
                betContent = "否/主队";
            } else if (StringUtils.equals(betTeam, "na")) {
                betContent = "否/客队";
            } else if (StringUtils.equals(betTeam, "nd")) {
                betContent = "否/和局";
            }
        } else if (StringUtils.equalsAny("418", betType)) {
            if (StringUtils.equals(betTeam, "yo")) {
                betContent = "对&大于";
            } else if (StringUtils.equals(betTeam, "yu")) {
                betContent = "对&小于";
            } else if (StringUtils.equals(betTeam, "no")) {
                betContent = "否&大于";
            } else if (StringUtils.equals(betTeam, "nu")) {
                betContent = "否&小于";
            }
        } else if (StringUtils.equalsAny("419", "420","421",betType)) {
            if (StringUtils.equals(betTeam, "1h")) {
                betContent = "上半场";
            } else if (StringUtils.equals(betTeam, "2h")) {
                betContent = "下半场";
            } else if (StringUtils.equals(betTeam, "n")) {
                betContent = "上下半场都没有进球";
            }
        } else if (StringUtils.equalsAny("422", "423",betType)) {
            if (StringUtils.equals(betTeam, "h")) {
                betContent = getCnName(hometeamname);
            } else if (StringUtils.equals(betTeam, "a")) {
                betContent = getCnName(awayteamname);
            } else if (StringUtils.equals(betTeam, "n")) {
                betContent = "没有";
            }
        } else if (StringUtils.equalsAny("424",betType)) {
            if (StringUtils.equals(betTeam, "s")) {
                betContent = "射门";
            } else if (StringUtils.equals(betTeam, "h")) {
                betContent = "头射";
            } else if (StringUtils.equals(betTeam, "p")) {
                betContent = "罚球";
            } else if (StringUtils.equals(betTeam, "fk")) {
                betContent = "任意球";
            } else if (StringUtils.equals(betTeam, "og")) {
                betContent = "乌龙球";
            } else if (StringUtils.equals(betTeam, "ng")) {
                betContent = "没进球";
            }
        } else if (StringUtils.equalsAny("426",betType)) {
            if (StringUtils.equals(betTeam, "h1")) {
                betContent = "主队净胜 1 球";
            } else if (StringUtils.equals(betTeam, "h2+")) {
                betContent = "主队净胜 2 球以上";
            } else if (StringUtils.equals(betTeam, "d")) {
                betContent = "和局";
            } else if (StringUtils.equals(betTeam, "a1")) {
                betContent = "客队净胜 1 球";
            } else if (StringUtils.equals(betTeam, "a2+")) {
                betContent = "客队净胜 2 球以上";
            } else if (StringUtils.equals(betTeam, "ng")) {
                betContent = "没进球";
            }
        } else {
            betContent = BET_CONTENT.getOrDefault(betType + "_" + betTeam, betTeam);
        }
        return betContent;
    }

    public static String getCnName(JSONArray array) {
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            String lang = jsonObject.getString("lang");
            if (StringUtils.equals(lang, "cs")) {
                return jsonObject.getString("name");
            }
        }
        return "未知";
    }
}
