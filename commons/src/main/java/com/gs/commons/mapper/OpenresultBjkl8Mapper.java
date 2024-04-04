package com.gs.commons.mapper;

import com.gs.commons.entity.OpenresultBjkl8;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gs.commons.entity.OpenresultCqssc;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_bjkl8】的数据库操作Mapper
* @createDate 2024-04-04 16:24:11
* @Entity com.gs.commons.entity.OpenresultBjkl8
*/
public interface OpenresultBjkl8Mapper extends BaseMapper<OpenresultBjkl8> {
    int batchOpenResult(List<OpenresultBjkl8> list);
}




