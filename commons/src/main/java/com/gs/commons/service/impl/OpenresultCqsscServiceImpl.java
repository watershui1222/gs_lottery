package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.OpenresultCqssc;
import com.gs.commons.entity.OpenresultJsk3;
import com.gs.commons.service.OpenresultCqsscService;
import com.gs.commons.mapper.OpenresultCqsscMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_cqssc】的数据库操作Service实现
* @createDate 2024-04-04 13:19:04
*/
@Service
public class OpenresultCqsscServiceImpl extends ServiceImpl<OpenresultCqsscMapper, OpenresultCqssc>
    implements OpenresultCqsscService{

    @Autowired
    private OpenresultCqsscMapper openresultCqsscMapper;

    @Override
    public int batchOpenResult(List<OpenresultCqssc> list) {
        return openresultCqsscMapper.batchOpenResult(list);
    }
}




