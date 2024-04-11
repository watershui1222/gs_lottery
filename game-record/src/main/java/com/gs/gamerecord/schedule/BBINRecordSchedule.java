package com.gs.gamerecord.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gs.commons.entity.BbinRecord;
import com.gs.commons.entity.PlatRecordControl;
import com.gs.commons.service.BbinRecordService;
import com.gs.commons.service.PlatRecordControlService;
import com.gs.commons.utils.AesUtils;
import com.gs.gamerecord.utils.BBINConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BBIN定时任务
 *
 * @author Administrator
 */
@Slf4j
@Component
public class BBINRecordSchedule {

    @Value("${platform.BBIN.website}")
    public String website;
    @Value("${platform.BBIN.uppername}")
    public String uppername;
    @Value("${platform.BBIN.apiDomain}")
    public String apiDomain;
    @Value("${platform.owner}")
    public String owner;

    @Autowired
    private PlatRecordControlService platRecordControlService;

    @Autowired
    private BbinRecordService bbinRecordService;

    /**
     * BBIN视讯拉单任务
     * @throws Exception
     */
    @Scheduled(cron = "0/15 * * * * ?")
    public void bbinliveRecord() throws Exception {
        Date now = new Date();
        PlatRecordControl bbinlive = platRecordControlService.getOne(
                new LambdaQueryWrapper<PlatRecordControl>()
                        .eq(PlatRecordControl::getStatus, 0)
                        .eq(PlatRecordControl::getPlatCode, "bbinlive")
        );

        if (bbinlive != null) {
            log.info("BBIN视讯---拉单开始[{}]-[{}]", DateUtil.formatDateTime(bbinlive.getBeginTime()), DateUtil.formatDateTime(bbinlive.getEndTime()));
            //需要将时间转换成美东时间
            Date mdBegin = DateUtil.offsetHour(bbinlive.getBeginTime(), -12);
            Date mdEnd = DateUtil.offsetHour(bbinlive.getEndTime(), -12);
            mdEnd = DateUtil.offsetMinute(mdEnd, -5);
            getLiveRecord(mdBegin, mdEnd);
            log.info("BBIN视讯---拉单完成[{}]-[{}]", DateUtil.formatDateTime(bbinlive.getBeginTime()), DateUtil.formatDateTime(bbinlive.getEndTime()));
            // 如果当前时间大于结束时间，更新拉取时间范围
            if (DateUtil.compare(now, bbinlive.getEndTime()) == 1) {
                platRecordControlService.update(
                        new LambdaUpdateWrapper<PlatRecordControl>()
                                .set(PlatRecordControl::getBeginTime, bbinlive.getEndTime())
                                .set(PlatRecordControl::getEndTime, DateUtil.offsetMinute(bbinlive.getEndTime(), 5))
                                .eq(PlatRecordControl::getPlatCode, "bbinlive")
                );
            }
        }
    }

