package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.PlatRecordBO;
import com.gs.commons.entity.KyRecord;
import com.gs.commons.mapper.KyRecordMapper;
import com.gs.commons.service.KyRecordService;
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
* @author 69000
* @description 针对表【t_ky_record】的数据库操作Service实现
* @createDate 2024-04-04 18:48:43
*/
@Service
public class KyRecordServiceImpl extends ServiceImpl<KyRecordMapper, KyRecord>
    implements KyRecordService{

    @Autowired
    private KyRecordMapper kyRecordMapper;

    @Override
    public int batchInsertOrUpdate(List<KyRecord> list) {
        return kyRecordMapper.batchInsertOrUpdate(list);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<KyRecord> wrapper = new QueryWrapper<KyRecord>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), KyRecord::getUserName, userName);

        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(startTime != null, KyRecord::getCreateTime, startTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        wrapper.le(endTime != null, KyRecord::getCreateTime, endTime);

        wrapper.orderByDesc(KyRecord::getCreateTime);
        IPage<KyRecord> page = this.page(
                new Query<KyRecord>().getPage(params),
                wrapper);

        List<PlatRecordBO> platRecordBOList = new ArrayList<>();
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<KyRecord> records = page.getRecords();
            platRecordBOList = BeanUtil.copyListPropertiesIgnoreNull(records, PlatRecordBO.class);

        }
        return new PageUtils(platRecordBOList, (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }
}




