package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.OpenresultGd11x5;
import com.gs.commons.mapper.OpenresultGd11x5Mapper;
import com.gs.commons.service.OpenresultGd11x5Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_gd11x5】的数据库操作Service实现
* @createDate 2024-04-04 15:56:40
*/
@Service
public class OpenresultGd11x5ServiceImpl extends ServiceImpl<OpenresultGd11x5Mapper, OpenresultGd11x5>
    implements OpenresultGd11x5Service{

    @Autowired
    private OpenresultGd11x5Mapper openresultGd11x5Mapper;

    @Override
    public int batchOpenResult(List<OpenresultGd11x5> list) {
        return openresultGd11x5Mapper.batchOpenResult(list);
    }
}




