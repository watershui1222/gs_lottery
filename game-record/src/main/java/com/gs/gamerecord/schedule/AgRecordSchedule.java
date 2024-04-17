package com.gs.gamerecord.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gs.commons.entity.AgRecord;
import com.gs.commons.entity.PlatRecordControl;
import com.gs.commons.service.AgRecordService;
import com.gs.commons.service.PlatRecordControlService;
import com.gs.commons.utils.DesUtils;
import com.gs.gamerecord.utils.AgConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * AG定时任务
 *
 * @author Administrator
 */
@Slf4j
@Component
public class AgRecordSchedule {

    @Value("${platform.AG.dataApi}")
    public String dataApi;
    @Value("${platform.AG.dataMd5}")
    public String dataMd5;
    @Value("${platform.AG.dataAgent}")
    public String dataAgent;
    @Value("${platform.owner}")
    public String owner;

    @Autowired
    private PlatRecordControlService platRecordControlService;

    @Autowired
    private AgRecordService agRecordService;

    /**
     * BBIN视讯拉单任务
     * @throws Exception
     */
    @Async
    @Scheduled(cron = "0/50 * * * * ?")
    public void agliveRecord() throws Exception {
        Date now = new Date();
        PlatRecordControl aglive = platRecordControlService.getOne(
                new LambdaQueryWrapper<PlatRecordControl>()
                        .eq(PlatRecordControl::getStatus, 0)
                        .eq(PlatRecordControl::getPlatCode, "aglive")
        );

        if (aglive != null) {
            log.info("AG视讯---拉单开始[{}]-[{}]", DateUtil.formatDateTime(aglive.getBeginTime()), DateUtil.formatDateTime(aglive.getEndTime()));
            //需要将时间转换成美东时间
            Date mdBegin = DateUtil.offsetHour(aglive.getBeginTime(), -12);
            Date mdEnd = DateUtil.offsetHour(aglive.getEndTime(), -12);
            getRecord("aglive", mdBegin, mdEnd);
            log.info("AG视讯---拉单完成[{}]-[{}]", DateUtil.formatDateTime(aglive.getBeginTime()), DateUtil.formatDateTime(aglive.getEndTime()));
            // 如果当前时间大于结束时间，更新拉取时间范围
            if (DateUtil.compare(now, aglive.getEndTime()) == 1) {
                platRecordControlService.update(
                        new LambdaUpdateWrapper<PlatRecordControl>()
                                .set(PlatRecordControl::getBeginTime, aglive.getEndTime())
                                .set(PlatRecordControl::getEndTime, DateUtil.offsetMinute(aglive.getEndTime(), 10))
                                .eq(PlatRecordControl::getPlatCode, "aglive")
                );
            }
        }
    }

    /**
     * ag xin电子拉单任务
     * @throws Exception
     */
    @Async
    @Scheduled(cron = "0/20 * * * * ?")
    public void agxingameRecord() throws Exception {
        Date now = new Date();
        PlatRecordControl agxin = platRecordControlService.getOne(
                new LambdaQueryWrapper<PlatRecordControl>()
                        .eq(PlatRecordControl::getStatus, 0)
                        .eq(PlatRecordControl::getPlatCode, "agxin")
        );

        if (agxin != null) {
            log.info("AG XIN电子---拉单开始[{}]-[{}]", DateUtil.formatDateTime(agxin.getBeginTime()), DateUtil.formatDateTime(agxin.getEndTime()));
            Date mdBegin = DateUtil.offsetHour(agxin.getBeginTime(), -12);
            Date mdEnd = DateUtil.offsetHour(agxin.getEndTime(), -12);
            getRecord("agxin", mdBegin, mdEnd);
            log.info("AG XIN电子---拉单完成[{}]-[{}]", DateUtil.formatDateTime(agxin.getBeginTime()), DateUtil.formatDateTime(agxin.getEndTime()));
            // 如果当前时间大于结束时间，更新拉取时间范围
            if (DateUtil.compare(now, agxin.getEndTime()) == 1) {
                platRecordControlService.update(
                        new LambdaUpdateWrapper<PlatRecordControl>()
                                .set(PlatRecordControl::getBeginTime, agxin.getEndTime())
                                .set(PlatRecordControl::getEndTime, DateUtil.offsetMinute(agxin.getEndTime(), 10))
                                .eq(PlatRecordControl::getPlatCode, "agxin")
                );
            }
        }
    }


