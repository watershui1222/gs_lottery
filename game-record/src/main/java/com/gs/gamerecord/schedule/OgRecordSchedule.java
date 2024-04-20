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
import com.gs.commons.service.OgRecordService;
import com.gs.commons.service.PlatRecordControlService;
import com.gs.commons.utils.AesUtils;
import com.gs.gamerecord.utils.LyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OG拉单
 *
 * @author Administrator
 */
@Slf4j
@Component
public class OgRecordSchedule {

    @Value("${platform.OG.publicKey}")
    private String publicKey;
    @Value("${platform.OG.privateKey}")
    private String privateKey;
    @Value("${platform.OG.operator}")
    private String operator;
    @Value("${platform.OG.apiDomain}")
    private String apiDomain;
    @Value("${platform.OG.betLimit}")
    private String betLimit;
    @Value("${platform.OG.lobbyId}")
    private String lobbyId;
    @Value("${platform.owner}")
    public String owner;

    @Autowired
    private PlatRecordControlService platRecordControlService;
    @Autowired
    private OgRecordService ogRecordService;
    @Autowired
    private StringRedisTemplate redisTemplateedisTemplate;

    @Async
    @Scheduled(cron = "0/20 * * * * ?")
    public void lyRecord() throws Exception {
        Date now = new Date();
        PlatRecordControl og = platRecordControlService.getOne(
                new LambdaQueryWrapper<PlatRecordControl>()
                        .eq(PlatRecordControl::getStatus, 0)
                        .eq(PlatRecordControl::getPlatCode, "og")
        );
        if (og != null) {
            log.info("OG---拉单开始[{}]-[{}]", DateUtil.formatDateTime(og.getBeginTime()), DateUtil.formatDateTime(og.getEndTime()));
            getRecord(og.getBeginTime(), og.getEndTime());
            log.info("OG---拉单完成[{}]-[{}]", DateUtil.formatDateTime(og.getBeginTime()), DateUtil.formatDateTime(og.getEndTime()));
            // 如果当前时间大于结束时间，更新拉取时间范围
            if (DateUtil.compare(now, og.getEndTime()) == 1) {
                platRecordControlService.update(
                        new LambdaUpdateWrapper<PlatRecordControl>()
                                .set(PlatRecordControl::getBeginTime, og.getEndTime())
                                .set(PlatRecordControl::getEndTime, DateUtil.offsetMinute(og.getEndTime(), 15))
                                .eq(PlatRecordControl::getPlatCode, "og")
                );
            }
        }
    }

    public void getRecord(Date beginTime,Date endTime) throws Exception {
//        Integer fetch_id = 12;//应当用redis记录此数据作为下次拉取的数据
//        Integer limit = 10000;
//        JSONObject json = new JSONObject();
//        json.put("fetch_id",fetch_id);
//        json.put("limit",limit);
//        String sortParmstr = SignUtils.sortParam(json);
//        String apiUrl = this.apiDomain + "/api/v2/platform/transaction/history?" + sortParmstr;
//        String result = apiRequestGet(apiUrl);
//        System.out.println(apiUrl);
//        JSONObject res = JSONObject.parseObject(result);
//        if(StrUtil.equals(res.getString("rs_code"), "S-100")){
//            Integer last_fetch_id = res.getInteger("last_fetch_id");
//            JSONArray records = res.getJSONArray("records");
//            {
//                "fetch_id": 12,
//                    "player_id": "gsTestAccount1", 账号
//                    "transaction_id": "1001421720000007834620_41", 单号
//                    "game_id": 55, 游戏id
//                    "round_id": 142172580,  台号
//                    "bet_place": "p",
//                    "result_url": "https:\/\/og-plus.oss-accelerate.aliyuncs.com\/results\/test\/20240418\/M120240418142172580.html",
//                    "debit_amount": 10,
//                    "credit_amount": 0,
//                    "winlose_amount": -10,
//                    "currency": "CNY",
//                    "secondary_info": {
//                "balance": 210,
//                        "bet_amount": 10, 投注金额
//                        "detain_amount": 0
//            },
//                "other_info": {
//                "ip": "175.100.24.122",
//                        "result": "banker,small,banker_bonus",
//                        "browser": "Chrome",
//                        "roundno": "18-18",
//                        "game_name": "baccarat",
//                        "deviceType": "desktop",
//                        "game_information": {
//                    "bankerCards": "QD,9H,",
//                            "playerCards": "4S,7C,"
//                }
//            },
//                "debit_at": 1713432871,  下注时间
//                    "rollback_at": "",
//                    "credit_at": 1713432891,  结算时间
//                    "cancel_at": "",
//                    "resettled_at": "",
//                    "transaction_type": "credit",
//                    "remark": {
//
//            },
//                "effective_amount": 10 有效投注
//            }
//        }
    }
}
