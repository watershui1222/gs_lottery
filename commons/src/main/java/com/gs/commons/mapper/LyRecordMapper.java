package com.gs.commons.mapper;

import com.gs.commons.entity.LyRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author richard
* @description 针对表【t_ly_record】的数据库操作Mapper
* @createDate 2024-04-05 16:42:55
* @Entity com.gs.commons.entity.LyRecord
*/
public interface LyRecordMapper extends BaseMapper<LyRecord> {

    int batchInsertOrUpdate(List<LyRecord> list);
}




