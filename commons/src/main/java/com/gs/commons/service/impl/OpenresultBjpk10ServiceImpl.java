package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.OpenresultBjpk10;
import com.gs.commons.mapper.OpenresultBjpk10Mapper;
import com.gs.commons.service.OpenresultBjpk10Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_bjpk10】的数据库操作Service实现
* @createDate 2024-04-04 19:21:52
*/
@Service
public class OpenresultBjpk10ServiceImpl extends ServiceImpl<OpenresultBjpk10Mapper, OpenresultBjpk10>
    implements OpenresultBjpk10Service{
    @Autowired
    private OpenresultBjpk10Mapper openresultBjpk10Mapper;

    @Override
    public int batchOpenResult(List<OpenresultBjpk10> list) {
        return openresultBjpk10Mapper.batchOpenResult(list);
    }
}




