package com.gs.commons.service;

import com.gs.commons.entity.TransactionRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.utils.PageUtils;

import java.util.Map;

/**
* @author 69000
* @description 针对表【t_transaction_record】的数据库操作Service
* @createDate 2024-04-03 17:30:21
*/
public interface TransactionRecordService extends IService<TransactionRecord> {

    PageUtils queryPage(Map<String,Object> params);

}
