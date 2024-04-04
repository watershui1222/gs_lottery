package com.gs.commons.mapper;

import com.gs.commons.entity.KyRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 69000
* @description 针对表【t_ky_record】的数据库操作Mapper
* @createDate 2024-04-04 18:48:43
* @Entity com.gs.commons.entity.KyRecord
*/
public interface KyRecordMapper extends BaseMapper<KyRecord> {

    int batchInsertOrUpdate(List<KyRecord> list);
}