    /**
     * 拉取视讯注单
     * @return
     */
    public void getLiveRecord(Date startTime, Date endTime){
        //获得URL前需使用此接口获取用户session
        String apiUrl = this.apiDomain + "/WagersRecordBy3";
        String website = this.website;
        String action = "ModifiedTime";
        String uppername = this.uppername;
        String keyB = "U5fBm";
        String strA = RandomUtil.randomString(3);
        String date = DateUtil.format(endTime, "yyyy-MM-dd");//这里的date参数必须用endTime 日期串
        //这里至少-7 -12 不然很多dateerror 因为如果使用ModifiedTime查询 BBIN不支持大于2分钟前的数据检索  而区间必须是5min
        String endtime = DateUtil.format(endTime, "HH:mm:ss");//当action为ModifiedTime，无法捞取最近2分钟内的下注记录。
        if(StrUtil.equals("00:00:00", endtime)){
            //无法跨天
            endtime = "23:59:59";
            date = DateUtil.format(DateUtil.offsetSecond(endTime, -1), "yyyy-MM-dd");
        }
        String starttime = DateUtil.format(DateUtil.offsetMinute(endTime, -5), "HH:mm:ss");//当action为ModifiedTime，是捞取带入的区间内被异动的纪录(区间限定5分钟且无法捞取7天前)。
        Date nowMd = DateUtil.offsetHour(DateUtil.date(), -12);
        String strB = AesUtils.MD5(website + keyB + DateUtil.format(nowMd, "yyyyMMdd"));//这里加密必须用当前美东时间
        String strC = RandomUtil.randomString(7);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("action", action);
        param.put("uppername", uppername);
        param.put("date", date);
        param.put("starttime", starttime);
        param.put("endtime", endtime);
        param.put("key", key.toLowerCase());
        log.info("BBIN eleRecord param:{}", param);
        String result = HttpUtil.post(apiUrl, param);
        log.info("BBIN eleRecord result:{}", result);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONArray data = resJson.getJSONArray("data");
        if(resJson.getBoolean("result") && CollUtil.isNotEmpty(data)){
            List<BbinRecord> list = new ArrayList<>();
            for (Object obj : data) {
                JSONObject recordJson = (JSONObject) obj;
                BbinRecord record = new BbinRecord();
                //判断是不是本平台用户
                String ownerUsername = recordJson.getString("UserName");
                String subOwnerStr = ownerUsername.substring(0, 2);
                if(!StrUtil.equals(subOwnerStr, this.owner)){
                    continue;
                }
                String username = ownerUsername.substring(2);
                record.setUserName(username);
                record.setPlatUserName(recordJson.getString("UserName"));
                record.setOrderNo(recordJson.getString("WagersID"));
                record.setGameId(recordJson.getString("GameType"));
                record.setGameName(BBINConstants.GAME_NAME.getOrDefault(record.getGameId(),record.getGameId()));
                record.setEffectiveBet(recordJson.getBigDecimal("Commissionable"));
                record.setAllBet(recordJson.getBigDecimal("BetAmount"));
                record.setProfit(recordJson.getBigDecimal("Payoff"));
                record.setWagersDate(DateUtil.parse(recordJson.getString("WagersDate")));
                record.setBetTime(DateUtil.offsetHour(record.getWagersDate(), 12));
                record.setModifiedDate(DateUtil.parse(recordJson.getString("ModifiedDate")));
                record.setSettleTime(DateUtil.offsetHour(record.getModifiedDate(), 12));
                record.setResultStatus(recordJson.getString("Result"));
                record.setOpenResult(recordJson.getString("ResultType"));
                record.setCard(recordJson.getString("Card"));
                record.setSerialId(recordJson.getString("SerialID"));
                record.setRoundNo(recordJson.getString("RoundNo"));
                record.setWagerDetail(recordJson.getString("WagerDetail"));
                record.setGameType(1);
                record.setCreateTime(DateUtil.date());
                record.setUpdateTime(DateUtil.date());
                list.add(record);
            }
            if(CollUtil.isNotEmpty(list)){
                bbinRecordService.batchInsertOrUpdate(list);
            }
        }
    }

