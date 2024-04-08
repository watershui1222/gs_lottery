package com.gs.gamerecord.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gs.commons.entity.HgRecord;
import com.gs.commons.entity.PlatRecordControl;
import com.gs.commons.entity.SbRecord;
import com.gs.commons.service.HgRecordService;
import com.gs.commons.service.PlatRecordControlService;
import com.gs.commons.service.SbRecordService;
import com.gs.commons.utils.AesUtils;
import com.gs.gamerecord.utils.HgConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 皇冠定时任务
 *
 * @author Administrator
 */
@Slf4j
@Component
public class SbRecordSchedule {

    @Value("${platform.ShaBa.operatorID}")
    public String operatorID;
    @Value("${platform.ShaBa.apiUrl}")
    public String apiUrl;
    @Value("${platform.ShaBa.vendorID}")
    public String vendorID;
    @Value("${platform.ShaBa.currencyID}")
    public String currencyID;//20=测试货币 13=CNY
    @Value("${platform.owner}")
    public String owner;

    @Autowired
    private PlatRecordControlService platRecordControlService;

    @Autowired
    private SbRecordService sbRecordService;


    @Scheduled(cron = "0 0/1 * * * ?")
    public void sbRecord() throws Exception {
        Date now = new Date();
        PlatRecordControl sb = platRecordControlService.getOne(
                new LambdaQueryWrapper<PlatRecordControl>()
                        .eq(PlatRecordControl::getStatus, 0)
                        .eq(PlatRecordControl::getPlatCode, "sb")
        );

        if (sb != null) {
            log.info("沙巴---拉单开始[{}]-[{}]", DateUtil.formatDateTime(sb.getBeginTime()), DateUtil.formatDateTime(sb.getEndTime()));
            /**
             * 昨天的  只能一天一天的查询 为防止每天的最后一段时间的单漏掉 这里只能获取昨天 和 今天的
             */
            Date yesterday = DateUtil.offsetHour(DateUtil.yesterday(), -12);//美东时间
            Date beginYes = DateUtil.beginOfDay(yesterday);
            Date endYes = DateUtil.endOfDay(yesterday);
            getRecord(beginYes, endYes);

            /**
             * 今天的
             */
            Date today = DateUtil.offsetHour(new Date(), -12);
            Date beginToday = DateUtil.beginOfDay(today);
            Date endToday = DateUtil.endOfDay(today);
            getRecord(beginToday, endToday);

            log.info("沙巴---拉单完成[{}]-[{}]", DateUtil.formatDateTime(sb.getBeginTime()), DateUtil.formatDateTime(sb.getEndTime()));
            // 如果当前时间大于结束时间，更新拉取时间范围
            if (DateUtil.compare(now, sb.getEndTime()) == 1) {
                platRecordControlService.update(
                        new LambdaUpdateWrapper<PlatRecordControl>()
                                .set(PlatRecordControl::getBeginTime, sb.getEndTime())
                                .set(PlatRecordControl::getEndTime, DateUtil.offsetMinute(sb.getEndTime(), 15))
                                .eq(PlatRecordControl::getPlatCode, "sb")
                );
            }
        }
    }

