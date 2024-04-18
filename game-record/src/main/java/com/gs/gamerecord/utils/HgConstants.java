package com.gs.gamerecord.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gs.commons.entity.HgRecord;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    public static void getHgRecordInfo(JSONArray wagerDateArr, List<HgRecord> recordList, String owner) {
        for (int i = 0; i < wagerDateArr.size(); i++) {
            JSONObject wager = wagerDateArr.getJSONObject(i);
            HgRecord hgRecord = new HgRecord();
            //判断是不是本平台用户
            String ownerUsername = wager.getString("username");
            String subOwnerStr = ownerUsername.substring(0, 2);
            if(!StrUtil.equals(subOwnerStr, owner)){
                continue;
            }
            String username = ownerUsername.substring(2);
            hgRecord.setUserName(username);
            hgRecord.setCreateTime(DateUtil.date());
            hgRecord.setUpdateTime(DateUtil.date());
            hgRecord.setGameName(HgConstants.GAME_NAME.getOrDefault(wager.getString("gtype"), wager.getString("gtype")));
            hgRecord.setAllBet(wager.getBigDecimal("gold"));
            hgRecord.setEffectiveBet(wager.getBigDecimal("degold"));
            hgRecord.setIoratio(wager.getBigDecimal("ioratio"));
            hgRecord.setLeague(wager.getString("league"));
            hgRecord.setOrderNo(wager.getString("id"));
            BigDecimal win = wager.getBigDecimal("wingold");
            BigDecimal profit = win.subtract(hgRecord.getEffectiveBet());
            hgRecord.setProfit(profit);
            hgRecord.setParlaynum(wager.getInteger("parlaynum"));
            hgRecord.setBetTime(DateUtil.offsetHour(wager.getDate("adddate"), 12));
            hgRecord.setParlaysub(wager.getString("parlaysub"));
            hgRecord.setLeague(wager.getString("league"));
            String wtype = wager.getString("wtype");
            hgRecord.setRtype(wtype);
            hgRecord.setOrderContent(wager.getString("order"));
            hgRecord.setOddsFormat(wager.getString("oddsFormat"));
            wtype = StrUtil.contains(wtype,"滚球") ? "滚球" : StrUtil.isNotBlank(hgRecord.getParlaysub()) ? "串关" : "";
            hgRecord.setWtype(wtype);
            hgRecord.setTnameAway(wager.getString("tname_away"));
            hgRecord.setTnameHome(wager.getString("tname_home"));
            hgRecord.setPlatUserName(wager.getString("username"));
            hgRecord.setStrong(wager.getString("strong"));
            hgRecord.setScore(wager.getString("score"));
            hgRecord.setResultScore(wager.getString("result_score"));
            hgRecord.setResultStatus(wager.getString("result"));
            Date resultdate = wager.getDate("resultdate");
            if (resultdate != null) {
                hgRecord.setSettleTime(DateUtil.offsetHour(resultdate, 12));
            }
            hgRecord.setSettleStatus(wager.getIntValue("settle"));
            hgRecord.setRawData(wager.toJSONString());
            if(StrUtil.isBlank(hgRecord.getParlaysub())){
                String matchDatetimeStr = wager.getString("orderdate") + " " + wager.getString("ordertime");
                Date matchDatetime = DateUtil.parseDateTime(matchDatetimeStr);
                hgRecord.setMatchDatetime(DateUtil.offsetHour(matchDatetime, 12));
            }
            recordList.add(hgRecord);
        }
    }
}
