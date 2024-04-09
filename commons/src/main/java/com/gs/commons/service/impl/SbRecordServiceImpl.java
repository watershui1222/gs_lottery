package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.PlatRecordBO;
import com.gs.commons.entity.SbRecord;
import com.gs.commons.service.SbRecordService;
import com.gs.commons.mapper.SbRecordMapper;
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
        wrapper.ge(startTime != null, SbRecord::getSettleTime, startTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        wrapper.le(endTime != null, SbRecord::getSettleTime, endTime);

        wrapper.orderByDesc(SbRecord::getSettleTime);
        IPage<SbRecord> page = this.page(
                new Query<SbRecord>().getPage(params),
                wrapper);

        List<PlatRecordBO> platRecordBOList = new ArrayList<>();
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<SbRecord> records = page.getRecords();
            platRecordBOList = BeanUtil.copyListPropertiesIgnoreNull(records, PlatRecordBO.class);

        }
        return new PageUtils(platRecordBOList, (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }
}




