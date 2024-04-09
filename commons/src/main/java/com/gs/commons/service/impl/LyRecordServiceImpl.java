package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.PlatRecordBO;
import com.gs.commons.entity.LyRecord;
import com.gs.commons.mapper.LyRecordMapper;
import com.gs.commons.service.LyRecordService;
import com.gs.commons.utils.BeanUtil;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author richard
* @description 针对表【t_ly_record】的数据库操作Service实现
* @createDate 2024-04-05 16:42:55
*/
@Service
public class LyRecordServiceImpl extends ServiceImpl<LyRecordMapper, LyRecord>
    implements LyRecordService{

    @Autowired
    private LyRecordMapper lyRecordMapper;
    @Override
    public int batchInsertOrUpdate(List<LyRecord> lyRecords) {
        return lyRecordMapper.batchInsertOrUpdate(lyRecords);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<LyRecord> wrapper = new QueryWrapper<LyRecord>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), LyRecord::getUserName, userName);

        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(startTime != null, LyRecord::getSettleTime, startTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        wrapper.le(endTime != null, LyRecord::getSettleTime, endTime);

        wrapper.orderByDesc(LyRecord::getSettleTime);
        IPage<LyRecord> page = this.page(
                new Query<LyRecord>().getPage(params),
                wrapper);

        List<PlatRecordBO> platRecordBOList = new ArrayList<>();
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<LyRecord> records = page.getRecords();
            platRecordBOList = BeanUtil.copyListPropertiesIgnoreNull(records, PlatRecordBO.class);

        }
        return new PageUtils(platRecordBOList, (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }
}




