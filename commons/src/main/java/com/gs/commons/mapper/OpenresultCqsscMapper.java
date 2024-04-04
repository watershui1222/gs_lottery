package com.gs.commons.mapper;

import com.gs.commons.entity.OpenresultCqssc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gs.commons.entity.OpenresultJsk3;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_cqssc】的数据库操作Mapper
* @createDate 2024-04-04 13:19:04
* @Entity com.gs.commons.entity.OpenresultCqssc
*/
public interface OpenresultCqsscMapper extends BaseMapper<OpenresultCqssc> {
    int batchOpenResult(List<OpenresultCqssc> list);
}