    /**
     * bbin电子拉单任务
     * @throws Exception
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void bbinelegameRecord() throws Exception {
        Date now = new Date();
        PlatRecordControl bbinele = platRecordControlService.getOne(
                new LambdaQueryWrapper<PlatRecordControl>()
                        .eq(PlatRecordControl::getStatus, 0)
                        .eq(PlatRecordControl::getPlatCode, "bbinele")
        );

        if (bbinele != null) {
            log.info("BBIN电子---拉单开始[{}]-[{}]", DateUtil.formatDateTime(bbinele.getBeginTime()), DateUtil.formatDateTime(bbinele.getEndTime()));
            Date mdBegin = DateUtil.offsetHour(bbinele.getBeginTime(), -12);
            Date mdEnd = DateUtil.offsetHour(bbinele.getEndTime(), -12);
            mdEnd = DateUtil.offsetMinute(mdEnd, -5);
            getEleGameRecord(mdBegin, mdEnd);
            log.info("BBIN电子---拉单完成[{}]-[{}]", DateUtil.formatDateTime(bbinele.getBeginTime()), DateUtil.formatDateTime(bbinele.getEndTime()));
            // 如果当前时间大于结束时间，更新拉取时间范围
            if (DateUtil.compare(now, bbinele.getEndTime()) == 1) {
                platRecordControlService.update(
                        new LambdaUpdateWrapper<PlatRecordControl>()
                                .set(PlatRecordControl::getBeginTime, bbinele.getEndTime())
                                .set(PlatRecordControl::getEndTime, DateUtil.offsetMinute(bbinele.getEndTime(), 5))
                                .eq(PlatRecordControl::getPlatCode, "bbinele")
                );
            }
        }
    }

    /**
     * 拉取电子游戏注单
     * @return
     */
    public void getEleGameRecord(Date startTime, Date endTime){
        //获得URL前需使用此接口获取用户session
        //(需强制带入，值:1、2、3、5)
        JSONArray data1 = eleGameRecordApi("1", startTime, endTime);
        JSONArray data2 = eleGameRecordApi("2", startTime, endTime);
        JSONArray data3 = eleGameRecordApi("3", startTime, endTime);
        JSONArray data5 = eleGameRecordApi("5", startTime, endTime);
        data1.addAll(data2);
        data1.addAll(data3);
        data1.addAll(data5);
        //需要将这几个结果集拼接成一个
        if(CollUtil.isNotEmpty(data1)){
            List<BbinRecord> list = new ArrayList<>();
            for (Object obj : data1) {
                JSONObject recordJson = (JSONObject) obj;
                BbinRecord record = new BbinRecord();
                //判断是不是本平台用户
                String ownerUsername = recordJson.getString("UserName");
                String subOwnerStr = ownerUsername.substring(0, 2);
                if(!StrUtil.equals(subOwnerStr, this.owner)){
                    continue;
                }
                String username = ownerUsername.substring(2);
                record.setUserName(username);
                record.setPlatUserName(recordJson.getString("UserName"));
                record.setOrderNo(recordJson.getString("WagersID"));
                record.setGameId(recordJson.getString("GameType"));
                record.setGameName(BBINConstants.GAME_NAME.getOrDefault(record.getGameId(),record.getGameId()));
                record.setEffectiveBet(recordJson.getBigDecimal("Commissionable"));
                record.setAllBet(recordJson.getBigDecimal("BetAmount"));
                record.setProfit(recordJson.getBigDecimal("Payoff"));
                record.setWagersDate(DateUtil.parse(recordJson.getString("WagersDate")));
                record.setBetTime(DateUtil.offsetHour(record.getWagersDate(), 12));
                record.setModifiedDate(DateUtil.parse(recordJson.getString("ModifiedDate")));
                record.setSettleTime(DateUtil.offsetHour(record.getModifiedDate(), 12));
                record.setResultStatus(recordJson.getString("Result"));
                record.setOpenResult(recordJson.getString("ResultType"));
                record.setCard(recordJson.getString("Card"));
                record.setSerialId(recordJson.getString("SerialID"));
                record.setRoundNo(recordJson.getString("RoundNo"));
                record.setWagerDetail(recordJson.getString("WagerDetail"));
                record.setGameType(2);
                record.setCreateTime(DateUtil.date());
                record.setUpdateTime(DateUtil.date());
                list.add(record);
            }
            if(CollUtil.isNotEmpty(list)){
                bbinRecordService.batchInsertOrUpdate(list);
            }
        }
    }

