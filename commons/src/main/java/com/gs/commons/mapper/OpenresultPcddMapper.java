package com.gs.commons.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gs.commons.entity.OpenresultPcdd;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_pcdd】的数据库操作Mapper
* @createDate 2024-04-04 17:44:12
* @Entity com.gs.commons.entity.OpenresultPcdd
*/
public interface OpenresultPcddMapper extends BaseMapper<OpenresultPcdd> {
    int batchOpenResult(List<OpenresultPcdd> list);
}




