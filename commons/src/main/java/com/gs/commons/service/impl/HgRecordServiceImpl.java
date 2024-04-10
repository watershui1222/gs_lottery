package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.PlatRecordBO;
import com.gs.commons.bo.PlatRecordSprotBO;
import com.gs.commons.entity.HgRecord;
import com.gs.commons.entity.SbRecord;
import com.gs.commons.service.HgRecordService;
import com.gs.commons.mapper.HgRecordMapper;
import com.gs.commons.utils.BeanUtil;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
* @author tommm
* @description 针对表【t_hg_record(皇冠体育注单表)】的数据库操作Service实现
* @createDate 2024-04-05 20:02:27
*/
@Service
public class HgRecordServiceImpl extends ServiceImpl<HgRecordMapper, HgRecord>
    implements HgRecordService{

    @Resource
    private HgRecordMapper hgRecordMapper;

    @Override
    public int batchInsertOrUpdate(List<HgRecord> recordList) {
        return hgRecordMapper.batchInsertOrUpdate(recordList);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<HgRecord> wrapper = new QueryWrapper<HgRecord>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), HgRecord::getUserName, userName);

        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(startTime != null, HgRecord::getSettleTime, startTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        wrapper.le(endTime != null, HgRecord::getSettleTime, endTime);

        wrapper.orderByDesc(HgRecord::getSettleTime);
        IPage<HgRecord> page = this.page(
                new Query<HgRecord>().getPage(params),
                wrapper);

        List<PlatRecordBO> platRecordBOList = new ArrayList<>();
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<HgRecord> records = page.getRecords();
            //封装体育数据
            for (HgRecord hgRecord:records) {
                PlatRecordBO bo = new PlatRecordBO();
                BeanUtil.copyPropertiesIgnoreNull(hgRecord, bo);
                if(StrUtil.isNotBlank(hgRecord.getParlaysub())){
                    //串关
                    bo.setIsSport(2);
                    List<PlatRecordSprotBO> sportDetailList = new ArrayList<>();
                    JSONObject parlaySub = JSONObject.parse(hgRecord.getParlaysub());
                    for(int i = 1; i <= hgRecord.getParlaynum(); i++){
                        String key = i + "";
                        JSONObject sub = parlaySub.getJSONObject(key);
                        PlatRecordSprotBO recordSport = new PlatRecordSprotBO();
                        recordSport.setWf(sub.getString("wtype"));
                        recordSport.setOdds(sub.getBigDecimal("ioratio"));
                        recordSport.setTnameHome(sub.getString("tname_home"));
                        recordSport.setTnameAway(sub.getString("tname_away"));
                        recordSport.setResultScore(sub.getString("score"));
                        recordSport.setLeague(sub.getString("league"));
                        recordSport.setGameName(GAME_NAME.getOrDefault(sub.getString("wtype"), sub.getString("wtype")));
                        recordSport.setOddsFormat(sub.getString("oddsFormat"));
                        String rtype = sub.getString("rtype");
                        recordSport.setOpenResult(rtype.split("_")[1]);
                        recordSport.setOrderContent(sub.getString("order"));
                        sportDetailList.add(recordSport);
                    }
                    bo.setSportDetailList(sportDetailList);
                }else{
                    //普通下注
                    bo.setIsSport(1);
                    PlatRecordSprotBO recordSport = new PlatRecordSprotBO();
                    recordSport.setWf(hgRecord.getWtype());
                    recordSport.setOdds(hgRecord.getIoratio());
                    recordSport.setTnameHome(hgRecord.getTnameHome());
                    recordSport.setTnameAway(hgRecord.getTnameAway());
                    recordSport.setResultScore(hgRecord.getResultScore());
                    recordSport.setLeague(hgRecord.getLeague());
                    recordSport.setGameName(hgRecord.getGameName());
                    recordSport.setOddsFormat(hgRecord.getOddsFormat());
                    recordSport.setOrderContent(hgRecord.getOrderContent());
                    recordSport.setOpenResult(hgRecord.getRtype());
                    bo.setSportDetail(recordSport);
                }
                platRecordBOList.add(bo);
            }

        }
        return new PageUtils(platRecordBOList, (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }

    public static final Map<String, String> GAME_NAME = new HashMap<>();
    static {
        GAME_NAME.put("FT", "足球");
        GAME_NAME.put("BK", "篮球");
        GAME_NAME.put("TB", "网球");
        GAME_NAME.put("BS", "棒球");
        GAME_NAME.put("OP", "其他");
        GAME_NAME.put("VF", "虚拟足球");
        GAME_NAME.put("SK", "台球");
        GAME_NAME.put("MT", "跨球类过关");
    }
}