    /**
     * (需强制带入，值:1、2、3、5)
     * @param subgamekind
     * @return
     */
    public JSONArray eleGameRecordApi(String subgamekind, Date startTime, Date endTime){
        //获得URL前需使用此接口获取用户session
        String apiUrl = this.apiDomain + "/WagersRecordBy5";
        String website = this.website;
        String action = "ModifiedTime";
        String uppername = this.uppername;
        String keyB = "U5fBm";
        String strA = RandomUtil.randomString(3);
        String date = DateUtil.format(endTime, "yyyy-MM-dd");//这里的date参数必须用endTime 日期串
        //这里至少-7 -12 不然很多dateerror 因为如果使用ModifiedTime查询 BBIN不支持大于2分钟前的数据检索
        String endtime = DateUtil.format(endTime, "HH:mm:ss");//当action为ModifiedTime，无法捞取最近2分钟内的下注记录。
        if(StrUtil.equals("00:00:00", endtime)){
            //无法跨天
            endtime = "23:59:59";
            date = DateUtil.format(DateUtil.offsetSecond(endTime, -1), "yyyy-MM-dd");
        }
        String starttime = DateUtil.format(DateUtil.offsetMinute(endTime, -5), "HH:mm:ss");//当action为ModifiedTime，是捞取带入的区间内被异动的纪录(区间限定5分钟且无法捞取7天前)。
        Date nowMd = DateUtil.offsetHour(DateUtil.date(), -12);
        String strB = AesUtils.MD5(website + keyB + DateUtil.format(nowMd, "yyyyMMdd"));
        String strC = RandomUtil.randomString(7);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("action", action);
        param.put("uppername", uppername);
        param.put("date", date);
        param.put("starttime", starttime);
        param.put("endtime", endtime);
        param.put("subgamekind", subgamekind);
        param.put("key", key.toLowerCase());
        log.info("BBIN eleRecord param:{}", param);
        String result = HttpUtil.post(apiUrl, param);
        //捕鱼会出现一些{"result":false,"data":{"Code":"40014","Message":"Date Error"}}的错误,无视即可
        log.info("BBIN eleRecord result:{}", result);
        JSONObject resJson = JSONObject.parseObject(result);
        if(resJson.getBoolean("result")){
            JSONArray data = resJson.getJSONArray("data");
            return CollUtil.isNotEmpty(data) ? data : new JSONArray();
        }
        return new JSONArray();
    }

    /**
     * bbin捕鱼拉单任务
     * @throws Exception
     */
    @Scheduled(cron = "0/20 * * * * ?")
    public void bbinfishgameRecord() throws Exception {
        Date now = new Date();
        PlatRecordControl bbinfish = platRecordControlService.getOne(
                new LambdaQueryWrapper<PlatRecordControl>()
                        .eq(PlatRecordControl::getStatus, 0)
                        .eq(PlatRecordControl::getPlatCode, "bbinfish")
        );

        if (bbinfish != null) {
            log.info("BBIN捕鱼---拉单开始[{}]-[{}]", DateUtil.formatDateTime(bbinfish.getBeginTime()), DateUtil.formatDateTime(bbinfish.getEndTime()));
            Date mdBegin = DateUtil.offsetHour(bbinfish.getBeginTime(), -12);
            Date mdEnd = DateUtil.offsetHour(bbinfish.getEndTime(), -12);
            mdEnd = DateUtil.offsetMinute(mdEnd, -10);
            getFishRecord(mdBegin, mdEnd);
            log.info("BBIN捕鱼---拉单完成[{}]-[{}]", DateUtil.formatDateTime(bbinfish.getBeginTime()), DateUtil.formatDateTime(bbinfish.getEndTime()));
            // 如果当前时间大于结束时间，更新拉取时间范围
            if (DateUtil.compare(now, bbinfish.getEndTime()) == 1) {
                platRecordControlService.update(
                        new LambdaUpdateWrapper<PlatRecordControl>()
                                .set(PlatRecordControl::getBeginTime, bbinfish.getEndTime())
                                .set(PlatRecordControl::getEndTime, DateUtil.offsetMinute(bbinfish.getEndTime(), 5))
                                .eq(PlatRecordControl::getPlatCode, "bbinfish")
                );
            }
        }
    }

