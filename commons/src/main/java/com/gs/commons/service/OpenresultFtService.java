package com.gs.commons.service;

import com.gs.commons.entity.OpenresultFt;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.commons.entity.OpenresultJsk3;

import java.util.List;

/**
* @author tommm
* @description 针对表【t_openresult_ft】的数据库操作Service
* @createDate 2024-04-04 15:32:12
*/
public interface OpenresultFtService extends IService<OpenresultFt> {
    int batchOpenResult(List<OpenresultFt> list);
}
