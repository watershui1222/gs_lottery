package com.gs.commons.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.TransactionRecord;
import com.gs.commons.service.TransactionRecordService;
import com.gs.commons.mapper.TransactionRecordMapper;
import com.gs.commons.utils.PageUtils;
import com.gs.commons.utils.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
* @author 69000
* @description 针对表【t_transaction_record】的数据库操作Service实现
* @createDate 2024-04-03 17:30:21
*/
@Service
public class TransactionRecordServiceImpl extends ServiceImpl<TransactionRecordMapper, TransactionRecord>
    implements TransactionRecordService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<TransactionRecord> wrapper = new QueryWrapper<TransactionRecord>().lambda();

        String userName = MapUtil.getStr(params, "userName");
        wrapper.eq(StringUtils.isNotBlank(userName), TransactionRecord::getUserName, userName);
        String type = MapUtil.getStr(params, "type");
        wrapper.eq(StringUtils.isNotBlank(type), TransactionRecord::getBusinessType, type);

        Date startTime = MapUtil.getDate(params, "startTime");
        wrapper.ge(startTime != null, TransactionRecord::getCreateTime, startTime);

        Date endTime = MapUtil.getDate(params, "endTime");
        wrapper.le(endTime != null, TransactionRecord::getCreateTime, endTime);

        wrapper.orderByDesc(TransactionRecord::getCreateTime);
        IPage<TransactionRecord> page = this.page(
                new Query<TransactionRecord>().getPage(params),
                wrapper);
        return new PageUtils(page);
    }
}




