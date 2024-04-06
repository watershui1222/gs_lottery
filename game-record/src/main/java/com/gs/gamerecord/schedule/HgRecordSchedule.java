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
import com.gs.commons.service.HgRecordService;
import com.gs.commons.service.PlatRecordControlService;
import com.gs.commons.utils.AesUtils;
import com.gs.gamerecord.utils.HgConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 皇冠定时任务
 *
 * @author Administrator
 */
@Slf4j
@Component
public class HgRecordSchedule {

    @Value("${platform.ShaBa.agId}")
    public String agId;
    @Value("${platform.ShaBa.agPassword}")
    public String agPassword;
    @Value("${platform.ShaBa.agName}")
    public String agName;
    @Value("${platform.ShaBa.secretKey}")
    public String secretKey;
    @Value("${platform.ShaBa.apiUrl}")
    public String apiUrl;

    @Autowired
    private PlatRecordControlService platRecordControlService;

    @Autowired
    private HgRecordService hgRecordService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void hgRecord() throws Exception {
        Date now = new Date();
        PlatRecordControl hg = platRecordControlService.getOne(
                new LambdaQueryWrapper<PlatRecordControl>()
                        .eq(PlatRecordControl::getStatus, 0)
                        .eq(PlatRecordControl::getPlatCode, "hg")
        );

        if (hg != null) {
            log.info("皇冠---拉单开始[{}]-[{}]", DateUtil.formatDateTime(hg.getBeginTime()), DateUtil.formatDateTime(hg.getEndTime()));
            aLLWager(hg.getBeginTime(), hg.getEndTime());
            log.info("皇冠---拉单完成[{}]-[{}]", DateUtil.formatDateTime(hg.getBeginTime()), DateUtil.formatDateTime(hg.getEndTime()));
            // 如果当前时间大于结束时间，更新拉取时间范围
            if (DateUtil.compare(now, hg.getEndTime()) == 1) {
                platRecordControlService.update(
                        new LambdaUpdateWrapper<PlatRecordControl>()
                                .set(PlatRecordControl::getBeginTime, hg.getEndTime())
                                .set(PlatRecordControl::getEndTime, DateUtil.offsetMinute(hg.getEndTime(), 1560))
                                .eq(PlatRecordControl::getPlatCode, "hg")
                );
            }
        }
    }

    /**
     * 拉注单
     * @return
     * @throws Exception
     */
    public void aLLWager(Date startTime, Date endTime){
        String token = agLogin();
        List<HgRecord> recordList = new ArrayList<>();
        if(StrUtil.isNotBlank(token)){

            String dateEnd = DateUtil.format(endTime, "yyyy-MM-dd hh:mm:ss");
            String dateStart = DateUtil.format(startTime, "yyyy-MM-dd hh:mm:ss");
            int page = 1;
            int wagerTotalpage = 1;

            do{
                JSONObject param = new JSONObject();
                JSONObject request = new JSONObject();
                request.put("method", "ALLWager");
                request.put("settle", "1");
                request.put("dateStart", dateStart);
                request.put("dateEnd", dateEnd);
                request.put("page", page);
                request.put("token", token);
                request.put("timestamp", DateUtil.current());
                request.put("langx", "zh-cn");
                String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey,false);
                param.put("Request", requestStr);
                param.put("Method", "ALLWager");
                param.put("AGID", agId);
                HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
                String resStr = res.body();
                if(!JSONUtil.isTypeJSON(resStr)){
                    String result = AesUtils.AESDecrypt(resStr, this.secretKey);
                    JSONObject resultJson = JSONObject.parseObject(result);
                    String respcode = resultJson.getString("respcode");
                    if(StrUtil.equals(respcode, "0000")){
                        wagerTotalpage = resultJson.getInteger("wager_totalpage");
                        JSONArray wagerDateArr = resultJson.getJSONArray("wager_data");
                        for (Object obj:wagerDateArr) {
                            JSONObject wager = (JSONObject) obj;
                            HgRecord hgRecord = new HgRecord();
                            hgRecord.setCreateTime(DateUtil.date());
                            hgRecord.setUpdateTime(DateUtil.date());
                            hgRecord.setGameName(HgConstants.GAME_NAME.getOrDefault(wager.getString("gtype"), wager.getString("gtype")));
                            hgRecord.setAllBet(wager.getBigDecimal("gold"));
                            hgRecord.setEffectiveBet(wager.getBigDecimal("members_vgold"));
                            hgRecord.setIoratio(wager.getBigDecimal("ioratio"));
                            hgRecord.setLeague(wager.getString("league"));
                            hgRecord.setOrderNo(wager.getString("id"));
                            hgRecord.setProfit(wager.getBigDecimal("vgold"));
                            hgRecord.setParlaynum(wager.getInteger("parlaynum"));
                            hgRecord.setBetTime(DateUtil.parse(wager.getString("adddate")));
                            hgRecord.setParlaysub(wager.getString("parlaysub"));
                            hgRecord.setLeague(wager.getString("league"));
                            hgRecord.setRtype(wager.getString("rtype"));
                            hgRecord.setOddsFormat(wager.getString("oddsFormat"));
                            hgRecord.setWtype(wager.getString("wtype"));
                            hgRecord.setUserName(wager.getString("username"));
                            hgRecord.setTnameAway(wager.getString("tname_away"));
                            hgRecord.setTnameHome(wager.getString("tname_home"));
                            hgRecord.setPlatUserName(wager.getString("username"));
                            hgRecord.setStrong(wager.getString("strong"));
                            hgRecord.setResultScore(wager.getString("result_score"));
                            hgRecord.setResultStatus(wager.getString("result"));
                            hgRecord.setSettleTime(DateUtil.parse(wager.getString("resultdate")));
                            hgRecord.setSettleStatus(wager.getIntValue("settle"));
                            hgRecord.setRawData(wager.toJSONString());
                            recordList.add(hgRecord);
                        }
                    }
                }
                page++;
            }while(page <= wagerTotalpage);
            if (CollUtil.isNotEmpty(recordList)) {
                hgRecordService.batchInsertOrUpdate(recordList);
            }
        }
    }

    public String agLogin() {
        String tokenKey = "HGtokenkey";
        if(redisTemplate.hasKey(tokenKey)){
            return redisTemplate.opsForValue().get(tokenKey);
        }
        String token = "";
        JSONObject param = new JSONObject();
        JSONObject request = new JSONObject();
        request.put("username", agName);
        request.put("password", agPassword);
        request.put("timestamp", DateUtil.current());
        String requestStr = AesUtils.AESEncrypt(request.toJSONString(), this.secretKey, false);
        param.put("Request", requestStr);
        param.put("Method", "AGLogin");
        param.put("AGID", agId);
        HttpResponse res = HttpUtil.createPost(this.apiUrl).contentType("application/json").charset("utf-8").body(param.toJSONString()).execute();
        String encryptRes = res.body();
        String result = AesUtils.AESDecrypt(encryptRes, this.secretKey);
        log.info("皇冠 aglogin接口 param= " + param + " result = " + result);
        JSONObject resultJS = JSONObject.parseObject(result);
        if(StrUtil.equals(resultJS.getString("respcode"), "0000")){
            token = resultJS.getString("token");
            redisTemplate.opsForValue().set(tokenKey, token, 36, TimeUnit.HOURS);
            return token;
        }
        return token;
    }
}
