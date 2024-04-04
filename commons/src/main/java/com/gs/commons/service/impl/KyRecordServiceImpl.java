package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.KyRecord;
import com.gs.commons.service.KyRecordService;
import com.gs.commons.mapper.KyRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 69000
* @description 针对表【t_ky_record】的数据库操作Service实现
* @createDate 2024-04-04 18:48:43
*/
@Service
public class KyRecordServiceImpl extends ServiceImpl<KyRecordMapper, KyRecord>
    implements KyRecordService{

    @Autowired
    private KyRecordMapper kyRecordMapper;

    @Override
    public int batchInsertOrUpdate(List<KyRecord> list) {
        return kyRecordMapper.batchInsertOrUpdate(list);
    }
}




