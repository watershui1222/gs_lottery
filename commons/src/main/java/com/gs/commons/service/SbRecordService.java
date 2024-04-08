package com.gs.commons.service;

import com.gs.commons.entity.SbRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author richard
* @description 针对表【t_sb_record(沙巴体育注单表)】的数据库操作Service
* @createDate 2024-04-08 18:32:16
*/
public interface SbRecordService extends IService<SbRecord> {

    int batchInsertOrUpdate(List<SbRecord> list);
}
