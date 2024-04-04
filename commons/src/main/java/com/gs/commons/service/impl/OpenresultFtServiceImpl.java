package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.OpenresultFt;
import com.gs.commons.entity.OpenresultJsk3;
import com.gs.commons.service.OpenresultFtService;
import com.gs.commons.mapper.OpenresultFtMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_ft】的数据库操作Service实现
* @createDate 2024-04-04 15:32:12
*/
@Service
public class OpenresultFtServiceImpl extends ServiceImpl<OpenresultFtMapper, OpenresultFt>
    implements OpenresultFtService{
    @Autowired
    private OpenresultFtMapper openresultFtMapper;
    @Override
    public int batchOpenResult(List<OpenresultFt> list) {
        return openresultFtMapper.batchOpenResult(list);
    }
}




