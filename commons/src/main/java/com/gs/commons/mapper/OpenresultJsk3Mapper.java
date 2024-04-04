package com.gs.commons.mapper;

import com.gs.commons.entity.OpenresultJsk3;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 69000
* @description 针对表【t_openresult_jsk3】的数据库操作Mapper
* @createDate 2024-04-03 17:30:12
* @Entity com.gs.commons.entity.OpenresultJsk3
*/
public interface OpenresultJsk3Mapper extends BaseMapper<OpenresultJsk3> {

    int insertBatchOrUpdate(List<OpenresultJsk3> list);

    int batchOpenResult(List<OpenresultJsk3> list);
}




