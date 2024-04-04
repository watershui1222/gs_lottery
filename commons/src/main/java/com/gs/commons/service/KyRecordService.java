package com.gs.commons.service;

import com.gs.commons.entity.KyRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 69000
* @description 针对表【t_ky_record】的数据库操作Service
* @createDate 2024-04-04 18:48:43
*/
public interface KyRecordService extends IService<KyRecord> {

    int batchInsertOrUpdate(List<KyRecord> list);

}
