package com.gs.commons.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gs.commons.entity.OpenresultMo6hc;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_mo6hc】的数据库操作Mapper
* @createDate 2024-04-04 19:54:40
* @Entity com.gs.commons.entity.OpenresultMo6hc
*/
public interface OpenresultMo6hcMapper extends BaseMapper<OpenresultMo6hc> {
    int batchOpenResult(List<OpenresultMo6hc> list);
}




