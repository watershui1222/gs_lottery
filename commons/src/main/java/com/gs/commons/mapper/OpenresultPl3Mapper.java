package com.gs.commons.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gs.commons.entity.OpenresultPl3;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_pl3】的数据库操作Mapper
* @createDate 2024-04-16 20:00:44
* @Entity com.gs.commons.entity.OpenresultPl3
*/
public interface OpenresultPl3Mapper extends BaseMapper<OpenresultPl3> {
    int batchOpenResult(List<OpenresultPl3> list);
}




