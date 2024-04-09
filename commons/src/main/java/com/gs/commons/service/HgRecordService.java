package com.gs.commons.service;

import com.gs.commons.entity.HgRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
* @author tommm
* @description 针对表【t_hg_record(皇冠体育注单表)】的数据库操作Service
* @createDate 2024-04-05 20:02:27
*/
public interface HgRecordService extends IService<HgRecord> {

    int batchInsertOrUpdate(List<HgRecord> recordList);

    PageUtils queryPage(Map<String, Object> params);
}
