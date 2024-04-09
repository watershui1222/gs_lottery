package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.AgRecord;
import com.gs.commons.service.AgRecordService;
import com.gs.commons.mapper.AgRecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
}




