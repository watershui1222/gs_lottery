package com.gs.commons.mapper;

import com.gs.commons.entity.AgRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author richard
* @description 针对表【t_ag_record(AG注单表)】的数据库操作Mapper
* @createDate 2024-04-09 16:19:30
* @Entity com.gs.commons.entity.AgRecord
*/
public interface AgRecordMapper extends BaseMapper<AgRecord> {

    int batchInsertOrUpdate(List<AgRecord> list);
}




