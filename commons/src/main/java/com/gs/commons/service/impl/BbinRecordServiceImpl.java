package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.BbinRecord;
import com.gs.commons.service.BbinRecordService;
import com.gs.commons.mapper.BbinRecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
}




