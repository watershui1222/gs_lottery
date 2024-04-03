package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.TransactionRecord;
import com.gs.commons.service.TransactionRecordService;
import com.gs.commons.mapper.TransactionRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author 69000
* @description 针对表【t_transaction_record】的数据库操作Service实现
* @createDate 2024-04-03 17:30:21
*/
@Service
public class TransactionRecordServiceImpl extends ServiceImpl<TransactionRecordMapper, TransactionRecord>
    implements TransactionRecordService{

}




