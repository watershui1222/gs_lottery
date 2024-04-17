package com.gs.gamerecord.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gs.commons.entity.LyRecord;
import com.gs.commons.entity.PlatRecordControl;
import com.gs.commons.service.LyRecordService;
import com.gs.commons.service.PlatRecordControlService;
import com.gs.commons.utils.AesUtils;
import com.gs.gamerecord.utils.LyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 每周奖励
 *
 * @author Administrator
 */
@Slf4j
@Component
public class LyRecordSchedule {

    @Value("${platform.LeYou.apiDomain}")
    public String apiDomain;
    @Value("${platform.LeYou.agent}")
    public String agent;
    @Value("${platform.LeYou.aesKey}")
    public String aesKey;
    @Value("${platform.LeYou.md5Key}")
    public String md5Key;
    @Value("${platform.LeYou.betRecordDomain}")
    public String betRecordDomain;
    @Value("${platform.owner}")
    public String owner;

    @Autowired
    private PlatRecordControlService platRecordControlService;

    @Autowired
    private LyRecordService lyRecordService;

    @Scheduled(cron = "0/30 * * * * ?")
    public void lyRecord() throws Exception {
        Date now = new Date();
        PlatRecordControl ly = platRecordControlService.getOne(
                new LambdaQueryWrapper<PlatRecordControl>()
                        .eq(PlatRecordControl::getStatus, 0)
                        .eq(PlatRecordControl::getPlatCode, "ly")
        );
        if (ly != null) {
            log.info("乐游---拉单开始[{}]-[{}]", DateUtil.formatDateTime(ly.getBeginTime()), DateUtil.formatDateTime(ly.getEndTime()));
            getRecord(ly.getBeginTime(), ly.getEndTime());
            log.info("乐游---拉单完成[{}]-[{}]", DateUtil.formatDateTime(ly.getBeginTime()), DateUtil.formatDateTime(ly.getEndTime()));
            // 如果当前时间大于结束时间，更新拉取时间范围
            if (DateUtil.compare(now, ly.getEndTime()) == 1) {
                platRecordControlService.update(
                        new LambdaUpdateWrapper<PlatRecordControl>()
                                .set(PlatRecordControl::getBeginTime, ly.getEndTime())
                                .set(PlatRecordControl::getEndTime, DateUtil.offsetMinute(ly.getEndTime(), 15))
                                .eq(PlatRecordControl::getPlatCode, "ly")
                );
            }
        }
    }

    public void getRecord(Date beginTime,Date endTime) throws Exception {
        String agent = this.agent;
        String timestamp = String.valueOf(DateUtil.current());
        String aesKey = this.aesKey;
        StringBuilder paramSb = new StringBuilder();
        paramSb.append("s=6&")
                .append("startTime=").append(beginTime.getTime())
                .append("&endTime=").append(endTime.getTime());
        String param = AesUtils.AESEncrypt(paramSb.toString(), aesKey, true);
        String key = SecureUtil.md5(agent + timestamp + this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.betRecordDomain).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
        String result = HttpUtil.get(urlSB.toString());
        JSONObject jsonResult = JSONObject.parseObject(result);
        JSONObject d = jsonResult.getJSONObject("d");
        int code = d.getIntValue("code");
        if (code == 0) {
            JSONObject list = d.getJSONObject("list");
            int count = d.getIntValue("count");
            if (count <= 0) {
                return;
            }
            JSONArray gameIds = list.getJSONArray("GameID"); //注单ID
            JSONArray accounts = list.getJSONArray("Accounts");//会员账号
            JSONArray serverID = list.getJSONArray("ServerID");//游戏房间代号
            JSONArray kindID = list.getJSONArray("KindID");//游戏代号
            JSONArray cellScore = list.getJSONArray("CellScore");//有效投注额
            JSONArray allBet = list.getJSONArray("AllBet");//总投注额
            JSONArray profit = list.getJSONArray("Profit");//输赢⾦额
            JSONArray revenue = list.getJSONArray("Revenue"); //反⽔⾦额
            JSONArray gameStartTime = list.getJSONArray("GameStartTime");//游戏开始时间
            JSONArray gameEndTime = list.getJSONArray("GameEndTime");//游戏结束时间
            List<LyRecord> lyRecords = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                String account = accounts.getString(i);
                LyRecord lyRecord = new LyRecord();
                String ownerUsername = account.split("_")[1];//还需去掉字符串前两位
                //判断是不是本平台用户
                String subOwnerStr = ownerUsername.substring(0, 2);
                if(!StrUtil.equals(subOwnerStr, this.owner)){
                    continue;
                }
                String username = ownerUsername.substring(2);
                lyRecord.setUserName(username);
                lyRecord.setPlatUserName(account);
                lyRecord.setOrderNo(gameIds.getString(i));
                lyRecord.setGameId(kindID.getString(i));
                lyRecord.setGameName(LyConstants.GAME_NAME.getOrDefault(lyRecord.getGameId(), lyRecord.getGameId()));
                lyRecord.setEffectiveBet(cellScore.getBigDecimal(i));
                lyRecord.setAllBet(allBet.getBigDecimal(i));
                lyRecord.setProfit(profit.getBigDecimal(i));
                lyRecord.setGameStartTime(gameStartTime.getDate(i));
                lyRecord.setGameEndTime(gameEndTime.getDate(i));
                lyRecord.setSettleTime(gameEndTime.getDate(i));
                lyRecord.setCreateTime(new Date());
                lyRecord.setUpdateTime(new Date());
                lyRecords.add(lyRecord);
            }
            if (CollUtil.isNotEmpty(lyRecords)) {
                log.info("LY---注单[{}]个", lyRecords.size());
                lyRecordService.batchInsertOrUpdate(lyRecords);
            }
        }
    }
}
