package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.LyRecord;
import com.gs.commons.service.LyRecordService;
import com.gs.commons.mapper.LyRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}




