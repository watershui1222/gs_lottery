package com.gs.commons.service;

import com.gs.commons.entity.BbinRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
* @author richard
* @description 针对表【t_bbin_record(BBIN注单表)】的数据库操作Service
* @createDate 2024-04-06 13:00:14
*/
public interface BbinRecordService extends IService<BbinRecord> {

    int batchInsertOrUpdate(List<BbinRecord> list);

    PageUtils queryPage(Map<String,Object> params);
}
