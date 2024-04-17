package com.gs.gamerecord.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gs.commons.entity.PlatRecordControl;
import com.gs.commons.entity.SbRecord;
import com.gs.commons.service.PlatRecordControlService;
import com.gs.commons.service.SbRecordService;
import com.gs.gamerecord.utils.SbConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


    @Scheduled(cron = "0/30 * * * * ?")
    public void sbRecord() throws Exception {
        Date now = new Date();
        PlatRecordControl sb = platRecordControlService.getOne(
                new LambdaQueryWrapper<PlatRecordControl>()
                        .eq(PlatRecordControl::getStatus, 0)
                        .eq(PlatRecordControl::getPlatCode, "sb")
        );

        if (sb != null) {
            log.info("沙巴---拉单开始[{}]-[{}]", DateUtil.formatDateTime(sb.getBeginTime()), DateUtil.formatDateTime(sb.getEndTime()));
            Date beginToday = DateUtil.beginOfDay(sb.getBeginTime());
            Date endToday = DateUtil.endOfDay(sb.getEndTime());
            getRecord(beginToday, endToday);

            log.info("沙巴---拉单完成[{}]-[{}]", DateUtil.formatDateTime(sb.getBeginTime()), DateUtil.formatDateTime(sb.getEndTime()));
            // 如果当前时间大于结束时间，更新拉取时间范围
            if (DateUtil.compare(now, sb.getEndTime()) == 1) {
                platRecordControlService.update(
                        new LambdaUpdateWrapper<PlatRecordControl>()
                                .set(PlatRecordControl::getBeginTime, sb.getEndTime())
                                .set(PlatRecordControl::getEndTime, DateUtil.offsetHour(sb.getEndTime(), 1))
                                .eq(PlatRecordControl::getPlatCode, "sb")
                );

//                platRecordControlService.update(
//                        new LambdaUpdateWrapper<PlatRecordControl>()
//                                .set(PlatRecordControl::getBeginTime, DateUtil.beginOfDay(now))
//                                .set(PlatRecordControl::getEndTime, DateUtil.endOfDay(now))
//                                .eq(PlatRecordControl::getPlatCode, "sb")
//                );
            }
        }
    }

    /**
     * 拉取注单
     *
     * @return
     */
    public void getRecord(Date beginTime, Date endTime) {
        String apiUrlStr = this.apiUrl + "/GetBetDetailByTimeframe";
        String vendor_id = this.vendorID;//厂商标识符
        String end_date = DateUtil.format(endTime, "yyyy-MM-dd'T'HH:mm:ss");
        String start_date = DateUtil.format(beginTime, "yyyy-MM-dd'T'HH:mm:ss");
        int time_type = 1;
        JSONObject param = new JSONObject();
        param.put("vendor_id", vendor_id);
        param.put("start_date", start_date);
        param.put("end_date", end_date);
        param.put("time_type", time_type);
        String result = HttpUtil.post(apiUrlStr, param);
        JSONObject resJSON = JSONObject.parseObject(result);
        int error_code = resJSON.getIntValue("error_code");
        if (error_code == 0) {
            JSONObject data = resJSON.getJSONObject("Data");
            JSONArray betDetails = data.getJSONArray("BetDetails");
            if (CollUtil.isNotEmpty(betDetails)) {
                List<SbRecord> list = new ArrayList<>();
                for (int i = 0; i < betDetails.size(); i++) {
                    JSONObject recordJSON = betDetails.getJSONObject(i);
                    SbRecord record = new SbRecord();
                    //判断是不是本平台用户
                    String ownerUsername = recordJSON.getString("vendor_member_id");
                    String subOwnerStr = ownerUsername.substring(0, 2);
                    if (!StrUtil.equals(subOwnerStr, this.owner)) {
                        continue;
                    }
                    String username = ownerUsername.substring(2);
                    record.setUserName(username);
                    record.setPlatUserName(ownerUsername);
                    record.setOrderNo(recordJSON.getString("trans_id"));
                    record.setIsLive(recordJSON.getInteger("islive"));
                    Date matchDatetime = recordJSON.getDate("match_datetime");
                    record.setMatchDatetime(DateUtil.offsetHour(matchDatetime, 12));
                    record.setBetContent(SbConstants.getBetContent(recordJSON));
                    JSONArray bettypename = recordJSON.getJSONArray("bettypename");
                    String bettypenameCN = SbConstants.getCnName(bettypename);
                    String betType = recordJSON.getString("bet_type");
                    // 29=串关
                    if (StringUtils.equals("29", betType)) {
                        record.setGameName(bettypenameCN);
                        record.setWtype(bettypenameCN);
                        JSONArray parlayData = recordJSON.getJSONArray("ParlayData");
                        for (int i1 = 0; i1 < parlayData.size(); i1++) {
                            JSONObject jsonObject = parlayData.getJSONObject(i1);
                            jsonObject.put("betContent", SbConstants.getBetContent(jsonObject));
                        }
                        record.setParlaynum(parlayData.size());
                        record.setParlaysub(JSON.toJSONString(parlayData));
                    } else {
                        JSONArray sportName = recordJSON.getJSONArray("sportname");
                        record.setGameName(SbConstants.getCnName(sportName));
                        JSONArray hometeamname = recordJSON.getJSONArray("hometeamname");
                        record.setTnameHome(SbConstants.getCnName(hometeamname));
                        JSONArray awayteamname = recordJSON.getJSONArray("awayteamname");
                        record.setTnameAway(SbConstants.getCnName(awayteamname));
                        JSONArray leaguename = recordJSON.getJSONArray("leaguename");
                        record.setLeague(SbConstants.getCnName(leaguename));
                        String home_score = recordJSON.getString("home_score");
                        String away_score = recordJSON.getString("away_score");
                        if (StringUtils.isNotBlank(home_score) && StringUtils.isNotBlank(away_score)) {
                            record.setScore(home_score + ":" + away_score);
                        }
                        record.setWtype(record.getIsLive() == 1 ? "滚球" : "");
                        record.setRtype(bettypenameCN);
                        record.setOddsFormat(recordJSON.getString("hdp"));
                    }
                    record.setIoratio(recordJSON.getBigDecimal("odds"));
                    record.setEffectiveBet(recordJSON.getBigDecimal("stake"));
                    record.setAllBet(recordJSON.getBigDecimal("stake"));
                    record.setProfit(recordJSON.getBigDecimal("winlost_amount"));
                    Date transaction_time = DateUtil.parse(recordJSON.getString("transaction_time"), "yyyy-MM-dd'T'HH:mm:ss");
                    record.setBetTime(DateUtil.offsetHour(transaction_time, 12));
                    String settle = recordJSON.getString("settlement_time");
                    if (StrUtil.isNotBlank(settle)) {
                        // 已结算
                        Date settlement_time = DateUtil.parse(settle, "yyyy-MM-dd'T'HH:mm:ss");
                        record.setSettleTime(DateUtil.offsetHour(settlement_time, 12));
                    } else {

                    }
                    record.setResultStatus(recordJSON.getString("ticket_status"));
                    record.setRawData(recordJSON.toJSONString());
                    record.setResettlementinfo(recordJSON.getString("resettlementinfo"));
                    record.setCreateTime(new Date());
                    record.setUpdateTime(new Date());
                    list.add(record);
                }
                if (CollUtil.isNotEmpty(list)) {
                    sbRecordService.batchInsertOrUpdate(list);
                }
            }
        }
    }
}
