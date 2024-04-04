package com.gs.commons.mapper;

import com.gs.commons.entity.OpenresultBjpk10;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gs.commons.entity.OpenresultCqssc;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_bjpk10】的数据库操作Mapper
* @createDate 2024-04-04 19:21:52
* @Entity com.gs.commons.entity.OpenresultBjpk10
*/
public interface OpenresultBjpk10Mapper extends BaseMapper<OpenresultBjpk10> {
    int batchOpenResult(List<OpenresultBjpk10> list);
}




