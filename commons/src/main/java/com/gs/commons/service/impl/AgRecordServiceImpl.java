package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.PlatRecordBO;
import com.gs.commons.entity.AgRecord;
import com.gs.commons.service.AgRecordService;
import com.gs.commons.mapper.AgRecordMapper;
import com.gs.commons.utils.BeanUtil;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author richard
* @description 针对表【t_ag_record(AG注单表)】的数据库操作Service实现
* @createDate 2024-04-09 16:19:30
*/
@Service
public class AgRecordServiceImpl extends ServiceImpl<AgRecordMapper, AgRecord>
    implements AgRecordService{

    @Resource
    private AgRecordMapper agRecordMapper;

    @Override
    public int batchInsertOrUpdate(List<AgRecord> list) {
        return agRecordMapper.batchInsertOrUpdate(list);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<AgRecord> wrapper = new QueryWrapper<AgRecord>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), AgRecord::getUserName, userName);

        Integer gameType = MapUtil.getInt(params, "gameType");
        wrapper.eq(null != gameType, AgRecord::getGameType, gameType);

        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(startTime != null, AgRecord::getSettleTime, startTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        wrapper.le(endTime != null, AgRecord::getSettleTime, endTime);



        wrapper.orderByDesc(AgRecord::getSettleTime);
        IPage<AgRecord> page = this.page(
                new Query<AgRecord>().getPage(params),
                wrapper);

        List<PlatRecordBO> platRecordBOList = new ArrayList<>();
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<AgRecord> records = page.getRecords();
            platRecordBOList = BeanUtil.copyListPropertiesIgnoreNull(records, PlatRecordBO.class);

        }
        return new PageUtils(platRecordBOList, (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }
}




