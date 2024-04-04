package com.gs.commons.mapper;

import com.gs.commons.entity.OpenresultCqssc;
import com.gs.commons.entity.OpenresultFt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_ft】的数据库操作Mapper
* @createDate 2024-04-04 15:32:12
* @Entity com.gs.commons.entity.OpenresultFt
*/
public interface OpenresultFtMapper extends BaseMapper<OpenresultFt> {
    int batchOpenResult(List<OpenresultFt> list);
}




