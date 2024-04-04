package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.OpenresultBjkl8;
import com.gs.commons.mapper.OpenresultBjkl8Mapper;
import com.gs.commons.service.OpenresultBjkl8Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_bjkl8】的数据库操作Service实现
* @createDate 2024-04-04 16:24:11
*/
@Service
public class OpenresultBjkl8ServiceImpl extends ServiceImpl<OpenresultBjkl8Mapper, OpenresultBjkl8>
    implements OpenresultBjkl8Service{
    @Autowired
    private OpenresultBjkl8Mapper openresultBjkl8Mapper;

    @Override
    public int batchOpenResult(List<OpenresultBjkl8> list) {
        return openresultBjkl8Mapper.batchOpenResult(list);
    }
}




