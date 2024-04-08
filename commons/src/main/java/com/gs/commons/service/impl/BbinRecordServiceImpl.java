package com.gs.commons.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.bo.PlatRecordBO;
import com.gs.commons.entity.BbinRecord;
import com.gs.commons.mapper.BbinRecordMapper;
import com.gs.commons.service.BbinRecordService;
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
* @description 针对表【t_bbin_record(BBIN注单表)】的数据库操作Service实现
* @createDate 2024-04-06 13:00:14
*/
@Service
public class BbinRecordServiceImpl extends ServiceImpl<BbinRecordMapper, BbinRecord>
    implements BbinRecordService{

    @Resource
    private BbinRecordMapper bbinRecordMapper;

    @Override
    public int batchInsertOrUpdate(List<BbinRecord> list) {
        return bbinRecordMapper.batchInsertOrUpdate(list);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<BbinRecord> wrapper = new QueryWrapper<BbinRecord>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), BbinRecord::getUserName, userName);

        Integer gameType = MapUtil.getInt(params, "gameType");
        wrapper.eq(null != gameType, BbinRecord::getGameType, gameType);

        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(startTime != null, BbinRecord::getCreateTime, startTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        wrapper.le(endTime != null, BbinRecord::getCreateTime, endTime);



        wrapper.orderByDesc(BbinRecord::getCreateTime);
        IPage<BbinRecord> page = this.page(
                new Query<BbinRecord>().getPage(params),
                wrapper);

        List<PlatRecordBO> platRecordBOList = new ArrayList<>();
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<BbinRecord> records = page.getRecords();
            platRecordBOList = BeanUtil.copyListPropertiesIgnoreNull(records, PlatRecordBO.class);

        }
        return new PageUtils(platRecordBOList, (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }
}




