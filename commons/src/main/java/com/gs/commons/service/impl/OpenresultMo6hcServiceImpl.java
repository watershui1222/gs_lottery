package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.OpenresultMo6hc;
import com.gs.commons.mapper.OpenresultMo6hcMapper;
import com.gs.commons.service.OpenresultMo6hcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_mo6hc】的数据库操作Service实现
* @createDate 2024-04-04 19:54:40
*/
@Service
public class OpenresultMo6hcServiceImpl extends ServiceImpl<OpenresultMo6hcMapper, OpenresultMo6hc>
    implements OpenresultMo6hcService{
    @Autowired
    private OpenresultMo6hcMapper openresultMo6hcMapper;

    @Override
    public int batchOpenResult(List<OpenresultMo6hc> list) {
        return openresultMo6hcMapper.batchOpenResult(list);
    }
}




