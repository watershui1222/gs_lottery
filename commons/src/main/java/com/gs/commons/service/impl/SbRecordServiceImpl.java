package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.PlatRecordBO;
import com.gs.commons.bo.PlatRecordSprotBO;
import com.gs.commons.entity.SbRecord;
import com.gs.commons.service.SbRecordService;
import com.gs.commons.mapper.SbRecordMapper;
import com.gs.commons.utils.BeanUtil;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author richard
* @description 针对表【t_sb_record(沙巴体育注单表)】的数据库操作Service实现
* @createDate 2024-04-08 18:32:16
*/
@Service
public class SbRecordServiceImpl extends ServiceImpl<SbRecordMapper, SbRecord>
    implements SbRecordService{

    @Resource
    private SbRecordMapper sbRecordMapper;
    @Override
    public int batchInsertOrUpdate(List<SbRecord> list) {
        return sbRecordMapper.batchInsertOrUpdate(list);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<SbRecord> wrapper = new QueryWrapper<SbRecord>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), SbRecord::getUserName, userName);

        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(startTime != null, SbRecord::getBetTime, startTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        wrapper.le(endTime != null, SbRecord::getBetTime, endTime);

        wrapper.orderByDesc(SbRecord::getBetTime);
        IPage<SbRecord> page = this.page(
                new Query<SbRecord>().getPage(params),
                wrapper);

        List<PlatRecordBO> platRecordBOList = new ArrayList<>();
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<SbRecord> records = page.getRecords();
            //封装体育数据
            for (SbRecord sbRecord:records) {
                PlatRecordBO bo = new PlatRecordBO();
                BeanUtil.copyPropertiesIgnoreNull(sbRecord, bo);
                if(StrUtil.isNotBlank(sbRecord.getParlaysub())){
                    //串关
                    bo.setIsSport(sbRecord.getParlaynum());
                    List<PlatRecordSprotBO> sportDetailList = new ArrayList<>();
                    JSONArray parlaySub = JSONArray.parseArray(sbRecord.getParlaysub());
                    for(Object obj:parlaySub){
                        JSONObject sub = (JSONObject) obj;
                        PlatRecordSprotBO recordSport = new PlatRecordSprotBO();
                        JSONArray bettypename = sub.getJSONArray("bettypename");
                        String bettypenameCN = bettypename.stream().filter(t -> ((JSONObject) t).getString("lang").equals("cs")).map(t -> ((JSONObject) t).getString("name")).distinct().collect(Collectors.joining());
                        recordSport.setWf(bettypenameCN);
                        String pk = StrUtil.equals("1", sub.getString("islive")) ? "滚球" : "";
                        recordSport.setPk(pk);
                        recordSport.setOdds(sub.getBigDecimal("odds"));
                        JSONArray hometeamname = sub.getJSONArray("hometeamname");
                        String hometeamnameCN = hometeamname.stream().filter(t -> ((JSONObject) t).getString("lang").equals("cs")).map(t -> ((JSONObject) t).getString("name")).distinct().collect(Collectors.joining());
                        recordSport.setTnameHome(hometeamnameCN);
                        JSONArray awayteamname = sub.getJSONArray("awayteamname");
                        String awayteamnameCN = awayteamname.stream().filter(t -> ((JSONObject) t).getString("lang").equals("cs")).map(t -> ((JSONObject) t).getString("name")).distinct().collect(Collectors.joining());
                        recordSport.setTnameAway(awayteamnameCN);
                        String home_score = sub.getString("home_score");
                        String away_score = sub.getString("away_score");
                        if (StrUtil.isAllNotBlank(home_score, away_score)){
                            recordSport.setScore(home_score + ":" + away_score);
                        }
                        JSONArray leaguename = sub.getJSONArray("leaguename");
                        String leaguenameCN = leaguename.stream().filter(t -> ((JSONObject) t).getString("lang").equals("cs")).map(t -> ((JSONObject) t).getString("name")).distinct().collect(Collectors.joining());
                        recordSport.setLeague(leaguenameCN);
                        JSONArray sportname = sub.getJSONArray("sportname");
                        String sportnameCN = sportname.stream().filter(t -> ((JSONObject) t).getString("lang").equals("cs")).map(t -> ((JSONObject) t).getString("name")).distinct().collect(Collectors.joining());
                        recordSport.setGameName(sportnameCN);
                        recordSport.setOddsFormat(sub.getString("hdp"));
                        recordSport.setOrderContent(sub.getString("betContent"));
                        Date match_datetime = DateUtil.parse(sub.getString("match_datetime"), "yyyy-MM-dd'T'HH:mm:ss");
                        recordSport.setMatchDatetime(DateUtil.offsetHour(match_datetime, 12));
                        sportDetailList.add(recordSport);
                    }
                    bo.setSportDetailList(sportDetailList);
                }else{
                    //普通下注
                    bo.setIsSport(1);
                    PlatRecordSprotBO recordSport = new PlatRecordSprotBO();
                    recordSport.setWf(sbRecord.getRtype());
                    recordSport.setPk(sbRecord.getWtype());
                    recordSport.setOdds(sbRecord.getIoratio());
                    recordSport.setTnameHome(sbRecord.getTnameHome());
                    recordSport.setTnameAway(sbRecord.getTnameAway());
//                    recordSport.setResultScore(sbRecord.getResultScore());
                    recordSport.setLeague(sbRecord.getLeague());
                    recordSport.setGameName(sbRecord.getGameName());
                    recordSport.setOddsFormat(sbRecord.getOddsFormat());
                    recordSport.setScore(sbRecord.getScore());
                    recordSport.setOrderContent(sbRecord.getBetContent());
                    recordSport.setMatchDatetime(sbRecord.getMatchDatetime());
                    bo.setSportDetail(recordSport);
                }
                platRecordBOList.add(bo);
            }
        }
        return new PageUtils(platRecordBOList, (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }
}




