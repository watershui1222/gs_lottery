package com.gs.commons.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gs.commons.entity.OpenresultFc3d;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_fc3d】的数据库操作Mapper
* @createDate 2024-04-04 20:33:52
* @Entity com.gs.commons.entity.OpenresultFc3d
*/
public interface OpenresultFc3dMapper extends BaseMapper<OpenresultFc3d> {
    int batchOpenResult(List<OpenresultFc3d> list);
}




