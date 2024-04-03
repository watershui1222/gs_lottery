package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.OpenresultJsk3;
import com.gs.commons.service.OpenresultJsk3Service;
import com.gs.commons.mapper.OpenresultJsk3Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 69000
* @description 针对表【t_openresult_jsk3】的数据库操作Service实现
* @createDate 2024-04-03 17:30:12
*/
@Service
public class OpenresultJsk3ServiceImpl extends ServiceImpl<OpenresultJsk3Mapper, OpenresultJsk3>
    implements OpenresultJsk3Service{

    @Autowired
    private OpenresultJsk3Mapper openresultJsk3Mapper;
    @Override
    public int insertBatchOrUpdate(List<OpenresultJsk3> list) {
        return openresultJsk3Mapper.insertBatchOrUpdate(list);
    }
}




