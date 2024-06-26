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
        if (StringUtils.equalsAny(betType,"1","7", "17", "20", "21", "25", "27", "1201",
                "1220", "153", "154", "155", "1301", "1303", "1308", "1311",
                "1316", "1324", "168", "185", "432", "191", "411", "229", "501", "425", "603", "604",
                "605", "606", "607", "609", "612", "613", "617", "635", "636", "637", "2705", "2706", "460", "2801",
                "2805", "2806", "2809", "5306", "9001", "9002", "9004", "9006", "9007", "9008", "9010", "9011", "9012", "9014",
                "9015", "9016", "9017", "9018","9020", "9021", "9024", "9026", "9027", "9028", "9030", "9031", "9032", "9033", "9034",
                "9036", "9037", "9038", "9039", "9040", "9042", "9043", "9044", "9045", "9046", "9048", "9049", "9050", "9051",
                "9052", "9054","9055","9056","9057","9059","9062","9064","9065","9066","9067","9068","9069","9063","9072",
                "9077", "")) {
            if (StringUtils.equals(betTeam, "h")) {
                betContent = getCnName(hometeamname);
            } else if (StringUtils.equals(betTeam, "a")) {
                betContent = getCnName(awayteamname);
            }
        } else if (StringUtils.equalsAny(betType,"5", "15", "28", "124", "125",
                "1205", "167", "176", "177", "430", "630", "2701", "2702", "458", "459", "5302", "5303")) {
            if (StringUtils.equals(betTeam, "1")) {
                betContent = getCnName(hometeamname);
            } else if (StringUtils.equals(betTeam, "2")) {
                betContent = getCnName(awayteamname);
            } else if (StringUtils.equals(betTeam, "x")) {
                betContent = "和局";
            }
        } else if (StringUtils.equalsAny(betType,"22")) {
            if (StringUtils.equals(betTeam, "1")) {
                betContent = getCnName(hometeamname);
            } else if (StringUtils.equals(betTeam, "2")) {
                betContent = getCnName(awayteamname);
            } else if (StringUtils.equals(betTeam, "x")) {
                betContent = "没有任何进球";
            }
        } else if (StringUtils.equalsAny(betType,"121")) {
            if (StringUtils.equals(betTeam, "a")) {
                betContent = getCnName(awayteamname);
            } else if (StringUtils.equals(betTeam, "x")) {
                betContent = "和局";
            }
        } else if (StringUtils.equalsAny(betType,"122")) {
            if (StringUtils.equals(betTeam, "h")) {
                betContent = getCnName(hometeamname);
            } else if (StringUtils.equals(betTeam, "x")) {
                betContent = "和局";
            }
        } else if (StringUtils.equalsAny(betType,"29")) {
            // 综合串关
            betContent = "串关";
        } else if (StringUtils.equalsAny(betType,"145", "146", "433", "147", "436", "148", "437", "149", "440", "150", "441",
                "9073")) {
            if (StringUtils.equals(betTeam, "y")) {
                betContent = "是";
            } else if (StringUtils.equals(betTeam, "n")) {
                betContent = "否";
            }
        } else if (StringUtils.equalsAny(betType,"151", "410", "1224")) {
            if (StringUtils.equals(betTeam, "1x")) {
                betContent = "主队或和局";
            } else if (StringUtils.equals(betTeam, "2x")) {
                betContent = "客队或和局";
            } else if (StringUtils.equals(betTeam, "12")) {
                betContent = "主队或客队";
            }
        } else if (StringUtils.equalsAny(betType,"160", "164", "180", "453", "2802")) {
            if (StringUtils.equals(betTeam, "1")) {
                betContent = getCnName(hometeamname);
            } else if (StringUtils.equals(betTeam, "2")) {
                betContent = getCnName(awayteamname);
            } else if (StringUtils.equals(betTeam, "x")) {
                betContent = "无";
            }
        } else if (StringUtils.equalsAny(betType,"206", "207", "208", "209")) {
            if (StringUtils.equals(betTeam, "h")) {
                betContent = getCnName(hometeamname);
            } else if (StringUtils.equals(betTeam, "a")) {
                betContent = getCnName(awayteamname);
            } else if (StringUtils.equals(betTeam, "n")) {
                betContent = "无";
            }
        } else if (StringUtils.equalsAny(betType,"163", "144", "646", "143")) {
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
        } else if (StringUtils.equalsAny(betType,"170", "448")) {
            if (StringUtils.equals(betTeam, "h")) {
                betContent = "主队";
            } else if (StringUtils.equals(betTeam, "a")) {
                betContent = "客队";
            } else if (StringUtils.equals(betTeam, "b")) {
                betContent = "都有";
            } else if (StringUtils.equals(betTeam, "n")) {
                betContent = "都无";
            } else if (StringUtils.equals(betTeam, "ng")) {
                betContent = "两者皆不";
            }
        } else if (StringUtils.equalsAny(betType,"171", "408")) {
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
        } else if (StringUtils.equalsAny(betType,"172", "415", "633", "634", "645")) {
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
            } else if (StringUtils.equals(betTeam, "hd")) {
                betContent = "主队 / 和局";
            } else if (StringUtils.equals(betTeam, "dd")) {
                betContent = "和局 / 和局";
            } else if (StringUtils.equals(betTeam, "ad")) {
                betContent = "客队 / 和局";
            }
        } else if (StringUtils.equalsAny(betType,"173", "174", "188",
                "427", "189", "434", "190", "435", "210", "211", "212", "213", "214", "215")) {
            if (StringUtils.equals(betTeam, "y")) {
                betContent = "是";
            } else if (StringUtils.equals(betTeam, "n")) {
                betContent = "否";
            }
        } else if (StringUtils.equalsAny(betType,"175")) {
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
        } else if (StringUtils.equalsAny(betType,"178", "197", "198", "204", "205", "228", "401", "402", "403", "404", "610",
                "615", "616", "638", "639", "641", "2703", "2704", "461", "462", "463", "464", "2803", "2804", "2811", "2812", "5304",
                "5307", "9003", "9009", "9013", "9019", "9025", "9029", "9035", "9041", "9047", "9053", "9058", "9060", "9061", "9070")) {
            if (StringUtils.equals(betTeam, "o")) {
                betContent = "大";
            } else if (StringUtils.equals(betTeam, "u")) {
                betContent = "小";
            }
        } else if (StringUtils.equalsAny(betType,"184", "428", "194", "203", "611", "9005", "9071", "9078", "9079")) {
            if (StringUtils.equals(betTeam, "o")) {
                betContent = "单";
            } else if (StringUtils.equals(betTeam, "e")) {
                betContent = "双";
            }
        } else if (StringUtils.equalsAny(betType,"186", "431", "631")) {
            if (StringUtils.equals(betTeam, "hd")) {
                betContent = "主队 / 和局";
            } else if (StringUtils.equals(betTeam, "ha")) {
                betContent = "主队 / 客队";
            } else if (StringUtils.equals(betTeam, "da")) {
                betContent = "和局 / 客队";
            }
        } else if (StringUtils.equalsAny(betType,"221", "222")) {
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
        } else if (StringUtils.equalsAny(betType,"223")) {
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
        } else if (StringUtils.equalsAny(betType,"224")) {
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
        } else if (StringUtils.equalsAny(betType,"225")) {
            if (StringUtils.equals(betTeam, "1")) {
                betContent = "是";
            } else if (StringUtils.equals(betTeam, "44")) {
                betContent = "否";
            }
        } else if (StringUtils.equalsAny(betType,"226")) {
            if (StringUtils.equals(betTeam, "194")) {
                betContent = "进球 / 罚牌 / 点球";
            } else if (StringUtils.equals(betTeam, "4")) {
                betContent = "角球";
            } else if (StringUtils.equals(betTeam, "1")) {
                betContent = "无";
            }
        } else if (StringUtils.equalsAny(betType,"417", "456")) {
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
        } else if (StringUtils.equalsAny(betType,"418", "457")) {
            if (StringUtils.equals(betTeam, "yo")) {
                betContent = "对&大于";
            } else if (StringUtils.equals(betTeam, "yu")) {
                betContent = "对&小于";
            } else if (StringUtils.equals(betTeam, "no")) {
                betContent = "否&大于";
            } else if (StringUtils.equals(betTeam, "nu")) {
                betContent = "否&小于";
            }
        } else if (StringUtils.equalsAny(betType,"419", "420","421", "452")) {
            if (StringUtils.equals(betTeam, "1h")) {
                betContent = "上半场";
            } else if (StringUtils.equals(betTeam, "2h")) {
                betContent = "下半场";
            } else if (StringUtils.equals(betTeam, "n")) {
                betContent = "上下半场都没有进球";
            }
        } else if (StringUtils.equalsAny(betType,"422", "423")) {
            if (StringUtils.equals(betTeam, "h")) {
                betContent = getCnName(hometeamname);
            } else if (StringUtils.equals(betTeam, "a")) {
                betContent = getCnName(awayteamname);
            } else if (StringUtils.equals(betTeam, "n")) {
                betContent = "没有";
            }
        } else if (StringUtils.equalsAny(betType,"424")) {
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
        } else if (StringUtils.equalsAny(betType,"426")) {
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
        } else if (StringUtils.equalsAny(betType,"445")) {
            if (StringUtils.equals(betTeam, "yy")) {
                betContent = "两队都有进球/两队都有进球";
            } else if (StringUtils.equals(betTeam, "yn")) {
                betContent = "两队都有进球/两队都没有进球";
            } else if (StringUtils.equals(betTeam, "ny")) {
                betContent = "两队都没有进球/两队都有进球";
            } else if (StringUtils.equals(betTeam, "nn")) {
                betContent = "两队都没有进球/两队都没有进球";
            }
        } else if (StringUtils.equalsAny(betType,"446", "447")) {
            if (StringUtils.equals(betTeam, "yy")) {
                betContent = "有/有";
            } else if (StringUtils.equals(betTeam, "yn")) {
                betContent = "有/没有";
            } else if (StringUtils.equals(betTeam, "ny")) {
                betContent = "没有/有";
            } else if (StringUtils.equals(betTeam, "nn")) {
                betContent = "没有/没有";
            }
        } else if (StringUtils.equalsAny(betType,"601", "602", "608", "614", "2807", "2808")) {
            if (StringUtils.equals(betTeam, "h1-2")) {
                betContent = "主队净胜 1 到 2 分";
            } else if (StringUtils.equals(betTeam, "h3-6")) {
                betContent = "主队净胜 3 到 6 分";
            } else if (StringUtils.equals(betTeam, "h7-9")) {
                betContent = "主队净胜 7 到 9 分";
            } else if (StringUtils.equals(betTeam, "h10-13")) {
                betContent = "主队净胜 10 到 13 分";
            } else if(StringUtils.equals(betTeam, "h14-16")){
                betContent = "主队净胜 14 到 16 分";
            } else if(StringUtils.equals(betTeam, "h17-20")){
                betContent = "主队净胜 17 到 20 分";
            } else if(StringUtils.equals(betTeam, "h21+")){
                betContent = "主队净胜 21 分以上";
            } else if(StringUtils.equals(betTeam, "a1-2")){
                betContent = "客队净胜 1 到 2 分";
            } else if(StringUtils.equals(betTeam, "a3-6")){
                betContent = "客队净胜 3 到 6 分";
            } else if(StringUtils.equals(betTeam, "a7-9")){
                betContent = "客队净胜 7 到 9 分";
            } else if(StringUtils.equals(betTeam, "a10-13")){
                betContent = "客队净胜 10 到 13 分";
            } else if(StringUtils.equals(betTeam, "a14-16")){
                betContent = "客队净胜 14 到 16 分";
            } else if(StringUtils.equals(betTeam, "a17-20")){
                betContent = "客队净胜 17 到 20 分";
            } else if(StringUtils.equals(betTeam, "a21+")){
                betContent = "客队净胜 21 分以上";
            } else if(StringUtils.equals(betTeam, "h1-5")){
                betContent = "主队净胜 1 到 5 分";
            } else if(StringUtils.equals(betTeam, "h6-10")){
                betContent = "主队净胜 6 到 10 分";
            } else if(StringUtils.equals(betTeam, "h11-15")){
                betContent = "主队净胜 11 到 15 分";
            } else if(StringUtils.equals(betTeam, "h16-20")){
                betContent = "主队净胜 16 到 20 分";
            } else if(StringUtils.equals(betTeam, "h21-25")){
                betContent = "主队净胜 21 到 25 分";
            } else if(StringUtils.equals(betTeam, "h26+")){
                betContent = "主队净胜 26 分以上";
            } else if(StringUtils.equals(betTeam, "a1-5")){
                betContent = "客队净胜 1 到 5 分";
            } else if(StringUtils.equals(betTeam, "a6-10")){
                betContent = "客队净胜 6 到 10 分";
            } else if(StringUtils.equals(betTeam, "a11-15")){
                betContent = "客队净胜 11 到 15 分";
            } else if(StringUtils.equals(betTeam, "a16-20")){
                betContent = "客队净胜 16 到 20 分";
            } else if(StringUtils.equals(betTeam, "a21-25")){
                betContent = "客队净胜 21 到 25 分";
            } else if(StringUtils.equals(betTeam, "a26+")){
                betContent = "客队净胜 26 分以上";
            } else if(StringUtils.equals(betTeam, "h1-4")){
                betContent = "主队净胜 1 到 4 分";
            } else if(StringUtils.equals(betTeam, "h5-8")){
                betContent = "主队净胜 5 到 8 分";
            } else if(StringUtils.equals(betTeam, "h9+")){
                betContent = "主队净胜 9 分以上";
            } else if(StringUtils.equals(betTeam, "d")){
                betContent = "和局";
            } else if(StringUtils.equals(betTeam, "a1-4")){
                betContent = "客队净胜 1 到 4 分";
            } else if(StringUtils.equals(betTeam, "a5-8")){
                betContent = "客队净胜 5 到 8 分";
            } else if(StringUtils.equals(betTeam, "a9+")){
                betContent = "客队净胜 9 分以上";
            } else if(StringUtils.equals(betTeam, "h11+")){
                betContent = "主队净胜 11+分";
            } else if(StringUtils.equals(betTeam, "a11+")){
                betContent = "客队净胜 11+分";
            }
        } else if (StringUtils.equalsAny(betType,"618")) {
            betContent = "主队加客队总比分的末位数为" + betTeam;
        } else if (StringUtils.equalsAny(betType,"619")) {
            betContent = "主队总比分的末位数为" + betTeam;
        } else if (StringUtils.equalsAny(betType,"620")) {
            betContent = "客队总比分的末位数为" + betTeam;
        } else if (StringUtils.equalsAny(betType,"621")) {
            betContent = "主队加客队上半场总比分的末位数为" + betTeam;
        } else if (StringUtils.equalsAny(betType,"622")) {
            betContent = "主队上半场总比分的末位数为" + betTeam;
        } else if (StringUtils.equalsAny(betType,"623")) {
            betContent = "客队上半场总比分的末位数为" + betTeam;
        } else if (StringUtils.equalsAny(betType,"624")) {
            betContent = "主队加客队下半场总比分的末位数为" + betTeam;
        } else if (StringUtils.equalsAny(betType,"625")) {
            betContent = "主队下半场总比分的末位数为" + betTeam;
        } else if (StringUtils.equalsAny(betType,"626")) {
            betContent = "客队下半场总比分的末位数为" + betTeam;
        } else if (StringUtils.equalsAny(betType,"627")) {
            betContent = "主队加客队第 X 节总比分的末位数为" + betTeam;
        } else if (StringUtils.equalsAny(betType,"628")) {
            betContent = "主队第 X 节总比分的末位数为" + betTeam;
        } else if (StringUtils.equalsAny(betType,"629")) {
            betContent = "客队第 X 节总比分的末位数为" + betTeam;
        } else if (StringUtils.equalsAny(betType,"632")) {
            if (StringUtils.equals(betTeam, "oooo")) {
                betContent = "单/单/单/单";
            } else if (StringUtils.equals(betTeam, "oooe")) {
                betContent = "单/单/单/双";
            } else if (StringUtils.equals(betTeam, "ooeo")) {
                betContent = "单/单/双/单";
            } else if (StringUtils.equals(betTeam, "oeoo")) {
                betContent = "单/双/单/单";
            } else if (StringUtils.equals(betTeam, "eooo")) {
                betContent = "双/单/单/单";
            } else if (StringUtils.equals(betTeam, "eeee")) {
                betContent = "双/双/双/双";
            } else if (StringUtils.equals(betTeam, "eeeo")) {
                betContent = "双/双/双/单";
            } else if (StringUtils.equals(betTeam, "eeoe")) {
                betContent = "双/双/单/双";
            } else if (StringUtils.equals(betTeam, "eoee")) {
                betContent = "双/单/双/双";
            } else if (StringUtils.equals(betTeam, "oeee")) {
                betContent = "单/双/双/双";
            } else if (StringUtils.equals(betTeam, "ooee")) {
                betContent = "单/单/双/双";
            } else if (StringUtils.equals(betTeam, "oeoe")) {
                betContent = "单/双/单/双";
            } else if (StringUtils.equals(betTeam, "eoeo")) {
                betContent = "双/单/双/单";
            } else if (StringUtils.equals(betTeam, "eeoo")) {
                betContent = "双/双/单/单";
            } else if (StringUtils.equals(betTeam, "eooe")) {
                betContent = "双/单/单/双";
            } else if (StringUtils.equals(betTeam, "oeeo")) {
                betContent = "单/双/双/单";
            }
        } else if (StringUtils.equalsAny(betType,"640")) {
            if (StringUtils.equals(betTeam, "0")) {
                betContent = "无和局赛节数";
            } else if (StringUtils.equals(betTeam, "1")) {
                betContent = "有 1 以上的和局赛节数(包含 1)";
            }
        } else if (StringUtils.equalsAny(betType,"642")) {
            if (StringUtils.equals(betTeam, "h12")) {
                betContent = "第 1 节和第 2 节由主队获胜";
            } else if (StringUtils.equals(betTeam, "h13")) {
                betContent = "第 1 节和第 3 节由主队获胜";
            } else if (StringUtils.equals(betTeam, "h14")) {
                betContent = "第 1 节和第 4 节由主队获胜";
            } else if (StringUtils.equals(betTeam, "h23")) {
                betContent = "第 2 节和第 3 节由主队获胜";
            } else if (StringUtils.equals(betTeam, "h24")) {
                betContent = "第 2 节和第 4 节由主队获胜";
            } else if (StringUtils.equals(betTeam, "h34")) {
                betContent = "第 3 节和第 4 节由主队获胜";
            } else if (StringUtils.equals(betTeam, "a12")) {
                betContent = "第 1 节和第 2 节由客队获胜";
            } else if (StringUtils.equals(betTeam, "a13")) {
                betContent = "第 1 节和第 3 节由客队获胜";
            } else if (StringUtils.equals(betTeam, "a14")) {
                betContent = "第 1 节和第 4 节由客队获胜";
            } else if (StringUtils.equals(betTeam, "a23")) {
                betContent = "第 2 节和第 3 节由客队获胜";
            } else if (StringUtils.equals(betTeam, "a24")) {
                betContent = "第 2 节和第 4 节由客队获胜";
            } else if (StringUtils.equals(betTeam, "a34")) {
                betContent = "第 3 节和第 4 节由客队获胜";
            } else if (StringUtils.equals(betTeam, "aos12")) {
                betContent = "第 1 节和第 2 节出现和局或二队各赢一节";
            } else if (StringUtils.equals(betTeam, "aos13")) {
                betContent = "第 1 节和第 3 节出现和局或二队各赢一节";
            } else if (StringUtils.equals(betTeam, "aos14")) {
                betContent = "第 1 节和第 4 节出现和局或二队各赢一节";
            } else if (StringUtils.equals(betTeam, "aos23")) {
                betContent = "第 2 节和第 3 节出现和局或二队各赢一节";
            } else if (StringUtils.equals(betTeam, "aos24")) {
                betContent = "第 2 节和第 4 节出现和局或二队各赢一节";
            } else if (StringUtils.equals(betTeam, "aos34")) {
                betContent = " 第 3 节和第 4 节出现和局或二队各赢一节";
            }
        } else if (StringUtils.equalsAny(betType,"643")) {
            if (StringUtils.equals(betTeam, "1q")) {
                betContent = "第 1 赛节";
            } else if (StringUtils.equals(betTeam, "2q")) {
                betContent = "第 2 赛节";
            } else if (StringUtils.equals(betTeam, "3q")) {
                betContent = "第 3 赛节";
            } else if (StringUtils.equals(betTeam, "4q")) {
                betContent = "第 4 赛节";
            } else if (StringUtils.equals(betTeam, "tie")) {
                betContent = "一样多";
            }
        } else if (StringUtils.equalsAny(betType,"449")) {
            if (StringUtils.equals(betTeam, "1xo")) {
                betContent = "主/和局 & 大";
            } else if (StringUtils.equals(betTeam, "1xu")) {
                betContent = "主/和局 & 小";
            } else if (StringUtils.equals(betTeam, "12o")) {
                betContent = "主/客 & 大";
            } else if (StringUtils.equals(betTeam, "12u")) {
                betContent = "主/客 & 小";
            } else if (StringUtils.equals(betTeam, "2xo")) {
                betContent = "客/和局 & 大";
            } else if (StringUtils.equals(betTeam, "2xu")) {
                betContent = "客/和局 & 小";
            }
        } else if (StringUtils.equalsAny(betType,"450")) {
            if (StringUtils.equals(betTeam, "oo")) {
                betContent = "单 & 大";
            } else if (StringUtils.equals(betTeam, "ou")) {
                betContent = "单 & 小";
            } else if (StringUtils.equals(betTeam, "eo")) {
                betContent = "双 & 大";
            } else if (StringUtils.equals(betTeam, "eu")) {
                betContent = "双 & 小";
            }
        } else if (StringUtils.equalsAny(betType,"451")) {
            if (StringUtils.equals(betTeam, "y1x")) {
                betContent = "是 & 主/和局";
            } else if (StringUtils.equals(betTeam, "y12")) {
                betContent = "是 & 主/客";
            } else if (StringUtils.equals(betTeam, "y2x")) {
                betContent = "是 & 客/和局";
            } else if (StringUtils.equals(betTeam, "n1x")) {
                betContent = "否 & 主/和局";
            } else if (StringUtils.equals(betTeam, "n12")) {
                betContent = "否 & 主/客";
            } else if (StringUtils.equals(betTeam, "n2x")) {
                betContent = "否 & 客/和局";
            }
        } else if (StringUtils.equalsAny(betType,"644", "2707")) {
            if (StringUtils.equals(betTeam, "aos")) {
                betContent = "其他比分";
            } else {
                betContent = betTeam;
            }
        } else if (StringUtils.equalsAny(betType,"454")) {
            if (StringUtils.equals(betTeam, "1xh")) {
                betContent = "主/和局 & 主";
            } else if (StringUtils.equals(betTeam, "12h")) {
                betContent = "主/客 & 主";
            } else if (StringUtils.equals(betTeam, "2xh")) {
                betContent = "客/和局 & 主";
            } else if (StringUtils.equals(betTeam, "1xa")) {
                betContent = "主/和局 & 客";
            } else if (StringUtils.equals(betTeam, "12a")) {
                betContent = "主/客 & 客";
            } else if (StringUtils.equals(betTeam, "2xa")) {
                betContent = "客/和局 & 客";
            } else if (StringUtils.equals(betTeam, "ng")) {
                betContent = "无进球";
            }
        } else if (StringUtils.equalsAny(betType,"455")) {
            if (StringUtils.equals(betTeam, "1-10")) {
                betContent = "00:01-10:00";
            } else if (StringUtils.equals(betTeam, "11-20")) {
                betContent = "10:01-20:00";
            } else if (StringUtils.equals(betTeam, "21-30")) {
                betContent = "20:01-30:00";
            } else if (StringUtils.equals(betTeam, "31-40")) {
                betContent = "30:01-40:00";
            } else if (StringUtils.equals(betTeam, "41-50")) {
                betContent = "40:01-50:00";
            } else if (StringUtils.equals(betTeam, "51-60")) {
                betContent = "50:01-60:00";
            } else if (StringUtils.equals(betTeam, "61-70")) {
                betContent = "60:01-70:00";
            } else if (StringUtils.equals(betTeam, "71-80")) {
                betContent = "70:01-80:00";
            } else if (StringUtils.equals(betTeam, "81-90")) {
                betContent = "80:01-90:00";
            } else if (StringUtils.equals(betTeam, "ng")) {
                betContent = "无进球";
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
