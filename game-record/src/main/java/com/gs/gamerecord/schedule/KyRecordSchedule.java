package com.gs.gamerecord.schedule;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gs.commons.entity.KyRecord;
import com.gs.commons.entity.PlatRecordControl;
import com.gs.commons.service.KyRecordService;
import com.gs.commons.service.PlatRecordControlService;
import com.gs.gamerecord.utils.KyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
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
public class KyRecordSchedule {

    @Value("${platform.KaiYuan.prefixURL}")
    public String prefixURL;//接口地址
    @Value("${platform.KaiYuan.recordURL}")
    public String recordURL;//拉单接口
    @Value("${platform.KaiYuan.agent}")
    public String agent;
    @Value("${platform.KaiYuan.aesKey}")
    public String aesKey;
    @Value("${platform.KaiYuan.md5Key}")
    public String md5Key;
    @Value("${platform.owner}")
    public String owner;

    @Autowired
    private PlatRecordControlService platRecordControlService;

    @Autowired
    private KyRecordService kyRecordService;

    @Async
    @Scheduled(cron = "0/20 * * * * ?")
    public void kyRecord() throws Exception {
        Date now = new Date();
        PlatRecordControl ky = platRecordControlService.getOne(
                new LambdaQueryWrapper<PlatRecordControl>()
                        .eq(PlatRecordControl::getStatus, 0)
                        .eq(PlatRecordControl::getPlatCode, "ky")
        );
        if (ky != null) {
            log.info("开元---拉单开始[{}]-[{}]", DateUtil.formatDateTime(ky.getBeginTime()), DateUtil.formatDateTime(ky.getEndTime()));
            getRecord(ky.getBeginTime(), ky.getEndTime());
            log.info("开元---拉单完成[{}]-[{}]", DateUtil.formatDateTime(ky.getBeginTime()), DateUtil.formatDateTime(ky.getEndTime()));
            // 如果当前时间大于结束时间，更新拉取时间范围
            if (DateUtil.compare(now, ky.getEndTime()) == 1) {
                platRecordControlService.update(
                        new LambdaUpdateWrapper<PlatRecordControl>()
                                .set(PlatRecordControl::getBeginTime, ky.getEndTime())
                                .set(PlatRecordControl::getEndTime, DateUtil.offsetMinute(ky.getEndTime(), 15))
                                .eq(PlatRecordControl::getPlatCode, "ky")
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
        String param = AESEncrypt(paramSb.toString(), aesKey);
        String key = SecureUtil.md5(agent + timestamp + this.md5Key);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append(this.recordURL).append("?").append("agent=").append(agent).append("&timestamp=").append(timestamp).append("&param=").append(param).append("&key=").append(key);
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
            List<KyRecord> kyRecords = new ArrayList<>();

//            List<>
            for (int i = 0; i < count; i++) {
                String account = accounts.getString(i);
                KyRecord kyRecord = new KyRecord();
                String ownerUsername = account.split("_")[1];//还需去掉字符串前两位
                //判断是不是本平台用户
                String subOwnerStr = ownerUsername.substring(0, 2);
                if(!StrUtil.equals(subOwnerStr, this.owner)){
                    continue;
                }
                String username = ownerUsername.substring(2);
                kyRecord.setUserName(username);
                kyRecord.setPlatUserName(account);
                kyRecord.setOrderNo(gameIds.getString(i));
                kyRecord.setGameId(kindID.getString(i));
                kyRecord.setGameName(KyConstants.GAME_NAME.getOrDefault(kyRecord.getGameId(), kyRecord.getGameId()));
                kyRecord.setEffectiveBet(cellScore.getBigDecimal(i));
                kyRecord.setAllBet(allBet.getBigDecimal(i));
                kyRecord.setProfit(profit.getBigDecimal(i));
                kyRecord.setGameStartTime(gameStartTime.getDate(i));
                kyRecord.setGameEndTime(gameEndTime.getDate(i));
                kyRecord.setSettleTime(gameEndTime.getDate(i));
                kyRecord.setCreateTime(new Date());
                kyRecord.setUpdateTime(new Date());
                kyRecords.add(kyRecord);
            }
            if (CollUtil.isNotEmpty(kyRecords)) {
                log.info("KY---注单[{}]个", kyRecords.size());
                kyRecordService.batchInsertOrUpdate(kyRecords);
            }
        }
    }

    public static String AESEncrypt(String value,String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] raw = key.getBytes("UTF-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
        String base64 = Base64.encode(encrypted);// 此处使用BASE64做转码
        return URLEncoder.encode(base64, "UTF-8");//URL加密
    }
}
