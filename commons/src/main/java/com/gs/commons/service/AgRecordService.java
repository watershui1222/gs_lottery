package com.gs.commons.service;

import com.gs.commons.entity.AgRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author richard
* @description 针对表【t_ag_record(AG注单表)】的数据库操作Service
* @createDate 2024-04-09 16:19:30
*/
public interface AgRecordService extends IService<AgRecord> {

    int batchInsertOrUpdate(List<AgRecord> list);
}