    /**
     * 拉取捕鱼大师注单
     * @return
     */
    public void getFishRecord(Date beginTime, Date endTime){
        String apiUrl = this.apiDomain + "/WagersRecordBy38";
        String website = this.website;
        String action = "ModifiedTime";
        String uppername = this.uppername;
        String keyB = "U5fBm";
        String strA = RandomUtil.randomString(3);
        String date = DateUtil.format(endTime, "yyyy-MM-dd");//这里的date参数必须用endTime 日期串
        //这里至少-7 -12 不然很多dateerror 因为如果使用ModifiedTime查询 BBIN不支持大于2分钟前的数据检索
        String endtime = DateUtil.format(endTime, "HH:mm:ss");//当action为ModifiedTime，无法捞取最近2分钟内的下注记录。
        if(StrUtil.equals("00:00:00", endtime)){
            //无法跨天
            endtime = "23:59:59";
            date = DateUtil.format(DateUtil.offsetSecond(endTime, -1), "yyyy-MM-dd");
        }
        String starttime = DateUtil.format(DateUtil.offsetMinute(endTime, -5), "HH:mm:ss");//当action为ModifiedTime，是捞取带入的区间内被异动的纪录(区间限定5分钟且无法捞取7天前)。
        Date nowMd = DateUtil.offsetHour(DateUtil.date(), -12);
        String strB = AesUtils.MD5(website + keyB + DateUtil.format(nowMd, "yyyyMMdd"));
        String strC = RandomUtil.randomString(7);
        String key = strA + strB + strC;
        JSONObject param = new JSONObject();
        param.put("website", website);
        param.put("action", action);
        param.put("uppername", uppername);
        param.put("date", date);
        param.put("starttime", starttime);
        param.put("endtime", endtime);
        param.put("key", key.toLowerCase());
        log.info("BBIN fishRecord param:{}", param);
        String result = HttpUtil.post(apiUrl, param);
        log.info("BBIN fishRecord result:{}", result);
        JSONObject resJson = JSONObject.parseObject(result);
        JSONArray data = resJson.getJSONArray("data");
        if(CollUtil.isNotEmpty(data)){
            List<BbinRecord> list = new ArrayList<>();
            for (Object obj : data) {
                JSONObject recordJson = (JSONObject) obj;
                BbinRecord record = new BbinRecord();
                record.setUserName(recordJson.getString("UserName"));
                record.setPlatUserName(recordJson.getString("UserName"));
                record.setOrderNo(recordJson.getString("WagersID"));
                record.setGameId(recordJson.getString("GameType"));
                record.setGameName(BBINConstants.GAME_NAME.getOrDefault(record.getGameId(),record.getGameId()));
                record.setEffectiveBet(recordJson.getBigDecimal("Commissionable"));
                record.setAllBet(recordJson.getBigDecimal("BetAmount"));
                record.setProfit(recordJson.getBigDecimal("Payoff"));
                record.setWagersDate(DateUtil.parse(recordJson.getString("WagersDate")));
                record.setBetTime(DateUtil.offsetHour(record.getWagersDate(), 12));
                record.setModifiedDate(DateUtil.parse(recordJson.getString("ModifiedDate")));
                record.setSettleTime(DateUtil.offsetHour(record.getModifiedDate(), 12));
                record.setResultStatus(recordJson.getString("Result"));
                record.setOpenResult(recordJson.getString("ResultType"));
                record.setCard(recordJson.getString("Card"));
                record.setSerialId(recordJson.getString("SerialID"));
                record.setRoundNo(recordJson.getString("RoundNo"));
                record.setWagerDetail(recordJson.getString("WagerDetail"));
                record.setGameType(3);
                record.setCreateTime(DateUtil.date());
                record.setUpdateTime(DateUtil.date());
                list.add(record);
            }
            if(CollUtil.isNotEmpty(list)){
                bbinRecordService.batchInsertOrUpdate(list);
            }
        }
    }
}
