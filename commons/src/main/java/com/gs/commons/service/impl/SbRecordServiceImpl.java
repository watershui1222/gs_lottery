package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.SbRecord;
import com.gs.commons.service.SbRecordService;
import com.gs.commons.mapper.SbRecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
}