    /**
     * ag yoplay电子拉单任务
     * @throws Exception
     */
    @Async
    @Scheduled(cron = "0/30 * * * * ?")
    public void agyoplaygameRecord() throws Exception {
        Date now = new Date();
        PlatRecordControl agyoplay = platRecordControlService.getOne(
                new LambdaQueryWrapper<PlatRecordControl>()
                        .eq(PlatRecordControl::getStatus, 0)
                        .eq(PlatRecordControl::getPlatCode, "agyoplay")
        );

        if (agyoplay != null) {
            log.info("AG YOPLAY电子---拉单开始[{}]-[{}]", DateUtil.formatDateTime(agyoplay.getBeginTime()), DateUtil.formatDateTime(agyoplay.getEndTime()));
            Date mdBegin = DateUtil.offsetHour(agyoplay.getBeginTime(), -12);
            Date mdEnd = DateUtil.offsetHour(agyoplay.getEndTime(), -12);
            getRecord("agyoplay", mdBegin, mdEnd);
            log.info("AG YOPLAY电子---拉单完成[{}]-[{}]", DateUtil.formatDateTime(agyoplay.getBeginTime()), DateUtil.formatDateTime(agyoplay.getEndTime()));
            // 如果当前时间大于结束时间，更新拉取时间范围
            if (DateUtil.compare(now, agyoplay.getEndTime()) == 1) {
                platRecordControlService.update(
                        new LambdaUpdateWrapper<PlatRecordControl>()
                                .set(PlatRecordControl::getBeginTime, agyoplay.getEndTime())
                                .set(PlatRecordControl::getEndTime, DateUtil.offsetMinute(agyoplay.getEndTime(), 10))
                                .eq(PlatRecordControl::getPlatCode, "agyoplay")
                );
            }
        }
    }


    /**
     * AG捕鱼拉单任务
     * @throws Exception
     */
    @Async
    @Scheduled(cron = "0/40 * * * * ?")
    public void agfishgameRecord() throws Exception {
        Date now = new Date();
        PlatRecordControl agfish = platRecordControlService.getOne(
                new LambdaQueryWrapper<PlatRecordControl>()
                        .eq(PlatRecordControl::getStatus, 0)
                        .eq(PlatRecordControl::getPlatCode, "agfish")
        );

        if (agfish != null) {
            log.info("AG捕鱼---拉单开始[{}]-[{}]", DateUtil.formatDateTime(agfish.getBeginTime()), DateUtil.formatDateTime(agfish.getEndTime()));
            Date mdBegin = DateUtil.offsetHour(agfish.getBeginTime(), -12);
            Date mdEnd = DateUtil.offsetHour(agfish.getEndTime(), -12);
            getRecord("agfish", mdBegin, mdEnd);
            log.info("AG捕鱼---拉单完成[{}]-[{}]", DateUtil.formatDateTime(agfish.getBeginTime()), DateUtil.formatDateTime(agfish.getEndTime()));
            // 如果当前时间大于结束时间，更新拉取时间范围
            if (DateUtil.compare(now, agfish.getEndTime()) == 1) {
                platRecordControlService.update(
                        new LambdaUpdateWrapper<PlatRecordControl>()
                                .set(PlatRecordControl::getBeginTime, agfish.getEndTime())
                                .set(PlatRecordControl::getEndTime, DateUtil.offsetMinute(agfish.getEndTime(), 10))
                                .eq(PlatRecordControl::getPlatCode, "agfish")
                );
            }
        }
    }


