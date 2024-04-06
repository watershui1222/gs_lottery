package com.gs.commons.mapper;

import com.gs.commons.entity.BbinRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author richard
* @description 针对表【t_bbin_record(BBIN注单表)】的数据库操作Mapper
* @createDate 2024-04-06 13:00:14
* @Entity com.gs.commons.entity.BbinRecord
*/
public interface BbinRecordMapper extends BaseMapper<BbinRecord> {

    int batchInsertOrUpdate(List<BbinRecord> list);
}




