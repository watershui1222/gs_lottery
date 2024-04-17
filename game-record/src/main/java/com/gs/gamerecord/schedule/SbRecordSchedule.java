package com.gs.gamerecord.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gs.commons.entity.PlatRecordControl;
import com.gs.commons.entity.SbRecord;
import com.gs.commons.service.PlatRecordControlService;
import com.gs.commons.service.SbRecordService;
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
            Date beginToday = DateUtil.beginOfDay(sb.getBeginTime());
            Date endToday = DateUtil.endOfDay(sb.getEndTime());
            getRecord(beginToday, endToday);

            log.info("沙巴---拉单完成[{}]-[{}]", DateUtil.formatDateTime(sb.getBeginTime()), DateUtil.formatDateTime(sb.getEndTime()));
            // 如果当前时间大于结束时间，更新拉取时间范围
            if (DateUtil.compare(now, sb.getEndTime()) == 1) {
                platRecordControlService.update(
                        new LambdaUpdateWrapper<PlatRecordControl>()
                                .set(PlatRecordControl::getBeginTime, DateUtil.beginOfDay(now))
                                .set(PlatRecordControl::getEndTime, DateUtil.endOfDay(now))
                                .eq(PlatRecordControl::getPlatCode, "sb")
                );
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
                    JSONArray bettypename = recordJSON.getJSONArray("bettypename");
                    String bettypenameCN = getCnName(bettypename);
                    String betType = recordJSON.getString("bet_type");
                    // 29=串关
                    if (!StringUtils.equals("29", betType)) {
                        JSONArray sportName = recordJSON.getJSONArray("sportname");
                        record.setGameName(getCnName(sportName));
                        JSONArray hometeamname = recordJSON.getJSONArray("hometeamname");
                        record.setTnameHome(getCnName(hometeamname));
                        JSONArray awayteamname = recordJSON.getJSONArray("awayteamname");
                        record.setTnameAway(getCnName(awayteamname));
                        JSONArray leaguename = recordJSON.getJSONArray("leaguename");
                        record.setLeague(getCnName(leaguename));
                        String home_score = recordJSON.getString("home_score");
                        String away_score = recordJSON.getString("away_score");
                        record.setResultScore(home_score + ":" + away_score);
                        record.setWtype(record.getIsLive() == 1 ? "滚球" : "其他" + bettypenameCN);
                        record.setRtype(bettypenameCN);
                    } else {
                        record.setGameName(bettypenameCN);
                        record.setWtype(bettypenameCN);
                    }
                    record.setIoratio(recordJSON.getBigDecimal("odds"));
                    record.setEffectiveBet(recordJSON.getBigDecimal("stake"));
                    record.setAllBet(record.getEffectiveBet());
                    record.setProfit(recordJSON.getBigDecimal("winlost_amount"));
                    Date transaction_time = DateUtil.parse(recordJSON.getString("transaction_time"), "yyyy-MM-dd'T'HH:mm:ss.SSS");
                    record.setBetTime(DateUtil.offsetHour(transaction_time, 12));
                    record.setSettleStatus(0);//默认未结算
                    String settle = recordJSON.getString("settlement_time");
                    if (StrUtil.isNotBlank(settle) && !StrUtil.equals("settle", "null")) {
                        Date settlement_time = DateUtil.parse(settle, "yyyy-MM-dd'T'HH:mm:ss.SSS");
                        record.setSettleTime(DateUtil.offsetHour(settlement_time, 12));
                        record.setSettleStatus(1);
                    } else {
                        continue;
                    }
                    record.setResultStatus(recordJSON.getString("ticket_status"));
                    record.setParlaysub(recordJSON.getString("ParlayData"));
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


    private String getCnName(JSONArray array) {
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