    public void getRecord(String subCode, Date startTime, Date endTime){
        String method = "";
        if(StrUtil.equals(subCode, "aglive")){
            method = "getorders.xml";
        }else if(StrUtil.equals(subCode, "agfish")){
            method = "gethunterorders.xml";
        }else if (StrUtil.equals(subCode, "agxin")){
            method = "getslotorders_ex.xml";
        }else if(StrUtil.equals(subCode, "agyoplay")){
            method = "getyoplayorders_ex.xml";
        }
        String domain = this.dataApi + "/" + method;
        String cagent = this.dataAgent;
        String enddate = DateUtil.format(endTime,"yyyy-MM-dd HH:mm:ss");
        String startdate = DateUtil.format(DateUtil.offsetMinute(endTime, -10),"yyyy-MM-dd HH:mm:ss");
        if(StrUtil.equals(subCode, "agfish")){
            Date oEndTime = DateUtil.offsetHour(endTime, 12);
            enddate = oEndTime.getTime()/1000 + "";
            startdate = DateUtil.offsetMinute(oEndTime, -10).getTime()/1000 + "";
        }
        String by = "DESC";
        int page = 1;
        int perpage = 500;
        int totalpage = 1;
        String key = DesUtils.MD5(this.dataAgent + startdate + enddate + by + page + perpage + this.dataMd5);
        List<AgRecord> list = new ArrayList<>();

        do{
            StringBuilder paramSB = new StringBuilder();
            paramSB.append("?cagent=").append(cagent)
                    .append("&startdate=").append(startdate)
                    .append("&enddate=").append(enddate)
                    .append("&by=").append(by)
                    .append("&page=").append(page)
                    .append("&perpage=").append(perpage)
                    .append("&key=").append(key);
            String url = domain + paramSB;
            log.info("ag getRecord url:{}", url);
            String result = HttpUtil.get(url);
            log.info("ag getRecord result:{}", result);
            Document document = XmlUtil.parseXml(result);
            Element resultEl = XmlUtil.getRootElement(document);
            String info = XmlUtil.getElement(resultEl, "info").getTextContent();
            if(StrUtil.equals(info, "0")){
                Element addition = XmlUtil.getElement(resultEl, "addition");
                totalpage = Integer.valueOf(XmlUtil.getElement(addition, "totalpage").getTextContent());
                List<Element> row = XmlUtil.getElements(resultEl, "row");
                for (Element e:row){
                    AgRecord record = new AgRecord();
                    record.setCreateTime(new Date());
                    record.setUpdateTime(new Date());
                    if(StrUtil.equals(subCode, "aglive")){
                        if(!isThisOwner(e.getAttribute("playName"))){
                            continue;
                        }
                        createLiveRecord(e, record);
                    }else if(StrUtil.equals(subCode, "agfish")){
                        if(!isThisOwner(e.getAttribute("username"))){
                            continue;
                        }
                        createFishRecord(e, record);
                    }else if (StrUtil.equals(subCode, "agxin")){
                        if(!isThisOwner(e.getAttribute("username"))){
                            continue;
                        }
                        createXinRecord(e, record);
                    }else if(StrUtil.equals(subCode, "agyoplay")){
                        if(!isThisOwner(e.getAttribute("username"))){
                            continue;
                        }
                        createYoplayRecord(e, record);
                    }
                    list.add(record);
                }
            }
            page++;
        }while(page <= totalpage);
        if(CollUtil.isNotEmpty(list)){
            log.info("AG---注单[{}]个", list.size());
            agRecordService.batchInsertOrUpdate(list);
        }
    }

    private boolean isThisOwner(String playName) {
        //判断是不是本平台用户
        String ownerUsername = playName;
        String subOwnerStr = ownerUsername.substring(0, 2);
        if(!StrUtil.equals(subOwnerStr, this.owner)){
            return false;
        }
        return true;
    }

    private void createYoplayRecord(Element e, AgRecord record) {
        String platUserName = e.getAttribute("username");
        String username = platUserName.substring(2);
        record.setUserName(username);
        record.setPlatUserName(platUserName);
        record.setOrderNo(e.getAttribute("billno"));
        record.setGameId(e.getAttribute("gametype"));
        record.setGameName(AgConstants.GAME_NAME.getOrDefault(record.getGameId(), record.getGameId()));
        record.setEffectiveBet(new BigDecimal(e.getAttribute("valid_account")));
        record.setAllBet(new BigDecimal(e.getAttribute("account")));
        record.setProfit(new BigDecimal(e.getAttribute("cus_account")));
        record.setWagersDate(DateUtil.parse(e.getAttribute("billtime")));
        record.setBetTime(DateUtil.offsetHour(record.getWagersDate(), 12));
        record.setModifiedDate(DateUtil.parse(e.getAttribute("reckontime")));
        record.setSettleTime(DateUtil.offsetHour(record.getModifiedDate(), 12));
        record.setResultStatus(e.getAttribute("flag"));
        record.setSerialId(e.getAttribute("gmcode"));
        record.setGameType(2);
    }

