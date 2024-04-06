package com.gs.commons.mapper;

import com.gs.commons.entity.HgRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_hg_record(皇冠体育注单表)】的数据库操作Mapper
* @createDate 2024-04-05 20:02:27
* @Entity com.gs.commons.entity.HgRecord
*/
public interface HgRecordMapper extends BaseMapper<HgRecord> {

    int batchInsertOrUpdate(List<HgRecord> list);
}




