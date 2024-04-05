package com.gs.commons.service;

import com.gs.commons.entity.LyRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author richard
* @description 针对表【t_ly_record】的数据库操作Service
* @createDate 2024-04-05 16:42:55
*/
public interface LyRecordService extends IService<LyRecord> {

    int batchInsertOrUpdate(List<LyRecord> lyRecords);
}