    private void createXinRecord(Element e, AgRecord record) {
        String platUserName = e.getAttribute("username");
        String username = platUserName.substring(2);
        record.setUserName(username);
        record.setPlatUserName(platUserName);
        record.setOrderNo(e.getAttribute("billno"));
        record.setGameId(e.getAttribute("gametype"));
        record.setGameName(AgConstants.GAME_NAME.getOrDefault(record.getGameId(), record.getGameId()));
        record.setEffectiveBet(new BigDecimal(e.getAttribute("valid_account")));
        record.setAllBet(new BigDecimal(e.getAttribute("account")));
        record.setProfit(new BigDecimal(e.getAttribute("cus_account")));
        record.setWagersDate(DateUtil.parse(e.getAttribute("billtime")));
        record.setBetTime(DateUtil.offsetHour(record.getWagersDate(), 12));
        record.setModifiedDate(DateUtil.parse(e.getAttribute("reckontime")));
        record.setSettleTime(DateUtil.offsetHour(record.getModifiedDate(), 12));
        record.setResultStatus(e.getAttribute("flag"));
        record.setGameType(2);
    }

    private void createFishRecord(Element e, AgRecord record) {
        String platUserName = e.getAttribute("username");
        String username = platUserName.substring(2);
        record.setUserName(username);
        record.setPlatUserName(platUserName);
        record.setOrderNo(e.getAttribute("billno"));
        record.setGameId(e.getAttribute("gametype"));
        record.setGameName(AgConstants.GAME_NAME.getOrDefault(record.getGameId(), record.getGameId()));
        record.setEffectiveBet(new BigDecimal(e.getAttribute("valid_account")));
        record.setAllBet(new BigDecimal(e.getAttribute("account")));
        record.setProfit(new BigDecimal(e.getAttribute("cus_account")));
        Long billtime = Long.valueOf(e.getAttribute("billtime"));
        record.setBetTime(DateUtil.date(billtime*1000));
        record.setWagersDate(DateUtil.offsetHour(record.getBetTime(), -12));
        Long reckontime = Long.valueOf(e.getAttribute("reckontime"));
        record.setSettleTime(DateUtil.date(reckontime*1000));
        record.setModifiedDate(DateUtil.offsetHour(record.getSettleTime(), -12));
        record.setResultStatus(e.getAttribute("flag"));
        record.setSerialId(e.getAttribute("roomid"));
        record.setGameType(3);
    }

    private void createLiveRecord(Element e, AgRecord record) {
        String platUserName = e.getAttribute("playName");
        String username = platUserName.substring(2);
        record.setUserName(username);
        record.setPlatUserName(platUserName);
        record.setOrderNo(e.getAttribute("billNo"));
        record.setGameId(e.getAttribute("gameType"));
        record.setGameName(AgConstants.GAME_NAME.getOrDefault(record.getGameId(), record.getGameId()));
        record.setEffectiveBet(new BigDecimal(e.getAttribute("validBetAmount")));
        record.setAllBet(new BigDecimal(e.getAttribute("betAmount")));
        record.setProfit(new BigDecimal(e.getAttribute("netAmount")));
        record.setWagersDate(DateUtil.parse(e.getAttribute("betTime")));
        record.setBetTime(DateUtil.offsetHour(record.getWagersDate(), 12));
        record.setModifiedDate(DateUtil.parse(e.getAttribute("recalcuTime")));
        record.setSettleTime(DateUtil.offsetHour(record.getModifiedDate(), 12));
        record.setResultStatus(e.getAttribute("flag"));
        record.setSerialId(e.getAttribute("gameCode"));
        record.setGameType(1);
    }
}
