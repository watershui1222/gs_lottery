package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.OpenresultPcdd;
import com.gs.commons.mapper.OpenresultPcddMapper;
import com.gs.commons.service.OpenresultPcddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_pcdd】的数据库操作Service实现
* @createDate 2024-04-04 17:44:12
*/
@Service
public class OpenresultPcddServiceImpl extends ServiceImpl<OpenresultPcddMapper, OpenresultPcdd>
    implements OpenresultPcddService{
    @Autowired
    private OpenresultPcddMapper openresultPcddMapper;

    @Override
    public int batchOpenResult(List<OpenresultPcdd> list) {
        return openresultPcddMapper.batchOpenResult(list);
    }
}