    /**
     * 拉取注单
     * @return
     */
    public void getRecord(Date beginTime, Date endTime){
        String apiUrlStr = this.apiUrl + "/GetBetDetailByTimeframe";
        String vendor_id = this.vendorID;//厂商标识符
        String end_date = DateUtil.format(endTime, "yyyy-MM-dd'T'HH:mm:ss");
        String start_date = DateUtil.format(beginTime, "yyyy-MM-dd'T'HH:mm:ss");
        int time_type = 2;
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("start_date", start_date);
        param.put("end_date", end_date);
        param.put("time_type", time_type);
        String result = HttpUtil.post(apiUrlStr, param);
        JSONObject resJSON = JSONObject.parseObject(result);
        int error_code = resJSON.getIntValue("error_code");
        if(error_code == 0){
            JSONObject data = resJSON.getJSONObject("Data");
            JSONArray betDetails = data.getJSONArray("BetDetails");
            if(CollUtil.isNotEmpty(betDetails)){
                List<SbRecord> list = new ArrayList<>();
                for (Object obj:betDetails) {
                    JSONObject recordJSON = (JSONObject) obj;
                    SbRecord record = new SbRecord();
                    //判断是不是本平台用户
                    String ownerUsername = recordJSON.getString("vendor_member_id");
                    String subOwnerStr = ownerUsername.substring(0, 2);
                    if(!StrUtil.equals(subOwnerStr, this.owner)){
                        continue;
                    }
                    String username = ownerUsername.substring(2);
                    record.setUserName(username);
                    record.setPlatUserName(ownerUsername);
                    record.setOrderNo(recordJSON.getString("trans_id"));
                    JSONArray sportName = recordJSON.getJSONArray("sportname");
                    String sportNameCN = sportName.stream().filter(t -> ((JSONObject) t).getString("lang").equals("cs")).map(t -> ((JSONObject) t).getString("name")).distinct().collect(Collectors.joining());
                    record.setGameName(sportNameCN);
                    JSONArray bettypename = recordJSON.getJSONArray("bettypename");
                    String bettypenameCN = bettypename.stream().filter(t -> ((JSONObject) t).getString("lang").equals("cs")).map(t -> ((JSONObject) t).getString("name")).distinct().collect(Collectors.joining());
                    record.setWtype(bettypenameCN);
                    JSONArray hometeamname = recordJSON.getJSONArray("hometeamname");
                    String hometeamnameCN = hometeamname.stream().filter(t -> ((JSONObject) t).getString("lang").equals("cs")).map(t -> ((JSONObject) t).getString("name")).distinct().collect(Collectors.joining());
                    record.setTnameHome(hometeamnameCN);
                    JSONArray awayteamname = recordJSON.getJSONArray("awayteamname");
                    String awayteamnameCN = awayteamname.stream().filter(t -> ((JSONObject) t).getString("lang").equals("cs")).map(t -> ((JSONObject) t).getString("name")).distinct().collect(Collectors.joining());
                    record.setTnameAway(awayteamnameCN);
                    JSONArray leaguename = recordJSON.getJSONArray("leaguename");
                    String leaguenameCN = leaguename.stream().filter(t -> ((JSONObject) t).getString("lang").equals("cs")).map(t -> ((JSONObject) t).getString("name")).distinct().collect(Collectors.joining());
                    record.setLeague(leaguenameCN);
                    record.setIoratio(recordJSON.getBigDecimal("odds"));
                    record.setEffectiveBet(recordJSON.getBigDecimal("stake"));
                    record.setAllBet(record.getEffectiveBet());
                    record.setProfit(recordJSON.getBigDecimal("winlost_amount"));
                    Date transaction_time = DateUtil.parse(recordJSON.getString("transaction_time"), "yyyy-MM-dd'T'HH:mm:ss.SSS");
                    record.setBetTime(DateUtil.offsetHour(transaction_time, 12));
                    record.setSettleStatus(0);//默认未结算
                    String settle = recordJSON.getString("settlement_time");
                    if(StrUtil.isNotBlank(settle) && !StrUtil.equals("settle", "null")){
                        Date settlement_time = DateUtil.parse(settle, "yyyy-MM-dd'T'HH:mm:ss.SSS");
                        record.setSettleTime(DateUtil.offsetHour(settlement_time, 12));
                        record.setSettleStatus(1);
                    }else{
                        continue;
                    }
                    String home_score = recordJSON.getString("home_score");
                    String away_score = recordJSON.getString("away_score");
                    record.setResultScore(home_score + ":" + away_score);
                    record.setParlaysub(recordJSON.getString("ParlayData"));
                    record.setRawData(recordJSON.toJSONString());
                    record.setResettlementinfo(recordJSON.getString("resettlementinfo"));
                    record.setIsLive(recordJSON.getInteger("islive"));
                    record.setCreateTime(new Date());
                    record.setUpdateTime(new Date());
                    list.add(record);
                }
                if(CollUtil.isNotEmpty(list)){
                    sbRecordService.batchInsertOrUpdate(list);
                }
            }
        }
    }
}
