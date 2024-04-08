package com.gs.commons.mapper;

import com.gs.commons.entity.SbRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author richard
* @description 针对表【t_sb_record(沙巴体育注单表)】的数据库操作Mapper
* @createDate 2024-04-08 18:32:16
* @Entity com.gs.commons.entity.SbRecord
*/
public interface SbRecordMapper extends BaseMapper<SbRecord> {

    int batchInsertOrUpdate(List<SbRecord> list);
}




